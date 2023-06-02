package com.example.seefood.screens.camera

import android.content.Context
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seefood.SeeFoodAppState
import com.example.seefood.data.camera.CameraService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Модель представления экрана камеры
 *
 * @constructor
 * @param[repo] интерфейс для взаимодействия с камерой, передается Hilt
 */
@HiltViewModel
class CameraScreenViewModel @Inject constructor(
   private val repo: CameraService
) : ViewModel() {
   /**
    * Функция отображения превью камеры
    *
    * @param[previewView] представление превью, предоставляется Hilt
    * @param[lifecycleOwner] владелец жизненного цикла превью, предоаставляется Hilt
    */
   fun showCameraPreview(
      previewView: PreviewView,
      lifecycleOwner: LifecycleOwner
   ){
      viewModelScope.launch {
         repo.showCameraPreview(
            previewView,
            lifecycleOwner
         )
      }
   }

   /**
    * Функция получения снимка и сохранения его на устройстве
    *
    * @param[context] контекст
    */
   fun captureAndSave(context: Context, appState: SeeFoodAppState){
      viewModelScope.launch {
         repo.captureAndSaveImage(context, appState)
      }
   }
}