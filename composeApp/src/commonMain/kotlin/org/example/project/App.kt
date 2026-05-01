package org.example.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.stringResource
import kotlinproject.composeapp.generated.resources.*


data class ShoppingItem(
    val name: String,
    val quantity: Int,
    val isChecked: Boolean = false
)

@Composable
@Preview
fun App() {
    AppTheme {
        val shoppingList = listOf(
            ShoppingItem(stringResource(Res.string.milk), 2),
            ShoppingItem(stringResource(Res.string.bread), 1),
            ShoppingItem(stringResource(Res.string.eggs), 10),
            ShoppingItem(stringResource(Res.string.apples), 5)
        )
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ShoppingListScreen(shoppingList)
        }
    }
}
