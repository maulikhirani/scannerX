package maulik.barcodescanner.analyzer

import android.graphics.ImageFormat
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import java.util.concurrent.atomic.AtomicBoolean


class ZXingBarcodeAnalyzer(private val listener: ScanningResultListener) : ImageAnalysis.Analyzer {

    private var multiFormatReader: MultiFormatReader = MultiFormatReader()
    private var isScanning = AtomicBoolean(false)

    override fun analyze(image: ImageProxy) {

        if (isScanning.get()) {
            image.close()
            return
        }

        isScanning.set(true)

        if ((image.format == ImageFormat.YUV_420_888 || image.format == ImageFormat.YUV_422_888
                    || image.format == ImageFormat.YUV_444_888) && image.planes.size == 3
        ) {
            val rotatedImage = RotatedImage(getLuminancePlaneData(image), image.width, image.height)
            rotateImageArray(rotatedImage, image.imageInfo.rotationDegrees)

            val planarYUVLuminanceSource = PlanarYUVLuminanceSource(
                rotatedImage.byteArray,
                rotatedImage.width,
                rotatedImage.height,
                0, 0,
                rotatedImage.width,
                rotatedImage.height,
                false
            )
            val hybridBinarizer = HybridBinarizer(planarYUVLuminanceSource)
            val binaryBitmap = BinaryBitmap(hybridBinarizer)
            try {
                val rawResult = multiFormatReader.decodeWithState(binaryBitmap)
                Log.d("Barcode:", rawResult.text)
                listener.onScanned(rawResult.text)
            } catch (e: NotFoundException) {
                e.printStackTrace()
            } finally {
                multiFormatReader.reset()
                image.close()
            }

            isScanning.set(false)
        }
    }

    // 90, 180. 270 rotation
    private fun rotateImageArray(imageToRotate: RotatedImage, rotationDegrees: Int) {
        if (rotationDegrees == 0) return // no rotation
        if (rotationDegrees % 90 != 0) return // only 90 degree times rotations

        val width = imageToRotate.width
        val height = imageToRotate.height

        val rotatedData = ByteArray(imageToRotate.byteArray.size)
        for (y in 0 until height) { // we scan the array by rows
            for (x in 0 until width) {
                when (rotationDegrees) {
                    90 -> rotatedData[x * height + height - y - 1] =
                        imageToRotate.byteArray[x + y * width] // Fill from top-right toward left (CW)
                    180 -> rotatedData[width * (height - y - 1) + width - x - 1] =
                        imageToRotate.byteArray[x + y * width] // Fill from bottom-right toward up (CW)
                    270 -> rotatedData[y + x * height] =
                        imageToRotate.byteArray[y * width + width - x - 1] // The opposite (CCW) of 90 degrees
                }
            }
        }

        imageToRotate.byteArray = rotatedData

        if (rotationDegrees != 180) {
            imageToRotate.height = width
            imageToRotate.width = height
        }
    }

    private fun getLuminancePlaneData(image: ImageProxy): ByteArray {
        val plane = image.planes[0]
        val buf = plane.buffer
        val data = ByteArray(buf.remaining())
        buf[data]
        buf.rewind()
        val width = image.width
        val height = image.height
        val rowStride = plane.rowStride
        val pixelStride = plane.pixelStride
        if (width != rowStride || pixelStride != 1) {
            // remove padding from the Y plane data
            val cleanData = ByteArray(width * height)
            for (y in 0 until height) {
                for (x in 0 until width) {
                    cleanData[y * width + x] = data[y * rowStride + x * pixelStride]
                }
            }
            return cleanData
        }
        return data
    }

    private class RotatedImage(var byteArray: ByteArray, var width: Int, var height: Int)
}