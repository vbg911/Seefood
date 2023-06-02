package com.example.seefood.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seefood.database.objects.Dish
import com.example.seefood.database.repos.DishRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Абстракция модели представления экрана избранного
 */
interface FavoritesViewModelAbstract  {
   /**
    * Переменная, содержащая [Flow] со списком всех объектов класса [Dish], включенных в избранное
    */
   val dishesListFlow: Flow<List<Dish>>

   /**
    * Функция удаления блюда из избранного
    *
    * @param[dish] объект класса [Dish], который нужно удалить из избранного
    */
   fun unfavoriteDish(dish: Dish)
}

/**
 * Модель представления экрана избранного
 *
 * @constructor
 * @param[dishRepository] интерфейс для взинтерфейс для взаимодействия с таблицей блюд локальной БД
 */
@HiltViewModel
class FavoritesViewModel
@Inject constructor(
   private val dishRepository: DishRepository
) : ViewModel(), FavoritesViewModelAbstract {

   override val dishesListFlow: Flow<List<Dish>>
      get() = dishRepository.getFavoriteDishes()

   override fun unfavoriteDish(dish: Dish) {
      if (dish.catalog == ""){
         viewModelScope.launch { dishRepository.deleteDishById(dish.id) }
      }
      else {
         viewModelScope.launch {
            dishRepository.upsertDish(
               Dish(
                  name = dish.name,
                  recipe = dish.recipe,
                  imgLocalPath = dish.imgLocalPath,
                  catalog = dish.catalog,
                  isFavorite = false,

                  id = dish.id
               )
            )
         }
      }
   }
}