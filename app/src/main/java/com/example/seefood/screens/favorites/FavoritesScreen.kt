package com.example.seefood.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.seefood.screens.favorites.composable.FavoriteDishCard

/**
 * Экран избранного (UI представление)
 *
 * @param[viewModel] модель представления, предоставляется с помощью Hilt
 * @param[openScreen] функция открытия экрана блюда, передается карточке
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FavoritesScreen(
   viewModel: FavoritesViewModel = hiltViewModel(),
   openScreen : (String) -> Unit
) {
   val dishesListState = viewModel.dishesListFlow.collectAsState(initial = listOf())

   FlowRow(
      modifier = Modifier.fillMaxSize(),
      horizontalArrangement = Arrangement.SpaceBetween
   ) {
      dishesListState.value.forEach { dish ->
         FavoriteDishCard(dish = dish, openScreen = openScreen, unfavorite = { viewModel.unfavoriteDish(dish) })
      }
   }
}