package com.example.seefood.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.seefood.database.objects.Catalog
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс для доступа к таблице каталогов в локальной БД
 */
@Dao
interface CatalogDao {
   /**
    * Вставка/обновление записи в таблице каталогов
    *
    * @param[catalog] объект класса [Catalog] который нужно добавить в БД
    */
   @Upsert
   suspend fun upsertCatalog(catalog: Catalog)

   /**
    * Удаление записи в таблице каталогов по имени
    *
    * @param[name] имя каталога который нужно удалить
    */
   @Query("DELETE FROM catalogs WHERE name = :name")
   suspend fun deleteCatalogByName(name: String)


   /**
    * Получение всех каталогов из таблицы
    *
    * @return[Flow] объект со списком объектов класса [Catalog]
    */
   @Query("SELECT * FROM catalogs")
   fun getAllCatalogs() : Flow<List<Catalog>>
}