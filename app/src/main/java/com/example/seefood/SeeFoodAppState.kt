package com.example.seefood

import android.content.Context
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController

/**
 * Класс состояния приложения
 *
 * @constructor
 * @param[navController] контроллер навигации
 * @param[context] контекст
 */
@Stable
class SeeFoodAppState(
   val navController: NavHostController,
   val context: Context,
) {
   /**
    * Переход к экрану
    *
    * @param[route] путь до экрана
    */
   fun navigate(route: String) {
      navController.navigate(route) { launchSingleTop = true }
   }

   /**
    * Переход к предыдущему экрану
    */
   fun navigateBack(){
      navController.navigateUp()
   }
}