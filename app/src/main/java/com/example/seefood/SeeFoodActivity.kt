package com.example.seefood

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint

/**
 * Основное активити приложения
 */
@AndroidEntryPoint
class SeeFoodActivity : ComponentActivity()
{
   override fun onCreate(savedInstanceState: Bundle?)
   {
      super.onCreate(savedInstanceState)
      setContent { SeeFoodApp() }
   }
}
