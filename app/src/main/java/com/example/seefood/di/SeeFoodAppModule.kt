package com.example.seefood.di

import android.app.Application
import com.example.seefood.data.network.ApiService
import com.example.seefood.data.network.PastebinService
import com.example.seefood.database.SeeFoodDatabase
import com.example.seefood.database.dao.CatalogDao
import com.example.seefood.database.dao.DishDao
import com.example.seefood.database.repos.CatalogRepository
import com.example.seefood.database.repos.DishRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Модуль внедрения зависимостей для всего приложения кроме камеры
 */
@Module
@InstallIn(SingletonComponent::class)
object SeeFoodAppModule {

   @Provides
   @Singleton
   fun provideNgrokRetrofit() : PastebinService = Retrofit.Builder()
      .baseUrl("https://pastebin.com")
      .addConverterFactory(ScalarsConverterFactory.create())
      .build()
      .create(PastebinService::class.java)

   /**
    * Функция предоставляющая базовую ссылку для обращения через Retrofit
    */
   @Provides
   fun baseUrl(pastebinService: PastebinService) =  runBlocking {
      pastebinService.getNgrokLink().body()!!
   }

   @Provides
   @Singleton
   fun provideOkHttpClient() : OkHttpClient = OkHttpClient.Builder()
      .readTimeout(30, TimeUnit.SECONDS)
      .connectTimeout(30, TimeUnit.SECONDS)
      .build()

   /**
    * Функция - провайдер Retrofit интерфейса для обращения к API SeeFood
    *
    * @param[baseUrl] базовая ссылка
    */
    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient) : ApiService = Retrofit.Builder()
       .baseUrl(baseUrl)
       .addConverterFactory(GsonConverterFactory.create())
       .client(okHttpClient)
       .build()
       .create(ApiService::class.java)


   /**
    * Функция провайдер экземпляра локальной БД
    *
    * @return Экземпляр [SeeFoodDatabase]
    */
    @Provides
    @Singleton
    fun provideSeeFoodDatabase(app: Application) : SeeFoodDatabase {
        return SeeFoodDatabase.getInstance(app.applicationContext)
    }

   /**
    * Функция провайдер репозитория для доступа к таблице блюд
    *
    * @return[DishRepository]
    */
    @Provides
    @Singleton
    fun provideDishRepository(
        dishDao: DishDao
    ) : DishRepository {
        return DishRepository(dishDao = dishDao)
    }

   /**
    * Функция провайдер интерфейса для доступа к таблице блюд
    *
    * @return[DishDao]
    */
    @Provides
    @Singleton
    fun provideDishDao(seeFoodDatabase: SeeFoodDatabase) : DishDao {
        return seeFoodDatabase.dishDao
    }

   /**
    * Функция провайдер репозитория для доступа к таблице каталогов
    *
    * @return[CatalogRepository]
    */
   @Provides
   @Singleton
   fun provideCategoryRepository(
      catalogDao: CatalogDao
   ) : CatalogRepository {
      return CatalogRepository(catalogDao = catalogDao)
   }

   /**
    * Функция провайдер интерфейса для доступа к таблице каталогов
    *
    * @return[CatalogDao]
    */
   @Provides
   @Singleton
   fun provideCategoryDao(seeFoodDatabase: SeeFoodDatabase) : CatalogDao {
      return seeFoodDatabase.catalogDao
   }
}