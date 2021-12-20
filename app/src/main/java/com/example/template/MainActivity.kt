package com.example.template

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.template.ui.screen.AnimatedGestureScreen
import com.example.template.ui.screen.BottomSheetScreen
import com.example.template.ui.screen.PanningZoomingScreen
import com.example.template.ui.screen.HomeScreen
import com.example.template.ui.screen.NavigationPagerScreen
import com.example.template.ui.screen.PagerScreen
import com.example.template.ui.screen.SnackbarScreen
import com.example.template.ui.screen.SwipableScreen
import com.example.template.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        startup(savedInstanceState)
        setContent {
            AppTheme {
                var nav by remember { mutableStateOf(Nav.HOME) }
                Screen(nav = nav) { nav = it }
            }
        }
    }

    private fun startup(savedInstanceState: Bundle?) {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check if the initial data is ready.
                    return if (true) {
                        // The content is ready... start drawing.
                        content.viewTreeObserver.removeOnPreDrawListener(this)

                        // finish regular onCreate() code
                        finishCreate(savedInstanceState)
                        true
                    } else {
                        // The content is not ready... suspend.
                        false
                    }
                }
            }
        )
    }

    private fun finishCreate(savedInstanceState: Bundle?) {
    }
}

enum class Nav(val title: String) {
    HOME("Template"),
    ANIMATED_GESTURE("Animated Gesture"),
    BOTTOM_SHEET("Bottom Sheet"),
    NAVIGATION_PAGER("Navigation Pager"),
    PAGER("Pager"),
    PANNING_ZOOMING("Panning and Zooming"),
    SNACKBAR("Snackbar"),
    SWIPABLE("Swipable");
}

@Composable
private fun Screen(nav: Nav, onNavigate: (Nav) -> Unit) {
    val onBack: () -> Unit = { onNavigate(Nav.HOME) }
    Surface(color = MaterialTheme.colors.background) {
        when (nav) {
            Nav.HOME -> HomeScreen(nav, onNavigate)
            Nav.PAGER -> PagerScreen(nav, onBack)
            Nav.NAVIGATION_PAGER -> NavigationPagerScreen(nav, onBack)
            Nav.SWIPABLE -> SwipableScreen(nav, onBack)
            Nav.ANIMATED_GESTURE -> AnimatedGestureScreen(nav, onBack)
            Nav.BOTTOM_SHEET -> BottomSheetScreen(nav, onBack)
            Nav.SNACKBAR -> SnackbarScreen(nav, onBack)
            Nav.PANNING_ZOOMING-> PanningZoomingScreen(nav, onBack)
        }
    }
}

@Composable
fun AppBar(nav: Nav, onBack: () -> Unit = {}) {
    val navIcon: (@Composable () -> Unit)? = if (nav == Nav.HOME) null else {
        {
            IconButton(onClick = { onBack() }) {
                Icon(Icons.Default.ArrowBack, "Back")
            }
        }
    }
    TopAppBar(title = { Text(text = nav.title) }, navigationIcon = navIcon)
}
