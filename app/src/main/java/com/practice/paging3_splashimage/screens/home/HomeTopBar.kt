package com.practice.paging3_splashimage.screens.home

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    onSearchClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Home",
                color = MaterialTheme.colorScheme.background
            )
        },
        Modifier.background(MaterialTheme.colorScheme.onPrimary),
        actions = {
            IconButton(onClick = onSearchClicked) {
                Icon(
                   imageVector = Icons.Default.Search,
                    contentDescription = "searchIcon"
                )
            }
        }

    )
}