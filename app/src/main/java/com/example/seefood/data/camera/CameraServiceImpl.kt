package com.example.seefood.data.camera

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.seefood.RESULT_SCREEN
import com.example.seefood.SeeFoodAppState
import com.example.seefood.common.util.URIPathHelper
import com.example.seefood.data.models.ClassificationResult
import com.example.seefood.data.network.ApiService
import com.example.seefood.database.objects.Dish
import com.example.seefood.database.repos.DishRepository
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

/**
 * Имплементация интерфейса взаимодействия с камерой
 *
 * @param[cameraProvider]
 * @param[selector]
 * @param[preview]
 * @param[imageCapture]
 */
class CameraServiceImpl(
   private val dishRepository: DishRepository,
   private val apiService: ApiService,
   private val cameraProvider: ProcessCameraProvider,
   private val selector: CameraSelector,
   private val preview: Preview,
   private val imageCapture: ImageCapture
): CameraService {

   private var imgPath : String = ""

   @SuppressLint("SimpleDateFormat")
   override suspend fun captureAndSaveImage(context: Context, appState: SeeFoodAppState){
      val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.ENGLISH).format(System.currentTimeMillis())

      val contentValues = ContentValues().apply {
         put(MediaStore.MediaColumns.DISPLAY_NAME,name)
         put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg")
         if (Build.VERSION.SDK_INT > 28){
            put(MediaStore.Images.Media.RELATIVE_PATH,"Pictures/SeeFood")
         }
      }

      val outputOptions = ImageCapture.OutputFileOptions
         .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
         )
         .build()

      imageCapture.takePicture(
         outputOptions,
         ContextCompat.getMainExecutor(context),
         object : ImageCapture.OnImageSavedCallback{
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
               val helper = URIPathHelper()
               val imagePath = helper.getPath(context, outputFileResults.savedUri!!)

               imgPath = imagePath!!

               Toast.makeText(
                  context,
                  "Saved image $imagePath",
                  Toast.LENGTH_SHORT
               ).show()

               sendToClassifier(context, outputFileResults.savedUri!!, appState)
            }

            override fun onError(exception: ImageCaptureException) {
               Toast.makeText(
                  context,
                  "Error: ${exception.message}",
                  Toast.LENGTH_SHORT
               ).show()
            }
         }
      )
   }

   override suspend fun showCameraPreview(
      previewView: PreviewView,
      lifecycleOwner: LifecycleOwner
   ) {
      preview.setSurfaceProvider(previewView.surfaceProvider)
      try {
         cameraProvider.unbindAll()
         cameraProvider.bindToLifecycle(
            lifecycleOwner,
            selector,
            preview,
            imageCapture
         )
      }catch (e:Exception){
         e.printStackTrace()
      }
   }

   override fun sendToClassifier(context: Context, imageUri: Uri, appState: SeeFoodAppState) {
      val contentResolver = context.contentResolver
      val inputStream = contentResolver.openInputStream(imageUri)
      val requestFile = inputStream?.readBytes()?.toRequestBody("".toMediaTypeOrNull(), 0)

      inputStream?.close()

      val helper = URIPathHelper()
      val path = helper.getPath(context, imageUri)
      val name = path?.split("/")?.last()

      println(path)
      val body = requestFile?.let { MultipartBody.Part.createFormData("photo", name.toString(), it) }

      runBlocking {
         if (body != null) {
            val response : ClassificationResult? = apiService.sendImageToClassifier(body).body()

            val newDish = Dish(
               name         = response?.name_dish!!,
               recipe       = response.recipe_dish!!,
               imgLocalPath = imgPath
            )
            println(newDish)

            val newDishId = dishRepository.upsertDish(newDish)?.toInt()
            appState.navigate("$RESULT_SCREEN/$newDishId")
            println(newDishId)
         }
      }
   }
}