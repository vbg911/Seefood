package com.example.seefood.screens.catalogs_menu.composable

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import coil.compose.AsyncImage
import com.example.seefood.CATALOG_SCREEN
import com.example.seefood.common.util.URIPathHelper
import com.example.seefood.database.objects.Catalog
import java.io.File

/**
 * Карточка отображения каталога для экрана меню каталогов
 *
 * @param[catalog] объект класса [Catalog], карточку которого требуется отобразить
 * @param[openScreen] функция вызываемая при нажатии на карточку
 * @param[deleteCatalog] функция для удаления каталога из локальной БД
 */
@Composable
fun CatalogCard(
   catalog    : Catalog,
   openScreen : (String) -> Unit,
   deleteCatalog : () -> Unit
) {
   Row(
      modifier = Modifier
         .fillMaxWidth()
         .background(color = Color.Transparent)
         .clickable { openScreen("$CATALOG_SCREEN/${catalog.name}") }
         .padding(bottom = 15.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
   ) {
      // TODO: Все паддинги, размеры, стили добавить в константы
      Row(
         horizontalArrangement = Arrangement.spacedBy(30.dp)
      ) {
         var imageSize by remember { mutableStateOf(Size.Zero) }
         val helper = URIPathHelper()
         val imageFile = File(helper.getPath(LocalContext.current, Uri.parse(catalog.thumbnailLocalPath)).toString())

         AsyncImage(
            modifier = Modifier
               .fillMaxWidth(0.21f)
               .clip(RoundedCornerShape(10.dp))
               .onGloballyPositioned { coordinates -> imageSize = coordinates.size.toSize() }
               .height(with(LocalDensity.current) { imageSize.width.toDp() }),
            model = imageFile,
            contentDescription = "Каталог ${catalog.name}",
            contentScale = ContentScale.Crop
         )

         Column {
            Text(
               modifier = Modifier.fillMaxWidth(0.8f),
               text = catalog.name,
               style = TextStyle(
                  fontWeight = FontWeight.W500,
                  fontSize = 22.sp,
                  color = Color.White
               ),
               maxLines = 1,
               overflow = TextOverflow.Ellipsis
            )
            Text(
               text = catalog.creationDate,
               style = TextStyle(
                  fontWeight = FontWeight.W500,
                  fontSize = 16.sp,
                  color = Color.White
               ),
               maxLines = 1,
               overflow = TextOverflow.Ellipsis
            )
         }
      }

      Box(
         modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(align = Alignment.TopEnd)
      ){
         var dropdownExpanded by remember { mutableStateOf(false) }

         IconButton(onClick = { dropdownExpanded = !dropdownExpanded }) {
            Icon(
               imageVector = Icons.Rounded.MoreVert,
               contentDescription = "Опции каталога",
               tint = Color.White
            )
         }

         DropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false }
         ) {
            val context = LocalContext.current
            DropdownMenuItem(
               onClick = {
                  dropdownExpanded = false
                  deleteCatalog()
                  Toast.makeText(context, "Удален каталог ${catalog.name}", Toast.LENGTH_SHORT).show()
               }){
               Text(text = "Удалить")
            }
         }
      }
   }
}