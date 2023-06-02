package com.example.seefood.screens.classification_result.composable

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.seefood.common.util.URIPathHelper
import com.example.seefood.database.objects.Catalog
import java.io.File

@Composable
fun CatalogDialogCard(
   catalog: Catalog,
   action: () -> Unit
){
   val universal = 100.dp

   val helper = URIPathHelper()
   val imageFile = File(helper.getPath(LocalContext.current, Uri.parse(catalog.thumbnailLocalPath)).toString())

   Column(
      modifier = Modifier
         .padding(15.dp)
         .width(universal),
      horizontalAlignment = Alignment.CenterHorizontally
   ) {
      Box(modifier = Modifier){

         AsyncImage(
            modifier = Modifier
               .clip(RoundedCornerShape(10.dp))
               .width(universal)
               .height(universal)
               .clickable {
                  action()
               },
            model = imageFile,
            contentDescription = catalog.name,
            contentScale = ContentScale.Crop
         )
      }

      Text(
         modifier = Modifier.padding(top = 10.dp),
         text = catalog.name,
         style = TextStyle(
            color = Color.White,
            fontWeight = FontWeight.W500,
            fontSize = 16.sp
         ),
         maxLines = 1,
         overflow = TextOverflow.Ellipsis
      )
   }
}