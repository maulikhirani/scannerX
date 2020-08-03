package maulik.barcodescanner

interface ScanningResultListener {
    fun onScanned(result: String)
}