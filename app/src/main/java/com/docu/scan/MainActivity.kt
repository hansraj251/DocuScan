package com.docu.scan

import android.os.Bundle
import android.widget.Toast

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.lifecycle.viewmodel.compose.viewModel

import com.docu.scan.home.HomeScreen
import com.docu.scan.home.ScanViewModel
import com.docu.scan.home.ScanViewModelFactory
import com.docu.scan.library.PdfOpener
import com.docu.scan.library.ScanRepositoryProvider
import com.docu.scan.ui.theme.DocuScanTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val repository =
            ScanRepositoryProvider.get(this)

        setContent {

            DocuScanTheme {

                val scanViewModel: ScanViewModel =
                    viewModel(
                        factory =
                            ScanViewModelFactory(
                                repository
                            )
                    )

                HomeScreen(
                    viewModel = scanViewModel,
                    onDocumentClick = { document ->

                        try {

                            PdfOpener.open(
                                this,
                                document.pdfPath
                            )

                        } catch (exception: Exception) {

                            Toast.makeText(
                                this,
                                "Unable to open PDF: " +
                                    "${exception.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                )
            }
        }
    }
}
