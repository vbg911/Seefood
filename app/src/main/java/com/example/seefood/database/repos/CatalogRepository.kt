package com.example.seefood.database.repos

import com.example.seefood.database.dao.CatalogDao
import com.example.seefood.database.objects.Catalog

class CatalogRepository(
   private val catalogDao: CatalogDao
) {
   suspend fun upsertCatalog(catalog: Catalog) = catalogDao.upsertCatalog(catalog = catalog)
   suspend fun deleteCatalogByName(name: String) = catalogDao.deleteCatalogByName(name = name)

   fun getAllCatalogs() = catalogDao.getAllCatalogs()
}