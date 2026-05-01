package org.example.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

@Composable
fun ShoppingListScreen() {
    var text by remember { mutableStateOf("") }
    var items by remember {
        mutableStateOf(
            listOf(
                ShoppingItem("Молоко", 1),
                ShoppingItem("Мука", 1)
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                label = { Text("Название продукта") }
            )

            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        items = items + ShoppingItem(text, 1)
                        text = ""
                    }
                }
            ) {
                Text("+")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(items) { item ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = item.isChecked,
                        onCheckedChange = { checked ->
                            items = items.map {
                                if (it == item) it.copy(isChecked = checked)
                                else it
                            }
                        }
                    )

                    Text(
                        text = item.name,
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick = {
                            items = items - item
                        }
                    ) {
                        Text("🗑")
                    }
                }
            }
        }
    }
}

