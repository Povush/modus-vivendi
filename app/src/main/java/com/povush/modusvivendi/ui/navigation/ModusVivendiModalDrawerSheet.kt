package com.povush.modusvivendi.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.DisabledByDefault
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stream
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.filled.Workspaces
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.povush.modusvivendi.R
import com.povush.modusvivendi.ui.theme.NationalTheme

@Composable
fun ModusVivendiModalDrawerSheet() {
    ModalDrawerSheet(
        modifier = Modifier
            .width(300.dp)
            .shadow(
                elevation = 16.dp,
                clip = false
            ),
        drawerShape = RectangleShape,
        drawerContainerColor = MaterialTheme.colorScheme.primary
    ) {
        AvatarAndHandle()
        GameSections()
    }
}

@Composable
private fun GameSections() {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .fillMaxSize()
            .padding(vertical = 8.dp)
    ) {
        GameMechanics(Icons.Default.Gavel, "Thoughtrealm")
        GameMechanics(Icons.Default.Stream, "Skills")
        GameMechanics(Icons.Default.Home, "Domain")
        GameMechanics(Icons.Default.Money, "Treasure")
        GameMechanics(Icons.Default.Workspaces, "Live modus")
        GameMechanics(Icons.Default.Checkroom, "Appearance")
        GameMechanics(Icons.Default.Task, "Questlines")
        GameMechanics(Icons.Default.Map, "Map")
        GameMechanics(Icons.Default.Groups, "Ecumene")
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        GameMechanics(Icons.Default.WorkspacePremium, "Modifiers")
        GameMechanics(Icons.Default.Settings, "Settings")
        GameMechanics(Icons.Default.Info, "About Universe")
    }
}

@Composable
private fun AvatarAndHandle() {
    var handleExpand by remember { mutableStateOf(false) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color.White),
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_imperial_direction_coat_of_arms),
                contentDescription = null,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.RocketLaunch,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .padding(4.dp)
                .clickable { /*TODO: GOD MODE*/ }
        )
    }
    Spacer(modifier = Modifier.size(16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { handleExpand = !handleExpand },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.padding(bottom = 16.dp)) {
            Text(
                text = "Imperial Direction",
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 24.sp)
            )
            Text(
                text = "@cosmologicalRenaissance",
                style = MaterialTheme.typography.titleSmall
                    .copy(fontSize = 14.sp, shadow = null)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.ExpandMore,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun GameMechanics(iconImageVector: ImageVector, title: String) {
    val interactionSource = remember { MutableInteractionSource() }
    val indication = rememberRipple(color = Color.Red)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable {  }
            .indication(
                interactionSource = interactionSource,
                indication = indication
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = iconImageVector,
            contentDescription = null,
            tint = lerp(Color.Black, Color.White, 0.4f),
            modifier = Modifier.padding(4.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                color = lerp(Color.Black, Color.White, 0.2f),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                shadow = null
                ),
        )
    }
}

@Preview
@Composable
fun ModusVivendiModalDrawerSheetPreview() {
    NationalTheme {
        ModusVivendiModalDrawerSheet()
    }
}