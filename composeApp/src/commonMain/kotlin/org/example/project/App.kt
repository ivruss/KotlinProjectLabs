package org.example.project

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview


data class ShoppingItem(
    val name: String,
    val quantity: Int
)

val shoppingList = listOf(
    ShoppingItem("Молоко", 2),
    ShoppingItem("Хлеб", 1),
    ShoppingItem("Яйца", 10),
    ShoppingItem("Яблоки", 5)
)

@Composable
@Preview
fun App() {
    ShoppingListScreen()
}