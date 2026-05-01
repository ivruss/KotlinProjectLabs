package org.example.project

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import kotlinproject.composeapp.generated.resources.*

@Composable
actual fun PermissionRequestSection() {
    Text(
        text = stringResource(Res.string.jvm_permission_not_supported),
        modifier = Modifier.padding(16.dp)
    )
}
