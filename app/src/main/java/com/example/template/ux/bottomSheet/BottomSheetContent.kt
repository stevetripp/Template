package com.example.template.ux.bottomSheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun BottomSheetContent(scaffoldState: BottomSheetScaffoldState) {
    val scope = rememberCoroutineScope()
    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(title = { Text(text = "Sheet content") })
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
}