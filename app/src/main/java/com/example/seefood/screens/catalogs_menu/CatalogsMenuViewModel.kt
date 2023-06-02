package com.example.seefood.screens.catalogs_menu

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
 * Абстракция модели представления экрана мню каталогов
 */
interface CatalogsMenuViewModelAbstract {
   /**
    * Переменная, содержащая [Flow] со списком всех объектов класса [Catalog]
    */
   val catalogsListFlow: Flow<List<Catalog>>

   /**
    * Функция добавления каталога в локальную БД
    *
    * @param[catalog] объект класса [Catalog], который нужно занести в локальную БД
    */
   fun addCatalog(catalog: Catalog)

   /**
    * Функция удаления каталога из локальной БД
    *
    * @param[catalog] объект класса [Catalog], который нужно удалить из локальной БД
    */
   fun deleteCatalog(catalog: Catalog)
}

/**
 * Модель представления экрана меню каталогов
 *
 * @constructor
 * @param[catalogRepository] интерфейс для взаимодействия с таблицей каталогов локальной БД
 * @param[dishRepository] интерфейс для взаимодействия с таблицей блюд локальной БД
 */
@HiltViewModel
class CatalogsMenuViewModel
@Inject constructor(
   private val catalogRepository: CatalogRepository,
   private val dishRepository: DishRepository
) : ViewModel(), CatalogsMenuViewModelAbstract {

   override val catalogsListFlow: Flow<List<Catalog>>
      get() = catalogRepository.getAllCatalogs()

   override fun addCatalog(catalog: Catalog) {
      viewModelScope.launch {
         catalogRepository.upsertCatalog(catalog = catalog)
      }
   }
   override fun deleteCatalog(catalog: Catalog) {
      viewModelScope.launch {
         val catalogsDishesList = dishRepository.getDishesByCatalogNameList(catalogName = catalog.name)

         for (dish in catalogsDishesList) {
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
         println("in2")
         catalogRepository.deleteCatalogByName(name = catalog.name)
      }
   }
}