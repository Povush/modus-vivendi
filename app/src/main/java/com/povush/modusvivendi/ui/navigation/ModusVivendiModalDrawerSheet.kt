package com.povush.modusvivendi.ui.navigation

import androidx.compose.foundation.OverscrollEffect
import android.os.Build
import android.view.HapticFeedbackConstants
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.ScrollableDefaults.overscrollEffect
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.PlaylistAddCheckCircle
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.filled.Workspaces
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.povush.modusvivendi.R
import com.povush.modusvivendi.ui.about_universe.AboutUniverseDestination
import com.povush.modusvivendi.ui.appearance.AppearanceDestination
import com.povush.modusvivendi.ui.common.components.ModusVivendiDropdownMenuItem
import com.povush.modusvivendi.ui.domain.DomainDestination
import com.povush.modusvivendi.ui.ecumene.EcumeneDestination
import com.povush.modusvivendi.ui.map.MapDestination
import com.povush.modusvivendi.ui.modifiers.ModifiersDestination
import com.povush.modusvivendi.ui.questlines.screens.QuestlinesDestination
import com.povush.modusvivendi.ui.routine.RoutineDestination
import com.povush.modusvivendi.ui.settings.SettingsDestination
import com.povush.modusvivendi.ui.technologies.TechnologiesDestination
import com.povush.modusvivendi.ui.theme.NationalTheme
import com.povush.modusvivendi.ui.thoughtrealm.ThoughtrealmDestination
import com.povush.modusvivendi.ui.treasure.TreasureDestination
import kotlinx.coroutines.delay

@Composable
fun ModusVivendiModalDrawerSheet(
    navController: NavHostController,
    closeDrawerState: () -> Unit,
    navigateToLogin: () -> Unit,
    currentDestination: String?,
    viewModel: ModalNavigationViewModel = hiltViewModel()
) {
    ModalDrawerSheet(
        modifier = Modifier
            .width(300.dp)
            .fillMaxHeight()
            .shadow(
                elevation = 16.dp,
                clip = false
            ),
        drawerShape = RectangleShape,
        drawerContainerColor = Color.White
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val scrollState = rememberScrollState()

        Box(modifier = Modifier
            .background(Color.White)
            .fillMaxHeight()
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
            ) {
                AvatarAndHandle(
                    coatOfArmsRes = uiState.coatOfArmsRes,
                    countryName = uiState.countryName,
                    handle = uiState.handle,
                    onHandleClicked = viewModel::onHandleClicked,
                    onGodModeClicked = viewModel::switchGodMode,
                    isGodMode = uiState.isGodMode,
                    accountsExpanded = uiState.accountsExpanded
                )
                GameSections(
                    navController = navController,
                    closeDrawerState = closeDrawerState,
                    accountsExpanded = uiState.accountsExpanded,
                    countryName = uiState.countryName,
                    coatOfArmsRes = uiState.coatOfArmsRes,
                    exitGame = {
                        viewModel.exitGame()
                        navigateToLogin()
                    },
                    currentDestination = currentDestination
                )
            }
        }
    }
}

@Composable
private fun GameSections(
    navController: NavHostController,
    closeDrawerState: () -> Unit,
    accountsExpanded: Boolean,
    countryName: String,
    @DrawableRes coatOfArmsRes: Int,
    exitGame: () -> Unit,
    currentDestination: String?
) {
    val gameMechanicsRoutes: List<Pair<ImageVector, NavigationDestination>> = listOf(
        Icons.Default.Gavel to ThoughtrealmDestination,
        Icons.Default.Home to DomainDestination,
        Icons.Default.Money to TreasureDestination,
        Icons.Default.Psychology to TechnologiesDestination,
        Icons.Default.Workspaces to RoutineDestination,
        Icons.Default.AutoAwesome to AppearanceDestination,
        Icons.Default.PlaylistAddCheckCircle to QuestlinesDestination,
        Icons.Default.Map to MapDestination,
        Icons.Default.Groups to EcumeneDestination,
    )

    val otherRoutes: List<Pair<ImageVector, NavigationDestination>> = listOf(
        Icons.Default.WorkspacePremium to ModifiersDestination,
        Icons.Default.Settings to SettingsDestination,
        Icons.Default.Info to AboutUniverseDestination
    )

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = Color.White)
            .animateContentSize()
    ) {
        AnimatedVisibility(
            visible = accountsExpanded,
            enter = expandVertically(), // Появление сверху
            exit = shrinkVertically() // Исчезновение вверх
        ) {
            Accounts(coatOfArmsRes, countryName, exitGame)
        }
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            gameMechanicsRoutes.forEach { pair ->
                val icon = pair.first
                val destination = pair.second

                GameMechanicsRoute(
                    iconImageVector = icon,
                    title = stringResource(destination.titleRes),
                    onClicked = {
                        if (navController.currentDestination?.route != destination.route) {
                            navController.navigate(destination.route)
                        }
                        closeDrawerState()
                    },
                    isChosen = currentDestination == destination.route
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            otherRoutes.forEach { pair ->
                val icon = pair.first
                val destination = pair.second

                GameMechanicsRoute(
                    iconImageVector = icon,
                    title = stringResource(destination.titleRes),
                    onClicked = {
                        if (navController.currentDestination?.route != destination.route) {
                            navController.navigate(destination.route)
                        }
                        closeDrawerState()
                    },
                    isChosen = currentDestination == destination.route
                )
            }
        }
    }
}

