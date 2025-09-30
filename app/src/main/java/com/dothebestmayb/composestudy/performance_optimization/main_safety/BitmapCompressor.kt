package com.dothebestmayb.composestudy.performance_optimization.main_safety

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt

/**
 * blocking 요소 : openInputStream, decodeByteArray, compress
 * 위 함수들은 suspend function이 아니기 때문에 실행 중인 thread를 blocking한다.
 *
 * Main Safety : suspend function은 Main Thread에서 호출해도 안전(Blocking 하지 않음)하게 만들어야 한다.
 */
class BitmapCompressor(
    private val context: Context
) {

    suspend fun compressImage(
        contentUri: Uri,
        compressionThreshold: Long
    ): Bitmap? {
        return withContext(Dispatchers.IO) {
            val inputBytes = context
                .contentResolver
                .openInputStream(contentUri)?.use { inputStream ->
                    inputStream.readBytes()
                } ?: return@withContext null

            // CPU 연산을 요구하는 작업 Default로 처리
            withContext(Dispatchers.Default) {
                val bitmap = BitmapFactory.decodeByteArray(inputBytes, 0, inputBytes.size)
                var outputBytes: ByteArray
                var quality = 100
                do {
                    ByteArrayOutputStream().use { outputStream ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                        outputBytes = outputStream.toByteArray()
                        quality -= (quality * 0.1).roundToInt()
                    }
                } while (outputBytes.size > compressionThreshold && quality > 5)
                BitmapFactory.decodeByteArray(outputBytes, 0, outputBytes.size)
            }
        }

    }

    suspend fun compressImageNotSafety(
        contentUri: Uri,
        compressionThreshold: Long
    ): Bitmap? {
        val inputBytes = context
            .contentResolver
            .openInputStream(contentUri)?.use { inputStream ->
                inputStream.readBytes()
            } ?: return null
        val bitmap = BitmapFactory.decodeByteArray(inputBytes, 0, inputBytes.size)
        var outputBytes: ByteArray
        var quality = 100
        do {
            ByteArrayOutputStream().use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                outputBytes = outputStream.toByteArray()
                quality -= (quality * 0.1).roundToInt()
            }
        } while (outputBytes.size > compressionThreshold && quality > 5)

        return BitmapFactory.decodeByteArray(outputBytes, 0, outputBytes.size)
    }
}
