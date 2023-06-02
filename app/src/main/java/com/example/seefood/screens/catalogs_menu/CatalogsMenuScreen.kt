package com.example.seefood.screens.catalogs_menu

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.seefood.R
import com.example.seefood.database.objects.Catalog
import com.example.seefood.screens.catalogs_menu.composable.CatalogCard

/**
 * Экран меню каталогов (UI представление)
 *
 * @param[viewModel] модель представления, предоставляется с помощью Hilt
 * @param[openScreen] функция для передачи карточке каталога для открытия экрана соответствующего каталога
 */
@Composable
fun CatalogsMenuScreen(
   viewModel  : CatalogsMenuViewModel = hiltViewModel(),
   openScreen : (String) -> Unit
){
   val catalogsListState = viewModel.catalogsListFlow.collectAsState(initial = listOf())

   var colSize by remember { mutableStateOf(Size.Zero) }

   // TODO: Добавить в константы все паддинги и цвета, может создать ext Modifier'ы
   Column(
      modifier = Modifier
         .fillMaxSize()
         .onGloballyPositioned { coordinates ->
            colSize = coordinates.size.toSize()
         }
   ) {
      val showImagePicker = remember { mutableStateOf(false) }
      
      FloatingActionButton(
         modifier = Modifier
            .size(with(LocalDensity.current) { colSize.width.toDp() / 8f })
            .align(Alignment.End),
         onClick = { showImagePicker.value = true },
         backgroundColor = Color.Transparent
      ) {
         Icon(
            modifier = Modifier
               .fillMaxSize(),
            imageVector = Icons.Rounded.AddCircle,
            contentDescription = "Добавить каталог",
            tint = Color.White
         )
      }
      
      if (showImagePicker.value) {
         AlertDialog(
            modifier = Modifier
               .fillMaxWidth()
               .border(width = 3.dp, color = Color.Black, shape = RoundedCornerShape(20.dp))
               .padding(2.dp)
               .background(color = Color.White, shape = RoundedCornerShape(20.dp)),
            contentColor = Color.Black,
            backgroundColor = Color.Transparent,
            onDismissRequest = { showImagePicker.value = false },
            buttons = {
               Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.End
               ) {
                  Icon(
                     modifier = Modifier
                        .padding(top = 5.dp, end = 5.dp)
                        .clip(CircleShape)
                        .clickable {
                           showImagePicker.value = false
                        },
                     imageVector = Icons.Rounded.Close,
                     contentDescription = "Отмена",
                     tint = Color.Black
                  )
               }
               Column(
                  modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
               ){
                  var imageUri by remember { mutableStateOf<Uri?>(null) }
                  val launcher = rememberLauncherForActivityResult(
                     contract = ActivityResultContracts.GetContent()
                  ) { uri: Uri? -> imageUri = uri
                  }

                  Row(
                     modifier = Modifier.fillMaxWidth(),
                     horizontalArrangement = Arrangement.SpaceEvenly
                  ){
                     Column {
                        Image(
                           modifier = Modifier
                              .height(80.dp)
                              .width(80.dp)
                              .clip(RoundedCornerShape(10.dp))
                              .clickable {
                                 launcher.launch("image/jpeg")
                              },
                           painter = if (imageUri == null)
                              rememberAsyncImagePainter(R.drawable.imagefiller) else
                              rememberAsyncImagePainter(imageUri),
                           contentDescription = null,
                           contentScale = ContentScale.Crop
                        )
                     }
                     
                     Column {
                        var name by remember{ mutableStateOf("") }
                        Box(
                           modifier = Modifier.fillMaxWidth(0.85f)
                        ) {
                           TextField(
                              placeholder = {
                                 Text("Название")
                              },
                              singleLine = true,
                              value = name,
                              onValueChange = { name = it },
                              shape = RoundedCornerShape(10.dp),
                              colors = TextFieldDefaults.textFieldColors(
                                 backgroundColor         = Color.Transparent,
                                 disabledIndicatorColor  = Color.Transparent,
                                 errorIndicatorColor     = Color.Transparent,
                                 focusedIndicatorColor   = Color.Transparent,
                                 unfocusedIndicatorColor = Color.Transparent
                              )
                           )

                           Divider(
                              modifier = Modifier
                                 .fillMaxWidth(0.8f)
                                 .offset(x = 15.dp, y = 45.dp),
                              color = Color.Black,
                              thickness = 2.dp,
                           )
                        }

                        val isAddEnabled = (imageUri != null && name != "")
                        IconButton(
                           enabled = isAddEnabled,
                           modifier = Modifier
                              .clip(CircleShape)
                              .background(color = if (isAddEnabled) Color.Black else Color.LightGray)
                              .align(Alignment.End),
                           onClick = {
                              viewModel.addCatalog(
                                 Catalog(
                                    name = name,
                                    thumbnailLocalPath = imageUri.toString()
                                 )
                              )
                              showImagePicker.value = false
                           }
                        ) {
                           Icon(
                              imageVector = Icons.Rounded.Add,
                              contentDescription = "Добавить каталог",
                              tint = Color.White
                           )
                        }
                     }
                  }
               }
            }
         )
      }
   }

   LazyColumn(
      modifier = Modifier
         .fillMaxSize()
         .padding(top = with(LocalDensity.current) { (colSize.width.toDp() / 8f) / 1.5f }),
      contentPadding = PaddingValues(start = 30.dp, end = with (LocalDensity.current) { colSize.width.toDp() / 8f } )
   ) {
      items(catalogsListState.value.size) { catalogIdx ->
         CatalogCard(
            catalog = catalogsListState.value[catalogIdx],
            openScreen = openScreen,
            deleteCatalog = { viewModel.deleteCatalog(catalogsListState.value[catalogIdx]) }
         )
      }
   }
}