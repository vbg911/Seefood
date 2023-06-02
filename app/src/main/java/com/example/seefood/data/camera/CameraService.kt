package com.example.seefood.data.camera

import android.content.Context
import android.net.Uri
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.example.seefood.SeeFoodAppState

/**
 * Интерфейс для доступа к функциям камеры
 */
interface CameraService {
   /**
    * Функция фотографирования и сохранения картинки на устройстве
    *
    * @param[context] контекст
    */
   suspend fun captureAndSaveImage(context: Context, appState: SeeFoodAppState)

   /**
    * Функция отображения окна предпросмотра камеры
    *
    * @param[previewView]
    * @param[lifecycleOwner]
    */
   suspend fun showCameraPreview(previewView: PreviewView, lifecycleOwner: LifecycleOwner)

   fun sendToClassifier(context: Context, imageUri : Uri, appState: SeeFoodAppState)
}