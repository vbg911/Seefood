package com.example.seefood.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.seefood.R.drawable as AppImages
import com.example.seefood.common.composable.MenuButton

/**
 * Домашний экран (UI представление)
 *
 * @param[viewModel] модель представления, предоставляется с помощью Hilt
 * @param[openScreen] шаблонная функция для смены экрана, вызывается при нажатии кнопок меню
 */
@Composable
fun HomeScreen(
  openScreen: (String) -> Unit,
  viewModel: HomeScreenViewModel = hiltViewModel()
){
   Column(modifier = Modifier.fillMaxSize()) {
      Spacer(modifier = Modifier.weight(0.3f))
      MenuButton(
         modifier = Modifier
            .align(Alignment.End)
            .fillMaxWidth(0.55f),
         buttonSkin = AppImages.favorites,
         contentDescription = "Favorites",
         onClick = { viewModel.onFavoritesPressed(openScreen) }
      )
      Spacer(modifier = Modifier.weight(0.5f))
      MenuButton(
         modifier = Modifier
            .align(Alignment.Start)
            .fillMaxWidth(0.55f),
         buttonSkin = AppImages.catalogs,
         contentDescription = "Catalogs",
         onClick = { viewModel.onCatalogPressed(openScreen) }
      )
      Spacer(modifier = Modifier.weight(0.9f))
      MenuButton(
         modifier = Modifier.fillMaxWidth(),
         buttonSkin = AppImages.camera,
         contentDescription = "Camera",
         onClick = { viewModel.onCameraPressed(openScreen) }
      )
   }
}