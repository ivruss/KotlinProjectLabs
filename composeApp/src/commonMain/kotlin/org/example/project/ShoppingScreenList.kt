package org.example.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.runtime.Composable

@Composable
fun ShoppingListScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(shoppingList) { item ->
            ShoppingItemView(item)
        }
    }
}
