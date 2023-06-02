package com.example.seefood.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.seefood.database.objects.Dish
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс для доступа к таблице блюд в локальной БД
 */
@Dao
interface DishDao {
   /**
    * Вставка/обновление записи в таблице блюд
    *
    * @param[dish] объект класса [Dish] который нужно добавить в БД
    */
   @Upsert
   suspend fun upsertDish(dish: Dish) : Long?

   /**
    * Удаление записи в таблице блюд по идентификаторов
    *
    * @param[id] идентификатор блюда которое нужно удалить из таблицы
    */
   @Query("DELETE FROM dishes WHERE id = :id")
   suspend fun deleteDishById(id: Int)

   /**
    * Получение списка блюд по имени каталога в котором они содержаться
    *
    * @param[catalogName] имя каталога блюда, содержащиеся в котором нужно получить
    * @return[List] объектов класса [Dish]
    */
   @Query("SELECT * FROM dishes WHERE catalog = :catalogName")
   suspend fun getDishesByCatalogNameList(catalogName: String) : List<Dish>


   /**
    * Получение записи из таблицы блюд по идентификатору
    *
    * @param[id] идентификатор блюда которое нужно получить из таблицы
    * @return[Flow] объект класса [Dish]
    */
   @Query("SELECT * FROM dishes WHERE id = :id")
   fun getDishById(id: Int) : Flow<Dish?>

   /**
    * Получение блюд, включенных в список избранного
    *
    * @return[Flow] объект со списком объектов класса [Dish]
    */
   @Query("SELECT * FROM dishes WHERE is_favorite = 1")
   fun getFavoriteDishes() : Flow<List<Dish>>

   /**
    * Получение блюд, содержащихся в каталоге с заданным именем
    *
    * @param[catalogName] имя каталога, блюда из которого нужно получить
    * @return[Flow] объект со списком объектов класса [Dish]
    */
   @Query("SELECT * FROM dishes WHERE catalog = :catalogName")
   fun getDishesByCatalogName(catalogName: String) : Flow<List<Dish>>
}