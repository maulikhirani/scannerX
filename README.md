# Introducing ScannerX! QR Code and Barcode Scanning with CameraX

Creating a QR code or barcode scanner was a tediuos task for developers as it required setting up lots of stuff related to Camera. Camera APIs have never been fun to work with. Traditional Camera APIs in Android are not reliable as they do not work for all the mobile phones universally. 

But this is not the case anymore. With the latest library-bundle introduced as part of Android Jetpack - [CameraX](https://developer.android.com/training/camerax) - we no longer need to worry about all the boilerplate code to setup camera preview and image analysis for scanning barcodes.

## ScannerX App

![ScannerCover](https://user-images.githubusercontent.com/25861055/128851911-4a6d77d6-8318-4cf6-99bd-ee324318b541.jpg)

ScannerX app is a showcase of how the QR/Barcode scanner can be implemented with the use of CameraX libraries. It also focuses on the demonstrating how different scanner SDKs can be incorporated with CameraX. 

**CameraX + Barcode Scanning SDK = ScannerX**

ScannerX features following barcode scanner SDKs:
1. [MLKit](https://developers.google.com/ml-kit/vision/barcode-scanning/android)
2. [ZXing](https://github.com/zxing/zxing)

You can pick which SDK you want to use for scanning from the app itself. This will also help you compare how both popular SDKs perform in various positions and distances.

![Scanner Options](https://user-images.githubusercontent.com/25861055/128852109-831a017b-b4b3-4012-9e9e-f333f3b9a3b0.jpg)
 ![Scanning Result](https://user-images.githubusercontent.com/25861055/128852186-82ba5dcb-063c-451e-b3bd-c0c6e08b3fe7.jpg)

**Other features of ScannerX includes:**
- A Square(ish) transparent viewfinder with semi-transparent overlay on top of camera preview (transparent hole in non-transparent layout)
- Turn on/off torch for scanning even in dark
- Show scanned code in modal bottom sheet on top of the the camera preview
- Copy the scanned QR/Barcode

**Improvements planned:**
1. Add animation for viewfinder.
2. ScannerX scans entire camera frame for barcodes right now. We can add the feature to scan only the part of viewfinder. (transparent hole)
3. Add more scanner SDKs. (Let me know if you know any!)
4. Code Quality Improvements.

Your feedback and suggestions are most welcome!

References:
- https://developer.android.com/training/camerax
- https://developers.google.com/ml-kit/vision/barcode-scanning/android
- https://github.com/dm77/barcodescanner
- https://github.com/googlesamples/mlkit/tree/master/android/material-showcase
