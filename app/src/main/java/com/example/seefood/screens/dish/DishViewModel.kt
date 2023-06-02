package com.example.seefood.screens.dish

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seefood.database.objects.Catalog
import com.example.seefood.database.objects.Dish
import com.example.seefood.database.repos.CatalogRepository
import com.example.seefood.database.repos.DishRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Абстракция модели представления экрана блюда
 */
interface DishViewModelAbstract {
   /**
    * Функция получения блюда по идентификатору
    *
    * @param[dishId] идентификатор блюда
    * @return[Flow] с объектом класса [Dish]
    */
   fun getRelatedDish(dishId : Int) : Flow<Dish?>
   val catalogsListFlow : Flow<List<Catalog>>

   /**
    * Функция удаления блюда из каталога
    *
    * @param[dish] объект класса [Dish], который нужно удалить из каталога
    */
   fun removeDishFromCatalog(dish: Dish)
   fun changeDishFavoritesState(dish: Dish, favoriteState: Boolean)
   fun changeDishCatalog(dish: Dish, catalogName: String)
}

/**
 * Модель представления экрана блюда
 *
 * @constructor
 * @param[dishRepository] интерфейс для взаимодействия с таблицей блюд локальной БД
 */
@HiltViewModel
class DishViewModel
@Inject constructor(
   private val dishRepository: DishRepository,
   private val catalogRepository: CatalogRepository
) : ViewModel(), DishViewModelAbstract {
   override fun getRelatedDish(dishId: Int): Flow<Dish?> {
      return dishRepository.getDishById(dishId)
   }

   override val catalogsListFlow: Flow<List<Catalog>>
      get() = catalogRepository.getAllCatalogs()

   override fun changeDishFavoritesState(dish: Dish, favoriteState : Boolean) {
      viewModelScope.launch {
         dishRepository.upsertDish(
            Dish(
               name = dish.name,
               recipe = dish.recipe,
               imgLocalPath = dish.imgLocalPath,
               catalog = dish.catalog,
               isFavorite = favoriteState,
               id = dish.id,
            )
         )
      }
   }

   override fun removeDishFromCatalog(dish: Dish) {
      viewModelScope.launch {
         if (dish.isFavorite) {
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
         else {
            dishRepository.deleteDishById(dish.id)
         }
      }
   }

   override fun changeDishCatalog(dish: Dish, catalogName: String) {
      viewModelScope.launch {
         dishRepository.upsertDish(
            Dish (
               name = dish.name,
               recipe = dish.recipe,
               imgLocalPath = dish.imgLocalPath,
               catalog = catalogName,
               isFavorite = dish.isFavorite,
               id = dish.id
            )
         )
      }
   }
}