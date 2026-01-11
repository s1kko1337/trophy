package com.example.trophy.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.trophy.domain.model.Photo
import java.io.File

/**
 * Сетка фотографий с возможностью добавления, удаления и выбора основной.
 */
@Composable
fun PhotoGrid(
    photos: List<Photo>,
    onAddClick: () -> Unit,
    onPhotoClick: (Photo) -> Unit,
    onDeleteClick: (Photo) -> Unit,
    onSetPrimaryClick: (Photo) -> Unit,
    modifier: Modifier = Modifier,
    isEditable: Boolean = true
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        // Кнопка добавления
        if (isEditable) {
            item {
                AddPhotoButton(onClick = onAddClick)
            }
        }

        // Фотографии
        items(photos, key = { it.id }) { photo ->
            PhotoGridItem(
                photo = photo,
                onClick = { onPhotoClick(photo) },
                onDeleteClick = { onDeleteClick(photo) },
                onSetPrimaryClick = { onSetPrimaryClick(photo) },
                isEditable = isEditable
            )
        }
    }
}

@Composable
private fun AddPhotoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp)),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Добавить фото",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun PhotoGridItem(
    photo: Photo,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSetPrimaryClick: () -> Unit,
    isEditable: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .then(
                if (photo.isPrimary) {
                    Modifier.border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    )
                } else Modifier
            )
    ) {
        AsyncImage(
            model = File(photo.filePath),
            contentDescription = "Фото",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        if (isEditable) {
            // Кнопка основного фото
            IconButton(
                onClick = onSetPrimaryClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(32.dp)
            ) {
                Icon(
                    if (photo.isPrimary) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    contentDescription = "Сделать основным",
                    tint = if (photo.isPrimary) Color.Yellow else Color.White,
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            Color.Black.copy(alpha = 0.5f),
                            RoundedCornerShape(4.dp)
                        )
                )
            }

            // Кнопка удаления
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(32.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Удалить",
                    tint = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            Color.Black.copy(alpha = 0.5f),
                            RoundedCornerShape(4.dp)
                        )
                )
            }
        }
    }
}

/**
 * Компактная версия - показывает только первые N фото и счётчик.
 */
@Composable
fun PhotoPreviewRow(
    photos: List<Photo>,
    onPhotoClick: (Photo) -> Unit,
    maxPhotos: Int = 3,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(maxPhotos),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        items(photos.take(maxPhotos)) { photo ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { onPhotoClick(photo) }
            ) {
                AsyncImage(
                    model = File(photo.filePath),
                    contentDescription = "Фото",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
