package com.example.seefood.screens.favorites.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.seefood.DISH_SCREEN
import com.example.seefood.R
import com.example.seefood.common.composable.AreYouSureDialog
import com.example.seefood.database.objects.Dish
import java.io.File

/**
 * Кароточка отображения блюда для экрана избранного
 *
 * @param[dish] объект класса [Dish], карточку которого требуется отобразить
 * @param[openScreen] функция вызываемая при нажатиии на карточку
 * @param[unfavorite] функция вызываемая при удалении блюда из избранного
 */
@Composable
fun FavoriteDishCard(
   dish: Dish,
   openScreen : (String) -> Unit,
   unfavorite : () -> Unit
) {
   val universal = 100.dp

   Column(
      modifier = Modifier
         .padding(15.dp)
         .width(universal),
      horizontalAlignment = Alignment.CenterHorizontally
   ) {
      Box(modifier = Modifier){
         var isDialogDisplayed by remember { mutableStateOf(false) }

         AsyncImage(
            modifier = Modifier
               .clip(RoundedCornerShape(10.dp))
               .width(universal)
               .height(universal)
               .clickable {
                  openScreen("$DISH_SCREEN/${dish.id}/${dish.isFavorite}")
               },
            model = File(dish.imgLocalPath),
            contentDescription = dish.name,
            contentScale = ContentScale.Crop
         )
         AsyncImage(
            modifier = Modifier
               .offset(x = universal / 6f, y = universal / 8f)
               .clip(CircleShape)
               .background(Color.Black)
               .padding(10.dp)
               .clickable { isDialogDisplayed = true }
               .height(universal / 4f)
               .width(universal / 4f)
               .align(Alignment.BottomEnd),
            model = R.drawable.heart,
            contentDescription = "Убрать из избранного"
         )

         if (isDialogDisplayed) {
            AreYouSureDialog(
               onDismiss = { isDialogDisplayed = false },
               onConfirm = { unfavorite(); isDialogDisplayed = false },
               titleText = "Убрать из избранного?")
         }
      }

      Text(
         modifier = Modifier.padding(top = 10.dp),
         text = dish.name,
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