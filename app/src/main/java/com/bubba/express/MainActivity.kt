package com.bubba.express

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.bubba.express.navigation.AppNavigation
import com.bubba.express.ui.theme.LaBubbaExpressTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaBubbaExpressTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}
