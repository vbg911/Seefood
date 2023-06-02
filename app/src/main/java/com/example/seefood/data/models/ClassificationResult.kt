package com.example.seefood.data.models


/**
 * Объект получаемый после запроса к классификатору
 */
data class ClassificationResult (
   val name_dish   : String?,
   val recipe_dish : String?
)