@Composable
private fun AvatarAndHandle(
    @DrawableRes coatOfArmsRes: Int,
    countryName: String,
    handle: String,
    onHandleClicked: () -> Unit,
    onGodModeClicked: () -> Unit,
    isGodMode: Boolean,
    accountsExpanded: Boolean,
) {
    val context = LocalContext.current
    val view = LocalView.current

    var isGodModeButtonScaled by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isGodModeButtonScaled) 0.8f else 1f,
        animationSpec = tween(durationMillis = 300),
        label = "God mode animation"
    )

    LaunchedEffect(isGodMode) {
        isGodModeButtonScaled = true
        delay(150)
        isGodModeButtonScaled = false
    }

    Column(modifier = Modifier
        .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color.White),
            ) {
                Image(
                    painter = painterResource(coatOfArmsRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.ic_spirograph_3),
                contentDescription = null,
                tint =
                if (isGodMode) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(36.dp)
                    .scale(scale)
                    .padding(4.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        val message =
                            if (!isGodMode) R.string.god_mode_is_on
                            else R.string.god_mode_is_off
                        onGodModeClicked()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                        }
                        Toast
                            .makeText(context, context.getString(message), Toast.LENGTH_SHORT)
                            .show()
                    }
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onHandleClicked() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = countryName,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 24.sp)
                )
                Text(
                    text = handle,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleSmall
                        .copy(fontSize = 14.sp, shadow = null)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector =
                    if (!accountsExpanded) Icons.Default.ExpandMore
                    else Icons.Default.ExpandLess,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun Accounts(
    coatOfArmsRes: Int,
    countryName: String,
    exitGame: () -> Unit
) {
    val view = LocalView.current

    var menuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(color = Color.White)
            .wrapContentSize()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = { /*TODO*/ },
                    onLongClick = {
                        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                        menuExpanded = true
                    }
                )
                .padding(vertical = 2.dp)
        ) {
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                modifier = Modifier
                    .background(Color.White)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(8.dp),
                shadowElevation = 8.dp,
                offset = DpOffset(x = 8.dp, y = 4.dp)
            ) {
                ModusVivendiDropdownMenuItem(R.string.exit_game) {
                    menuExpanded = false
                    exitGame()
                }
            }

            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(36.dp)
            ) {
                Image(
                    painter = painterResource(coatOfArmsRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(17.dp)
                        .background(color = Color.White, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF53CA5E),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Text(
                text = countryName,
                color = lerp(Color.Black, Color.White, 0.3f),
                style = MaterialTheme.typography.titleSmall
                    .copy(fontSize = 16.sp, shadow = null, fontWeight = FontWeight.Bold)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
                .clickable { /*TODO*/ }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = lerp(Color.Black, Color.White, 0.4f),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(36.dp)
            )
            Text(
                text = "Add account",
                color = lerp(Color.Black, Color.White, 0.3f),
                style = MaterialTheme.typography.titleSmall
                    .copy(fontSize = 16.sp, shadow = null, fontWeight = FontWeight.Bold)
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
        HorizontalDivider()
    }
}

@Composable
private fun GameMechanicsRoute(
    iconImageVector: ImageVector,
    title: String,
    onClicked: () -> Unit,
    isChosen: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClicked() }
            .background(color = if (isChosen) lerp(Color.White, Color.Black, 0.1f) else Color.White)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = iconImageVector,
            contentDescription = null,
            tint = lerp(Color.Black, Color.White, 0.4f),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview
@Composable
fun ModusVivendiModalDrawerSheetPreview() {
    val navController = rememberNavController()

    NationalTheme {
        ModusVivendiModalDrawerSheet(navController, {}, {}, null)
    }
}