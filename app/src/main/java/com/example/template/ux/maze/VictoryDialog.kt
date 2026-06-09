package com.example.template.ux.maze

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

/**
 * Composable rendering the victory/success pop-up dialog.
 *
 * @param timeElapsed The total elapsed time in milliseconds.
 * @param moveCount The total number of steps the player has taken.
 * @param isGameCompleted Flag indicating if the maze has been solved.
 * @param onRegenerate Callback to start a new game/regenerate the maze.
 * @param onBack Callback to return to the previous screen/menu.
 */
@Composable
fun VictoryDialog(
    timeElapsed: Long,
    moveCount: Int,
    isGameCompleted: Boolean,
    onRegenerate: () -> Unit,
    onBack: () -> Unit
) {
    if (isGameCompleted) {
        AlertDialog(
            onDismissRequest = { /* Prevent dismiss by clicking outside */ },
            title = {
                Text(
                    text = "🏆 Maze Solved!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00FF66),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Congratulations, you escaped the maze!",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Time", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            val sec = timeElapsed / 1000.0
                            Text(text = String.format(Locale.getDefault(), "%.2f s", sec), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Moves", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = moveCount.toString(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onRegenerate,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FF66), contentColor = Color.Black)
                ) {
                    Text("Play Again", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = onBack) {
                    Text("Back to Menu")
                }
            }
        )
    }
}
