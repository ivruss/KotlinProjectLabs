package org.example.project

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import org.jetbrains.compose.resources.stringResource
import kotlinproject.composeapp.generated.resources.*

@Composable
actual fun PermissionRequestSection() {
    val context = LocalContext.current
    
    // Запрос уведомлений актуален только для Android 13+ (API 33)
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.POST_NOTIFICATIONS
    } else {
        null
    }

    var isGranted by remember {
        mutableStateOf(
            permission?.let {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            } ?: true
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted = it }
    )

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.permissions_title),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            if (isGranted) {
                Text(stringResource(Res.string.permission_granted))
            } else {
                Text(stringResource(Res.string.permission_denied))
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    permission?.let { launcher.launch(it) }
                }) {
                    Text(stringResource(Res.string.request_permission))
                }
            }
        }
    }
}
