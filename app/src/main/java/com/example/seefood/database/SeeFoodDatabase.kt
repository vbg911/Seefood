package com.example.seefood.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.seefood.database.dao.CatalogDao
import com.example.seefood.database.dao.DishDao
import com.example.seefood.database.objects.Catalog
import com.example.seefood.database.objects.Dish

/**
 * Класс локальной базы данных приложения
 */
@Database(
   entities = [Dish::class, Catalog::class],
   version  = 1
)
abstract class SeeFoodDatabase : RoomDatabase() {

   abstract val dishDao    : DishDao
   abstract val catalogDao : CatalogDao

   companion object {
      private var INSTANCE: SeeFoodDatabase? = null

      /**
       * Функция получения экземпляра БД
       */
      fun getInstance(context: Context) : SeeFoodDatabase {
         if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, SeeFoodDatabase::class.java, "dishdata.db")
               .fallbackToDestructiveMigration()
               .build()
         }
         return INSTANCE as SeeFoodDatabase
      }
   }
}