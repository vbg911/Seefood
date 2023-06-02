package com.example.seefood.data.network

import retrofit2.Response
import retrofit2.http.GET

interface PastebinService {
   @GET("/raw/z5R6aeiq")
   suspend fun getNgrokLink() : Response<String>
}