package com.dothebestmayb.composestudy.performance_optimization.main_safety

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun PhotoPickerScreen(
    compressor: BitmapCompressor,
    modifier: Modifier = Modifier,
) {
    var compressedBitmap by remember {
        mutableStateOf< Bitmap?>(null)
    }
    val scope = rememberCoroutineScope()
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) {
        scope.launch {
            compressedBitmap = compressor.compressImage(
                contentUri = it ?: return@launch,
                compressionThreshold = 1024L // 1MB
            )
        }
    }
    LaunchedEffect(key1 = true) {
        photoPicker.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }
    Column(modifier) {
        compressedBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.height(500.dp)
            )
        }
        RotatingBoxScreen()
    }
}
