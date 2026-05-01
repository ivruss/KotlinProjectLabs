package org.example.project

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.runtime.Composable



@Composable
fun ShoppingItemView(item: ShoppingItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.name)
        Text(text = "x${item.quantity}")
    }
}
