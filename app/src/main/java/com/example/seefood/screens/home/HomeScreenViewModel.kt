package com.example.seefood.screens.home

import androidx.lifecycle.ViewModel
import com.example.seefood.CAMERA_SCREEN
import com.example.seefood.CATALOG_MENU_SCREEN
import com.example.seefood.FAVORITES_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Абстракция модели представления домашнего экрана
 */
interface HomeScreenViewModelAbstract {
   /**
    * Функция вызываемая при нажатии на кнопку избранного
    *
    * @param[openScreen] шаблонная функция для смены экрана
    */
   fun onFavoritesPressed (openScreen: (String) -> Unit)

   /**
    * Функция вызываемая при нажатии на кнопку каталогов
    *
    * @param[openScreen] шаблонная функция для смены экрана
    */
   fun onCatalogPressed   (openScreen: (String) -> Unit)

   /**
    * Функция вызываемая при нажатии на кнопку камеры
    *
    * @param[openScreen] шаблонная функция для смены экрана
    */
   fun onCameraPressed    (openScreen: (String) -> Unit)
}

/**
 * Модель представления домашнего экрана
 */
@HiltViewModel
class HomeScreenViewModel
@Inject constructor() : ViewModel(), HomeScreenViewModelAbstract {
   override fun onFavoritesPressed(openScreen: (String) -> Unit) {
      openScreen(FAVORITES_SCREEN)
   }

   override fun onCatalogPressed(openScreen: (String) -> Unit) {
      openScreen(CATALOG_MENU_SCREEN)
   }

   override fun onCameraPressed(openScreen: (String) -> Unit) {
      openScreen(CAMERA_SCREEN)
   }
}