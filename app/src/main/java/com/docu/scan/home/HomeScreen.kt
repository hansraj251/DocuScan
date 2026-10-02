package com.docu.scan.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.docu.scan.library.ScanDocument
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ScanViewModel,
    onScan: () -> Unit,
    onDocumentClick: (ScanDocument) -> Unit
) {
    val documents by
        viewModel.documents.collectAsStateWithLifecycle()

    var renameDocument by
        remember {
            mutableStateOf<ScanDocument?>(null)
        }

    var deleteDocument by
        remember {
            mutableStateOf<ScanDocument?>(null)
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("DocuScan")
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Text(
                text = "Your Documents",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "${documents.size} document(s)",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            if (!ScanViewModel.hasDocuments(documents)) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalAlignment =
                        Alignment.CenterHorizontally,
                    verticalArrangement =
                        Arrangement.Center
                ) {

                    Text(
                        text = "No documents yet",
                        style =
                            MaterialTheme.typography
                                .headlineSmall
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            "Scan your first document to get started."
                    )

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    Button(
                        onClick = onScan
                    ) {
                        Text("SCAN DOCUMENT")
                    }
                }

            } else {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    items(
                        items = documents,
                        key = { it.id }
                    ) { document ->

                        DocumentCard(
                            document = document,
                            onClick = {
                                onDocumentClick(document)
                            },
                            onRename = {
                                renameDocument = document
                            },
                            onDelete = {
                                deleteDocument = document
                            }
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onScan
                ) {
                    Text("SCAN NEW DOCUMENT")
                }
            }
        }
    }

    renameDocument?.let { document ->

        var title by
            remember(document.id) {
                mutableStateOf(document.title)
            }

        AlertDialog(
            onDismissRequest = {
                renameDocument = null
            },
            title = {
                Text("Rename document")
            },
            text = {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    singleLine = true,
                    label = {
                        Text("Title")
                    }
                )
            },
            confirmButton = {

                TextButton(
                    onClick = {
                        if (title.trim().isNotEmpty()) {
                            viewModel.rename(
                                id = document.id,
                                title = title
                            )
                            renameDocument = null
                        }
                    }
                ) {
                    Text("SAVE")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        renameDocument = null
                    }
                ) {
                    Text("CANCEL")
                }
            }
        )
    }

    deleteDocument?.let { document ->

        AlertDialog(
            onDismissRequest = {
                deleteDocument = null
            },
            title = {
                Text("Delete document?")
            },
            text = {
                Text(
                    "This will permanently delete " +
                        "\"${document.title}\"."
                )
            },
            confirmButton = {

                TextButton(
                    onClick = {
                        viewModel.delete(document)
                        deleteDocument = null
                    }
                ) {
                    Text("DELETE")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        deleteDocument = null
                    }
                ) {
                    Text("CANCEL")
                }
            }
        )
    }
}

@Composable
private fun DocumentCard(
    document: ScanDocument,
    onClick: () -> Unit,
    onRename: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = document.title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text =
                    "${document.pageCount} page(s) • " +
                        formatDate(document.createdAt),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Row {

                OutlinedButton(
                    onClick = onRename
                ) {
                    Text("RENAME")
                }

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                OutlinedButton(
                    onClick = onDelete
                ) {
                    Text("DELETE")
                }
            }
        }
    }
}

private fun formatDate(
    timestamp: Long
): String {
    return SimpleDateFormat(
        "dd MMM yyyy, hh:mm a",
        Locale.getDefault()
    ).format(Date(timestamp))
}
