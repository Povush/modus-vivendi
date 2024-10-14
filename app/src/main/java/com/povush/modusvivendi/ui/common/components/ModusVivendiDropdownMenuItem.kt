package com.povush.modusvivendi.ui.common.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun ModusVivendiDropdownMenuItem(
    @StringRes textRes: Int,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = { Text(text = stringResource(textRes), style = MaterialTheme.typography.bodyLarge) },
        onClick = onClick,
        modifier = Modifier.height(36.dp),
    )
}
