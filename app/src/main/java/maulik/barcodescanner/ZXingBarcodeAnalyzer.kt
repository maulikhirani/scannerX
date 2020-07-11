package maulik.barcodescanner

import android.content.res.Resources.NotFoundException
import android.graphics.ImageFormat
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import java.nio.ByteBuffer


class ZXingBarcodeAnalyzer : ImageAnalysis.Analyzer {

    private var multiFormatReader: MultiFormatReader = MultiFormatReader()

    override fun analyze(image: ImageProxy) {
        if ((image.format == ImageFormat.YUV_420_888 || image.format == ImageFormat.YUV_422_888
                    || image.format == ImageFormat.YUV_444_888) && image.planes.size == 3) {
            val buffer: ByteBuffer = image.planes.get(0).buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)
            val planarYUVLuminanceSource = PlanarYUVLuminanceSource(
                bytes,
                image.width,
                image.height,
                0, 0,
                image.width,
                image.height,
                false
            )
            val hybridBinarizer = HybridBinarizer(planarYUVLuminanceSource)
            val binaryBitmap = BinaryBitmap(hybridBinarizer)
            try {
                val rawResult = multiFormatReader.decodeWithState(binaryBitmap)
                Log.d("Barcode:", rawResult.text)
            } catch (e: NotFoundException) {
                e.printStackTrace()
            } finally {
                multiFormatReader.reset()
                image.close()
            }
        }
    }
}