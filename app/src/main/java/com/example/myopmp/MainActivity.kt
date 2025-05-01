package com.example.myopmp

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myopmp.ui.theme.MyOPMPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyOPMPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GreetingScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Функція для активації вібрації
fun vibratePhone(context: Context) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(50)
    }
}

@Composable
fun GreetingScreen(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("Привіт, світ!") }
    var clickCount by remember { mutableStateOf(0) }
    var isPressed by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1.0f,
        animationSpec = tween(durationMillis = 100),
        label = "scale"
    )

    val buttonColor = when {
        clickCount >= 30 -> Color.Yellow
        clickCount >= 20 -> Color.Green
        clickCount >= 10 -> Color.Red
        else -> MaterialTheme.colorScheme.primary
    }

    val buttonSize = when {
        clickCount >= 30 -> 160.dp
        clickCount >= 20 -> 140.dp
        clickCount >= 10 -> 120.dp
        else -> 100.dp
    }

    val clicksToNextChange = when {
        clickCount < 10 -> 10 - clickCount
        clickCount < 20 -> 20 - clickCount
        clickCount < 30 -> 30 - clickCount
        else -> 0
    }

    val nextChangeMessage = when {
        clickCount < 10 -> "До червоного кольору залишилось: $clicksToNextChange"
        clickCount < 20 -> "До зеленого кольору залишилось: $clicksToNextChange"
        clickCount < 30 -> "До жовтого кольору залишилось: $clicksToNextChange"
        else -> "Досягнуто максимального рівня!"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Кількість кліків: $clickCount", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = nextChangeMessage, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                clickCount++

                vibratePhone(context)

                if (clickCount == 10 || clickCount == 20 || clickCount == 30) {
                    vibratePhone(context)
                    vibratePhone(context)
                }

                text = "Ти натиснув кнопку $clickCount разів!"
                isPressed = true
            },
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            modifier = Modifier
                .scale(scale)
                .size(buttonSize)
        ) {
            Text("Натисни мене")
        }

        Spacer(modifier = Modifier.height(20.dp))
        TextButton(onClick = {
            clickCount = 0
            text = "Лічильник скинуто!"
        }) {
            Text("Скинути лічильник")
        }
    }

        if (isPressed) {
        isPressed = false
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyOPMPTheme {
        GreetingScreen()
    }
}