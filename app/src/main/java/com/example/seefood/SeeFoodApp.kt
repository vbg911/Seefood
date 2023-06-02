package com.example.seefood

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.seefood.screens.favorites.FavoritesScreen
import com.example.seefood.screens.camera.CameraScreen
import com.example.seefood.screens.catalog.CatalogScreen
import com.example.seefood.screens.catalogs_menu.CatalogsMenuScreen
import com.example.seefood.screens.classification_result.ClassificationResultScreen
import com.example.seefood.screens.dish.DishScreen
import com.example.seefood.screens.home.HomeScreen
import com.example.seefood.ui.theme.Background
import com.example.seefood.ui.theme.SeefoodTheme

/**
 * Основная Composable функция
 */
@Composable
fun SeeFoodApp(){
   SeefoodTheme {
      val appState = rememberAppState()
      Scaffold(
         backgroundColor = Background, // TODO: Добавить цвета в константы
         topBar = { SeeFoodTopBar() }
      ) { innerPadding ->
         NavHost(
            navController = appState.navController,
            startDestination = HOME_SCREEN,
            modifier = Modifier.padding(innerPadding)
         ){
            seeFoodGraph(appState)
         }
      }
   }
}

/**
 * Функция получения состояния приложения
 *
 * @param[navController] контроллер навигации
 * @param[context] контекст
 */
@Composable
fun rememberAppState(
   navController: NavHostController = rememberNavController(),
   context: Context = context(),
) =
   remember(navController, context) {
      SeeFoodAppState(navController, context)
   }

/**
 * Функция получения текущего контекста
 */
@Composable
@ReadOnlyComposable
fun context(): Context { return LocalContext.current }

/**
 * Функция получения необходимых для приложения разрешений в зависимости от версии ОС
 */
fun permissions(): List<String> {
   return if (Build.VERSION.SDK_INT <= 28) {
      listOf(
         Manifest.permission.CAMERA,
         Manifest.permission.WRITE_EXTERNAL_STORAGE,
         Manifest.permission.READ_EXTERNAL_STORAGE
      )
   }
   else {
      listOf(
         Manifest.permission.CAMERA,
         Manifest.permission.ACCESS_MEDIA_LOCATION,
         if (Build.VERSION.SDK_INT >= 33) Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
      )
   }
}

/**
 * Composable функция верхней панели приложения
 */
@Composable
fun SeeFoodTopBar(){
   TopAppBar(
      modifier = Modifier
         .shadow(
            elevation = 10.dp,
            shape = RoundedCornerShape(
               bottomStart = 5.dp,
               bottomEnd = 5.dp
            )
         )
         .clip(
            RoundedCornerShape(
               bottomStart = 5.dp,
               bottomEnd = 5.dp
            )
         ),
      backgroundColor = Color.Black, // TODO: Добавить цвета как константы
   ) {
      Text(
         modifier = Modifier.fillMaxWidth(),
         text = "SEEFOOD",
         style = TextStyle( // TODO: Сделать все стили текста как константы
            color = Color.White,
            fontWeight = FontWeight.W700,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
         )
      )
   }
}

/**
 * Функция построения навигационного графа
 *
 * @param[appState] состояние приложения
 */
fun NavGraphBuilder.seeFoodGraph(appState: SeeFoodAppState){
   composable(HOME_SCREEN) {
      HomeScreen(openScreen = { route -> appState.navigate(route) })
   }

   composable(CAMERA_SCREEN) {
      CameraScreen(appState = appState)
   }

   composable(CATALOG_MENU_SCREEN) {
      CatalogsMenuScreen(openScreen = { route -> appState.navigate(route) })
   }

   composable(
      "$CATALOG_SCREEN$CATALOG_SCREEN_ARGS",
      arguments = listOf(navArgument(name = "catalogName"){ type = NavType.StringType })
   ) {
      val catalogName = it.arguments?.getString("catalogName").toString()
      CatalogScreen(catalogName = catalogName, openScreen = { route -> appState.navigate(route) })
   }

   composable(
      "$RESULT_SCREEN$RESULT_SCREEN_ARGS",
      arguments = listOf(navArgument(name = "dishId"){ type = NavType.IntType })
   ) {
      val dishId = it.arguments?.getInt("dishId")!!
      ClassificationResultScreen(dishId = dishId, openScreen = { route -> appState.navigate(route) })
   }

   composable(
      "$DISH_SCREEN$DISH_SCREEN_ARGS",
      arguments = listOf(
         navArgument(name = "dishId"){ type = NavType.IntType },
         navArgument(name = "isDishFavorite"){ type = NavType.BoolType }
      )
   ) {
      val dishId = it.arguments?.getInt("dishId")!!
      val isDishFavorite = it.arguments?.getBoolean("isDishFavorite")!!
      DishScreen(dishId = dishId, isDishFavorite = isDishFavorite)
   }

   composable(FAVORITES_SCREEN) {
      FavoritesScreen(openScreen = { route -> appState.navigate(route) })
   }
}