package com.example.seefood.database.objects

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.seefood.common.util.getCurrentDateAsString

@Entity(tableName = "catalogs")
data class Catalog (
   @PrimaryKey
   val name               : String,
   val creationDate       : String = getCurrentDateAsString(),
   val thumbnailLocalPath : String
)
