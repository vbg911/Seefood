package com.example.seefood.screens.camera

import android.widget.Toast
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.seefood.permissions
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.example.seefood.R
import com.example.seefood.SeeFoodAppState

/**
 * Экран камеры (UI представление)
 *
 * @param[viewModel] модель представления, предоставляется с помощью Hilt
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
   viewModel: CameraScreenViewModel = hiltViewModel(),
   appState : SeeFoodAppState
) {
   val requiredPermissions = permissions()
   val permissionState = rememberMultiplePermissionsState(permissions = requiredPermissions)

   if (!permissionState.allPermissionsGranted) { SideEffect { permissionState.launchMultiplePermissionRequest() } }

   val context        = LocalContext.current
   val lifecycleOwner = LocalLifecycleOwner.current
   val configuration  = LocalConfiguration.current
   val screeHeight    = configuration.screenHeightDp.dp
   val screenWidth    = configuration.screenWidthDp.dp

   var previewView: PreviewView

   val cameraPreviewHeight = screeHeight * 0.80f
   val cameraPreviewWidth  = screenWidth

   Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
   ) {
      if (permissionState.allPermissionsGranted){
         Box(
            modifier = Modifier
               .height(cameraPreviewHeight)
               .width(cameraPreviewWidth)) {
            AndroidView(
               factory = {
                  previewView = PreviewView(it)
                  viewModel.showCameraPreview(previewView, lifecycleOwner)
                  previewView
               },
               modifier = Modifier
                  .height(cameraPreviewHeight)
                  .width(cameraPreviewWidth)
            )
         }
      }

      Box(
         modifier = Modifier
            .fillMaxHeight(),
         contentAlignment = Alignment.Center
      ){
         IconButton(onClick = {
            if (permissionState.allPermissionsGranted){
               viewModel.captureAndSave(context, appState)
            }
            else{
               Toast.makeText(
                  context,
                  "Please accept permission in app settings",
                  Toast.LENGTH_LONG
               ).show()
            }
         }) {

            Icon(
               imageVector = ImageVector.vectorResource(id = R.drawable.round_camera_24),
               contentDescription = "",
               tint = Color.White
            )
         }
      }

   }
}