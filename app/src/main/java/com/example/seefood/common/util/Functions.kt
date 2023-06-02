package com.example.seefood.common.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Функция получения текущей даты и времени
 */
fun getCurrentDateAsString(): String {
   val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.ROOT)
   return sdf.format(Date())
}