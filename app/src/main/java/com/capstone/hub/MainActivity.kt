package com.capstone.hub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.capstone.hub.ui.HubScreen
import com.capstone.hub.ui.theme.CapstoneHubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CapstoneHubTheme {
                HubScreen()
            }
        }
    }
}
