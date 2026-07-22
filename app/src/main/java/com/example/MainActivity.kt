package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.HydrationScreen
import com.example.ui.HydrationViewModel
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.HydroTrackTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      HydroTrackTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = DarkBackground
        ) {
          val hydrationViewModel: HydrationViewModel = viewModel()
          HydrationScreen(viewModel = hydrationViewModel)
        }
      }
    }
  }
}

