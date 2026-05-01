package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import kotlinproject.composeapp.generated.resources.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(initialItems: List<ShoppingItem>? = null) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    val milkString = stringResource(Res.string.milk)
    val flourString = stringResource(Res.string.flour)

    var text by remember { mutableStateOf("") }
    var items by remember {
        mutableStateOf(
            initialItems ?: listOf(
                ShoppingItem(milkString, 1),
                ShoppingItem(flourString, 1)
            )
        )
    }

    var itemToDelete by remember { mutableStateOf<ShoppingItem?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val isWideScreen = maxWidth > 600.dp

            if (isWideScreen) {
                Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        ShoppingInput(
                            text = text,
                            onTextChange = { text = it },
                            onAdd = {
                                if (text.isNotBlank()) {
                                    val addedName = text
                                    items = items + ShoppingItem(addedName, 1)
                                    text = ""
                                    scope.launch {
                                        val message = getString(Res.string.item_added_message, addedName)
                                        snackbarHostState.showSnackbar(message)
                                    }
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(2f)) {
                        ShoppingList(
                            items = items,
                            onCheckedChange = { item, checked ->
                                items = items.map {
                                    if (it == item) it.copy(isChecked = checked)
                                    else it
                                }
                            },
                            onDeleteRequest = { itemToDelete = it }
                        )
                    }
                }
            } else {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    ShoppingInput(
                        text = text,
                        onTextChange = { text = it },
                        onAdd = {
                            if (text.isNotBlank()) {
                                val addedName = text
                                items = items + ShoppingItem(addedName, 1)
                                text = ""
                                scope.launch {
                                    val message = getString(Res.string.item_added_message, addedName)
                                    snackbarHostState.showSnackbar(message)
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ShoppingList(
                        items = items,
                        onCheckedChange = { item, checked ->
                            items = items.map {
                                if (it == item) it.copy(isChecked = checked)
                                else it
                            }
                        },
                        onDeleteRequest = { itemToDelete = it }
                    )
                }
            }
        }
    }

    itemToDelete?.let { item ->
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = { Text(stringResource(Res.string.delete_confirmation_title)) },
            text = { Text(stringResource(Res.string.delete_confirmation_text, item.name)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        val deletedName = item.name
                        items = items - item
                        itemToDelete = null
                        scope.launch {
                            val message = getString(Res.string.item_deleted_message, deletedName)
                            snackbarHostState.showSnackbar(message)
                        }
                    }
                ) {
                    Text(stringResource(Res.string.confirm_button))
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) {
                    Text(stringResource(Res.string.cancel_button))
                }
            }
        )
    }
}

@Composable
fun ShoppingInput(
    text: String,
    onTextChange: (String) -> Unit,
    onAdd: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            label = { Text(stringResource(Res.string.product_name_label)) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onAdd) {
            Text(stringResource(Res.string.add_button_text))
        }
    }
}

@Composable
fun ShoppingList(
    items: List<ShoppingItem>,
    onCheckedChange: (ShoppingItem, Boolean) -> Unit,
    onDeleteRequest: (ShoppingItem) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = item.isChecked,
                    onCheckedChange = { onCheckedChange(item, it) }
                )
                Text(
                    text = item.name,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { onDeleteRequest(item) }) {
                    Text(stringResource(Res.string.delete_button_text))
                }
            }
        }
    }
}
