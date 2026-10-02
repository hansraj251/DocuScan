package com.docu.scan.scanner

import android.app.Activity
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanner
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL

class DocumentScannerController(
    private val scanner: GmsDocumentScanner
) {

    fun start(
        activity: Activity,
        launcher: androidx.activity.result.ActivityResultLauncher<IntentSenderRequest>
    ) {
        scanner
            .getStartScanIntent(activity)
            .addOnSuccessListener { intentSender ->
                launcher.launch(
                    IntentSenderRequest.Builder(intentSender).build()
                )
            }
            .addOnFailureListener { error ->
                error.printStackTrace()
            }
    }
}

@Composable
fun rememberDocumentScannerController(
    onResult: (GmsDocumentScanningResult) -> Unit,
    onError: (Exception) -> Unit
): Pair<DocumentScannerController, androidx.activity.result.ActivityResultLauncher<IntentSenderRequest>> {

    val context = LocalContext.current

    val scanner = remember {
        val options =
            GmsDocumentScannerOptions.Builder()
                .setGalleryImportAllowed(true)
                .setPageLimit(50)
                .setResultFormats(
                    RESULT_FORMAT_JPEG,
                    RESULT_FORMAT_PDF
                )
                .setScannerMode(SCANNER_MODE_FULL)
                .build()

        GmsDocumentScanning.getClient(options)
    }

    val launcher =
        androidx.activity.compose.rememberLauncherForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val scanResult =
                        GmsDocumentScanningResult
                            .fromActivityResultIntent(result.data)

                    if (scanResult != null) {
                        onResult(scanResult)
                    } else {
                        onError(
                            IllegalStateException(
                                "Scanner returned no result"
                            )
                        )
                    }
                } catch (exception: Exception) {
                    onError(exception)
                }
            }
        }

    return remember(scanner) {
        Pair(
            DocumentScannerController(scanner),
            launcher
        )
    }
}
