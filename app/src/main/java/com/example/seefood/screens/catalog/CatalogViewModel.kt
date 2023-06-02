package com.example.seefood.screens.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seefood.database.objects.Dish
import com.example.seefood.database.repos.DishRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Абстракция модели представления экрана каталога
 */
interface CatalogViewModelAbstract {
   /**
    * Функция получения списка блюд из соответствующего каталога
    *
    * @param[catalogName] имя каталога
    * @return[Flow] объект со списком объектов класса [Dish]
    */
   fun getCatalogsDishesListFlow(catalogName: String) : Flow<List<Dish>>

   /**
    * Функция удаления блюда из каталога
    *
    * @param[dish] объект класса [Dish], который нужно удалить из каталога
    */
   fun removeDishFromCatalog(dish: Dish)
//   fun addDishToCatalog(dish: Dish) TODO: Может потом доделать чтобы добавлять из избранного в каталоги
}

/**
 * Модель представления экрана каталога
 *
 * @constructor
 * @param[dishRepository] интерфейс для взаимодействия с таблицей блюд локальной БД
 */
@HiltViewModel
class CatalogViewModel
@Inject constructor(
   private val dishRepository: DishRepository
) : ViewModel(), CatalogViewModelAbstract {

   override fun getCatalogsDishesListFlow(catalogName: String): Flow<List<Dish>> {
      return dishRepository.getDishesByCatalogName(catalogName = catalogName)
   }

   override fun removeDishFromCatalog(dish: Dish) {
      viewModelScope.launch {
         if (!dish.isFavorite){
            viewModelScope.launch { dishRepository.deleteDishById(dish.id) }
         }
         else {
            viewModelScope.launch {
               dishRepository.upsertDish(
                  Dish(
                     name = dish.name,
                     recipe = dish.recipe,
                     imgLocalPath = dish.imgLocalPath,
                     catalog = "",
                     isFavorite = dish.isFavorite,

                     id = dish.id
                  )
               )
            }
         }
      }
   }
}