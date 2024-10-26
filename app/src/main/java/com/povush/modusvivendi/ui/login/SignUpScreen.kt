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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.povush.modusvivendi.R
import com.povush.modusvivendi.ui.navigation.NavigationDestination

object SignUpDestination : NavigationDestination {
    override val route = "sign_up"
    override val titleRes = R.string.sign_up
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navigateBack: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
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
                value = "",
                onValueChange = {  },
                label = { Text(text = "Country name") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium
            )
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
                onClick = viewModel::createCountry,
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = "[S] Create country")
            }
        }
    }
}
