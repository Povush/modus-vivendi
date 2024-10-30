package com.povush.modusvivendi.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.povush.modusvivendi.R
import com.povush.modusvivendi.ui.navigation.NavigationDestination

object SignInDestination : NavigationDestination {
    override val route = "sign_in"
    override val titleRes = R.string.sign_in
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    navigateBack: () -> Unit,
    navigateToSignUp: () -> Unit,
    navigateToGame: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier,
//        topBar = {
//            TopAppBar(
//                title = {  },
//                navigationIcon = {
//                    IconButton(onClick = { navigateBack() }) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                            contentDescription = null,
//                            tint = Color.Black,
//                        )
//                    }
//                },
//                colors = TopAppBarColors(
//                    containerColor = Color.Transparent,
//                    scrolledContainerColor = Color.Transparent,
//                    navigationIconContentColor = Color.Black,
//                    titleContentColor = Color.Transparent,
//                    actionIconContentColor = Color.Transparent
//                )
//            )
//        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text(text = "Email") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium
            )
            TextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium
            )
            Button(
                onClick = { navigateToSignUp() },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = "I don't have an account yet")
            }
            Button(
                onClick = {
                    viewModel.enter()
                    navigateToGame()
                },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = "[S] Enter")
            }
        }
    }
}
