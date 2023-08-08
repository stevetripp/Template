package com.example.template.ux.carousel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.composable.DropdownList
import com.example.template.ui.composable.DropdownOption
import com.example.template.ui.theme.AppTheme
import com.example.template.ui.widget.Carousel
import com.example.template.ux.main.Screen

@Composable
fun CarouselScreen(navController: NavController) {
    CarouselContent(navController::popBackStack)
}

@Composable
private fun CarouselContent(onBack: () -> Unit = {}) {
    val dropDownOptions = listOf(
        DropdownOption("1"),
        DropdownOption("2"),
        DropdownOption("3"),
        DropdownOption("4"),
        DropdownOption("5"),
    )
    var selectedDropdownOption by remember { mutableStateOf(dropDownOptions[4]) }
    val cardCount = selectedDropdownOption.selectedValue.toInt()

    Scaffold(topBar = { AppTopAppBar(title = Screen.CAROUSEL.title, onBack = onBack) }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            DropdownList(value = selectedDropdownOption.selectedValue, label = "Card Count", options = dropDownOptions, onValueChanged = {
                selectedDropdownOption = it
            })
            Carousel(modifier = Modifier.height(200.dp), itemCount = cardCount) { itemIndex ->
                OutlinedCard(modifier = Modifier.padding(horizontal = 8.dp)) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(modifier = Modifier.align(Alignment.Center), text = itemIndex.toString())
                    }
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun Preview() {
    AppTheme { CarouselContent() }
}