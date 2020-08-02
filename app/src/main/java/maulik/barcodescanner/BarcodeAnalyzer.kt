package maulik.barcodescanner

import android.view.View
import androidx.camera.core.ImageAnalysis

interface BarcodeAnalyzer: ImageAnalysis.Analyzer {
    fun scanBarcodeOnlyAtCenter(viewFinder: View)
}