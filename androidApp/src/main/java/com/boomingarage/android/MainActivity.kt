package com.boomingarage.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.boomingarage.android.navigation.AppNavGraph
import com.boomingarage.android.ui.theme.BoominGarageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BoominGarageTheme {
                AppNavGraph()
            }
        }
    }
}
