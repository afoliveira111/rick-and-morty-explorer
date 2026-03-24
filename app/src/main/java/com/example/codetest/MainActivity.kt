package com.example.codetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.codetest.navigation.AppNavGraph
import com.example.codetest.ui.theme.CodeTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CodeTestTheme {
                AppNavGraph()
            }
        }
    }
}