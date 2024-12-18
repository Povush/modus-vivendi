package com.povush.modusvivendi.ui.common.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.povush.modusvivendi.R

@Composable
fun ModusVivendiDropdownMenuItem(
    @StringRes textRes: Int,
    onClick: () -> Unit,
    leadingIcon: ImageVector? = null,
    isDangerous: Boolean = false,
    textStyle: TextStyle? = null
) {
    DropdownMenuItem(
        text = {
            Text(
                text = stringResource(textRes),
                style = textStyle ?: MaterialTheme.typography.bodyLarge.copy(
                    color = if (isDangerous) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                    fontFamily = FontFamily(
                        Font(R.font.exo2)
                    ),
                    fontWeight = FontWeight.Bold
                )
            )
        },
        onClick = onClick,
        modifier = Modifier.height(36.dp),
        leadingIcon = if (leadingIcon != null) {
            {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = if (isDangerous) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
                )
            }
        } else null
    )
}
