package com.gzautoreplay.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gzautoreplay.app.ui.rules.RulesScreen
import com.gzautoreplay.app.ui.theme.GZAutoReplayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GZAutoReplayTheme {
                RulesScreen()
            }
        }
    }
}
