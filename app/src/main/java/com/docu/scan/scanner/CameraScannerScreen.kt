package com.docu.scan.scanner

import android.app.Activity
import android.widget.Toast

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

import com.docu.scan.library.ScanRepositoryProvider
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CameraScannerScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val activity = context as? Activity

    val repository =
        ScanRepositoryProvider.get(context)

    val controller =
        rememberDocumentScannerController(

            onResult = { result ->

                val pdf =
                    result.getPdf()

                val pdfUri =
                    pdf?.getUri()

                val pageCount =
                    pdf?.getPageCount()
                        ?: result.getPages()?.size
                        ?: 0

                if (pdfUri == null) {

                    Toast.makeText(
                        context,
                        "No PDF returned by scanner",
                        Toast.LENGTH_LONG
                    ).show()

                    return@rememberDocumentScannerController
                }

                CoroutineScope(Dispatchers.IO).launch {

                    try {

                        val document =
                            repository.saveScan(
                                sourceUri = pdfUri,
                                pageCount = pageCount
                            )

                        launch(Dispatchers.Main) {

                            Toast.makeText(
                                context,
                                "Saved: ${document.title} • ${document.pageCount} page(s)",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } catch (exception: Exception) {

                        launch(Dispatchers.Main) {

                            Toast.makeText(
                                context,
                                "Failed to save scan: ${exception.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        exception.printStackTrace()
                    }
                }
            },

            onError = { exception ->

                Toast.makeText(
                    context,
                    "Scanner error: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()

                exception.printStackTrace()
            }
        )

    val scannerController =
        controller.first

    val scannerLauncher =
        controller.second

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "DOCUSCAN",
                color = Color.White
            )

            Button(
                onClick = {

                    if (activity != null) {

                        scannerController.start(
                            activity,
                            scannerLauncher
                        )
                    }
                }
            ) {

                Text("SCAN DOCUMENT")
            }

            OutlinedButton(
                onClick = onBack
            ) {
                Text("BACK")
            }
        }
    }
}
