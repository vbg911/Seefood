package com.example.seefood.screens.classification_result

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.seefood.HOME_SCREEN
import com.example.seefood.R
import com.example.seefood.database.objects.Dish
import com.example.seefood.screens.classification_result.composable.CatalogDialogCard
import com.example.seefood.ui.theme.Accent
import com.example.seefood.ui.theme.Background
import java.io.File
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClassificationResultScreen(
   viewModel: ClassificationResultViewModel = hiltViewModel(),
   dishId   : Int,
   openScreen : (String) -> Unit
){
   val relatedDish = viewModel.getRelatedDish(dishId = dishId).collectAsState(initial = Dish())

   val max = LocalConfiguration.current.screenHeightDp.dp / 2
   val min = 0.dp

   val (minPx, maxPx) = with(LocalDensity.current) { min.toPx() to max.toPx() }
   val offset = remember { mutableStateOf(maxPx) }

   var rowSize by remember { mutableStateOf(Size.Zero) }

   var imageSize by remember { mutableStateOf(Size.Zero) }

   val universal = 100.dp

   var isCatalogsDialogShowed by remember { mutableStateOf(false) }

   Box() {
      AsyncImage(
         modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates -> imageSize = coordinates.size.toSize() }
            .height(max),
         model = File(relatedDish.value?.imgLocalPath!!),
         contentDescription = relatedDish.value?.name,
         contentScale = ContentScale.Crop
      )

      AsyncImage(
         modifier = Modifier
            .align(Alignment.BottomEnd)
            .offset(x = -universal / 12f, y = -universal + universal / 4f)
            .clip(CircleShape)
            .background(Color.Black)
            .padding(10.dp)
            .clickable { isCatalogsDialogShowed = true }
            .height(universal / 3f)
            .width(universal / 3f),
         model = R.drawable.add_to_catalog,
         contentDescription = "Каталоги"
      )

      var isFavorite by remember{ mutableStateOf(false) }
      AsyncImage(
         modifier = Modifier
            .align(Alignment.BottomEnd)
            .offset(x = -universal / 12f, y = -universal / 12f)
            .clip(CircleShape)
            .background(Color.Black)
            .padding(10.dp)
            .clickable {
               isFavorite = !isFavorite
               viewModel.changeDishFavoritesState(dish = relatedDish.value!!, isFavorite)
            }
            .height(universal / 3f)
            .width(universal / 3f),
         model = if (isFavorite) R.drawable.heart else R.drawable.empty_heart,
         contentDescription = "Избранное"
      )
   }

   if (isCatalogsDialogShowed) {
      AlertDialog(
         properties = DialogProperties(usePlatformDefaultWidth = false),
         onDismissRequest = { isCatalogsDialogShowed = false },
         backgroundColor = Background,
         contentColor = Color.White,
         title = {
            Text(
               modifier = Modifier.fillMaxWidth(),
               text = "Выберите каталог",
               textAlign = TextAlign.Center
            )
         },
         buttons = {
            val catalogsListState = viewModel.catalogsListFlow.collectAsState(initial = listOf())

            FlowRow(
               modifier = Modifier.fillMaxSize(),
               horizontalArrangement = Arrangement.SpaceBetween
            ) {
               catalogsListState.value.forEach { catalog ->
                  CatalogDialogCard(
                     catalog = catalog,
                     action = {
                        viewModel.changeDishCatalog(
                           dish = relatedDish.value!!,
                           catalogName = catalog.name)
                        isCatalogsDialogShowed = false
                     }
                  )
               }
            }
         }
      )
   }

   Box(
      modifier = Modifier
         .fillMaxWidth()
         .fillMaxHeight()
         .offset { IntOffset(0, offset.value.roundToInt()) }
         .background(color = Color.Black)
   ) {
      Column() {
         Row(
            modifier = Modifier
               .fillMaxWidth()
               .height(20.dp)
               .onGloballyPositioned { coordinates ->
                  rowSize = coordinates.size.toSize()
               }
               .draggable(
                  orientation = Orientation.Vertical,
                  state = rememberDraggableState { delta ->
                     val updValue = offset.value + delta
                     offset.value = updValue.coerceIn(minPx, maxPx)
                  }
               ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top // Alignment.CenterVertically
         ) {
            Box(
               modifier = Modifier
                  .fillMaxWidth() // width(with(LocalDensity.current) { rowSize.width.toDp() / 4 })
                  .height(with(LocalDensity.current) { rowSize.height.toDp() / 4.5f })
                  .background(color = Accent, /*shape = RoundedCornerShape(with(LocalDensity.current) { rowSize.height.toDp() / 10 })*/)
            )
         }

         Column(
            modifier = Modifier.padding(horizontal = 20.dp)
         ){
            relatedDish.value?.let {
               Text(
                  text = it.name,
                  style = TextStyle(color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.W700)
               )
            }

            LazyColumn {
               item {
                  relatedDish.value?.let {
                     Text(
                        text = it.recipe,
                        style = TextStyle(color = Color.White, fontSize = 18.sp)
                     )
                  }
               }
            }
         }
      }
   }

   BackHandler(true) {
      if ((!relatedDish.value!!.isFavorite) && (relatedDish.value!!.catalog == "")){
         viewModel.removeDish(relatedDish.value!!)
      }
      openScreen(HOME_SCREEN)
   }
}