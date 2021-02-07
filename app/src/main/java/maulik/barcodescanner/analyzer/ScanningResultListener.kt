package maulik.barcodescanner.analyzer

interface ScanningResultListener {
    fun onScanned(result: String)
}