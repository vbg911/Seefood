package com.example.seefood.common.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seefood.ui.theme.Background

/**
 * Диалог уверенности в действии пользователя
 *
 * @param[onConfirm] действие при подтверждении
 * @param[onDismiss] действие при отмене
 * @param[titleText] текст заголовка
 */
@Composable
fun AreYouSureDialog(
   onDismiss: () -> Unit,
   onConfirm: () -> Unit,
   titleText: String
) {
   AlertDialog(
      modifier = Modifier
         .clip(RoundedCornerShape(20.dp))
         .border(width = 3.dp, color = Color.Black, shape = RoundedCornerShape(20.dp))
         .padding(2.dp),
      onDismissRequest = onDismiss,
      title = {
         Text(
            text = titleText,
            style = TextStyle(fontWeight = FontWeight.W500, fontSize = 20.sp) // TODO: Добавить стиль в константы
         )
      },
      confirmButton = {
         Button(
            colors = ButtonDefaults.buttonColors(
               backgroundColor = Background,
               contentColor = Color.White
            ),
            onClick = onConfirm
         ) {
            Text(text = "Да")
         }
      },
      dismissButton = {
         Button(
            colors = ButtonDefaults.buttonColors(
               backgroundColor = Background,
               contentColor = Color.White
            ),
            onClick = onDismiss
         ) {
            Text(text = "Нет")
         }
      }
   )
}