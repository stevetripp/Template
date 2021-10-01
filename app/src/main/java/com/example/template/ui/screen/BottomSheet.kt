package com.example.template.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.template.AppBar
import com.example.template.Nav
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(nav: Nav, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    var showSnackbar by remember { mutableStateOf(false) }
    Scaffold(topBar = { AppBar(nav, onBack) }) {
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberBottomSheetScaffoldState()
        Box(
            modifier = Modifier
                .background(Color.Blue)
                .fillMaxSize()
        )
        BottomSheetScaffold(
            sheetContent = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(128.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Swipe up to expand sheet")
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Sheet content")
                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = {
                            scope.launch { scaffoldState.bottomSheetState.collapse() }
                        }
                    ) {
                        Text("Click to collapse sheet")
                    }
                }
            },
            scaffoldState = scaffoldState,
//            topBar ={ AppBar(nav, onBack) },
            topBar = {
                TopAppBar(
                    title = { Text("Bottom sheet scaffold") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Localized description")
                        }
                    }
                )
            },
            floatingActionButton = {
                var clickCount by remember { mutableStateOf(0) }
                FloatingActionButton(
                    onClick = { showSnackbar = true }
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = "Localized description")
                }
            },
            floatingActionButtonPosition = FabPosition.End,
//            sheetPeekHeight = 128.dp,
//            drawerContent = {
//                Column(
//                    Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text("Drawer content")
//                    Spacer(Modifier.height(20.dp))
//                    Button(onClick = { scope.launch { scaffoldState.drawerState.close() } }) {
//                        Text("Click to close drawer")
//                    }
//                }
//            }
        ) { innerPadding ->
            LazyColumn(contentPadding = innerPadding) {
                items(100) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(Color.Blue)
                    )
                }
            }
        }
        LaunchedEffect(key1 = showSnackbar) {
            if (showSnackbar) {
                scaffoldState.snackbarHostState.showSnackbar("Message")
                showSnackbar = false
            }
        }
    }
}