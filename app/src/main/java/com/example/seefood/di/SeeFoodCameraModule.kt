package com.example.seefood.di

import android.app.Application
import androidx.camera.core.*
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.ImageCapture.FLASH_MODE_AUTO
import androidx.camera.lifecycle.ProcessCameraProvider
import com.example.seefood.data.camera.CameraService
import com.example.seefood.data.camera.CameraServiceImpl
import com.example.seefood.data.network.ApiService
import com.example.seefood.database.repos.DishRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Модуль внедрения зависимостей для камеры
 */
@Module
@InstallIn(SingletonComponent::class)
object SeeFoodCameraModule {

   /**
    * Функция провайдер для объекта [CameraSelector] (задней камеры смартфона)
    *
    * @return[CameraSelector]
    */
   @Provides
   @Singleton
   fun provideCameraSelector(): CameraSelector {
      return CameraSelector.Builder()
         .requireLensFacing(CameraSelector.LENS_FACING_BACK)
         .build()
   }

   /**
    * Функция провайдер для объекта [ProcessCameraProvider]
    *
    * @return[ProcessCameraProvider]
    */
   @Provides
   @Singleton
   fun provideCameraProvider(application: Application)
         : ProcessCameraProvider {
      return ProcessCameraProvider.getInstance(application).get()

   }

   /**
    * Функция провайдер предпрасмотра для камеры
    *
    * @return[Preview]
    */
   @Provides
   @Singleton
   fun provideCameraPreview(): Preview {
      return Preview.Builder().build()
   }

   /**
    * Функция провайдер для объекта [ImageCapture] (use case для фотографирования)
    *
    * @return[ImageCapture]
    */
   @Provides
   @Singleton
   fun provideImageCapture(): ImageCapture {
      return ImageCapture.Builder()
         .setFlashMode(FLASH_MODE_AUTO)
         .build()
   }

   /**
    * Функция провайдер для объекта [ImageAnalysis]
    *
    * @return[ImageAnalysis]
    */
   @Provides
   @Singleton
   fun provideImageAnalysis(): ImageAnalysis {
      return ImageAnalysis.Builder()
         .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
         .build()
   }

   /**
    * Функция провайдер для сервиса камеры [CameraService]
    *
    * @param[cameraProvider]
    * @param[selector]
    * @param[imageCapture]
    * @param[preview]
    *
    * @return[CameraServiceImpl]
    */
   @Provides
   @Singleton
   fun provideCustomCameraRepo(
      dishRepository: DishRepository,
      cameraProvider: ProcessCameraProvider,
      selector: CameraSelector,
      imageCapture: ImageCapture,
      preview: Preview,
      apiService: ApiService
   ): CameraService {
      return CameraServiceImpl (
         dishRepository,
         apiService,
         cameraProvider,
         selector,
         preview,
         imageCapture
      )
   }
}