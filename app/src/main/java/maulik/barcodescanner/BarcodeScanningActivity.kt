package maulik.barcodescanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import maulik.barcodescanner.databinding.ActivityBarcodeScanningBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BarcodeScanningActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, BarcodeScanningActivity::class.java)
            context.startActivity(starter)
        }
    }

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var binding: ActivityBarcodeScanningBinding
    /** Blocking camera operations are performed using this executor */
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarcodeScanningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider?) {

        cameraProvider?.unbindAll()

        val preview: Preview = Preview.Builder()
            .build()

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()


        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(cameraExecutor, YourImageAnalyzer())

        preview.setSurfaceProvider(binding.cameraPreview.createSurfaceProvider())

        cameraProvider?.bindToLifecycle(this, cameraSelector, imageAnalysis, preview)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Shut down our background executor
        cameraExecutor.shutdown()
    }
}