package com.example.seefood.data.network

import com.example.seefood.data.models.ClassificationResult
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Интерфейс для запросов к API
 */
interface ApiService {

   /**
    * POST запрос к API для классификации изображения
    */
   @Multipart
   @POST(".")
   suspend fun sendImageToClassifier(@Part photo : MultipartBody.Part) : Response<ClassificationResult>
}