package com.example.template.ux.webview

import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ux.main.Screen

@Composable
fun WebViewScreen(navController: NavController) {
    WebViewContent(navController::popBackStack)
}

@Composable
private fun WebViewContent(onBack: () -> Unit) {
    val backgroundColor = Color.White
    val textColor = Color.Black
    val linkColor = Color.Cyan
    var webView by remember { mutableStateOf<WebView?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            webView?.let {
                Log.i("SMT", "Destroy()")
                it.destroy()
                webView = null
            }
        }
    }

    val body =
        """<p>Help children understand rhythm and how to lead music at home or church.</p><p>Invite a child to pick a favorite Primary song or hymn. Sing it together, and have the children 
            |mark the rhythm by clapping, tapping sticks or pencils together, stomping their feet, and so on. Repeat as many times as you like, with each child picking a different song to show 
            |how songs have different rhythms and speeds.</p><p>Explain that music leaders in Primary and sacrament meeting wave their hands in a certain way to help keep everyone on the 
            |same beat. Show the children the standard conducting patterns at the back of the hymnbook or the <i>Children’s Songbook,</i> if available.</p><p>Demonstrate how to make the 
            |patterns with your hands, and have the children practice conducting the music as they sing their favorite songs.</p><p>Please adapt activities as necessary to ensure all 
            |individuals are able to participate, belong, and contribute.</p><ul><li>Have the children take turns leading everyone in a song, speeding up or slowing down as much as they 
            |like. This will help the children learn to watch the song leader.</li><li>Invite the music leader for Primary or sacrament meeting to help with or lead this activity.</li><li>Make 
            |or use simple instruments, such as shakers, drums, or rhythm sticks to help keep the beat during the songs.</li><li>Show the children how to read the time signature for each song to 
            |know how many beats are in each measure.</li><li>As a follow-up, ask the Primary music leader if the children can take turns leading a song during singing time.</li><li><b>Service 
            |idea: </b>Ask the children to think of someone they could help feel happy by sharing a special song or musical number. Invite them to sing, play, or share a recording of a 
            |song or music. If social media is available, consider how they can share a song or music with someone who does not live nearby.</li></ul><p>Encourage children to talk about how 
            |what they are learning can help them and others grow closer to Heavenly Father and Jesus Christ. Discussions can take place before, during, or after the activity and should last just 
            |a few minutes. You could ask questions like the following:<br></p><ul><li>How can singing songs at home and at church help us show Heavenly Father and Jesus Christ that we love 
            |them? (see <a href="http://www.churchofjesuschrist.org/study/scriptures/dc-testament/dc/25.html?verse=12#p12" target="_blank">Doctrine and Covenants 25:12</a>).</li><li>How can good 
            |music help us feel the Spirit?</li></ul><ul></ul><ul></ul><ul><li><a href="https://www.churchofjesuschrist.org/youth/childrenandyouth/discover/understanding-clouds?lang=eng">
            |Understanding Clouds</a></li><li><a href="https://www.churchofjesuschrist.org/youth/childrenandyouth/discover/activity-exploring-science?lang=eng">Exploring Science</a></li><li><a>
            |Mapping the Solar System</a></li><li><a href="https://www.churchofjesuschrist.org/youth/childrenandyouth/discover/activity-learning-about-stars?lang=eng">Learning about Stars</a></li><li>
            |<a href="https://www.churchofjesuschrist.org/youth/childrenandyouth/discover/my-family?lang=eng">Learning about My Family</a></li><li><a href="https://www.churchofjesuschrist.org/youth/
            |childrenandyouth/discover/learning-is-fun?lang=eng">Learning Is Fun</a></li><li><a href="https://www.churchofjesuschrist.org/youth/childrenandyouth/discover/activity-enjoying-music?
            |lang=eng">Enjoying Music</a></li></ul><p></p>""".trimMargin()
    val page = """
                    <!DOCTYPE html>
                    <html>
                        <head>
                            <meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no'>
                            <style>
                                body {
                                    background-color: $backgroundColor;
                                    color: $textColor;
                                }
                                a:link {
                                    color: $linkColor;
                                }
                            </style>
                        </head>
                        <body>$body</body>
                    </html>""".trimIndent()

    Scaffold(topBar = { AppTopAppBar(title = Screen.WEBVIEW.title, onBack = onBack) }) { paddingValues ->
        AndroidView(
            modifier = Modifier.padding(paddingValues),
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    settings.javaScriptEnabled = true
// Uncomment to handle link clicks
//                    webViewClient = object : WebViewClient() {
//                        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
//                            // Capture URL for Analytic logging
//                            request?.url?.let { onNavigate(it) }
//                            return true
//                        }
//                    }
                }.also { webView = it }
            },
            update = {
//                it.loadData("<p>Help children understand rhythm and how to lead music at home or church.</p>", "text/html", "UTF-8")

                // Use base64 encoding to get around Android 10+ loadData problem. See https://stackoverflow.com/a/58182876
                val encodedHtml = Base64.encodeToString(page.toByteArray(), Base64.NO_PADDING)
                it.loadData(encodedHtml, "text/html", "base64")
            }
        )
    }
}