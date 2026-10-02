package com.docu.scan.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.docu.scan.library.ScanDocument
import com.docu.scan.library.ScanRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ScanViewModel(
    private val repository: ScanRepository
) : ViewModel() {

    val documents: StateFlow<List<ScanDocument>> =
        repository
            .observeAll()
            .map(::sortDocuments)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun rename(
        id: Long,
        title: String
    ) {
        viewModelScope.launch {
            repository.rename(
                id = id,
                title = title
            )
        }
    }

    fun delete(
        document: ScanDocument
    ) {
        viewModelScope.launch {
            repository.delete(document)
        }
    }

    companion object {

        fun sortDocuments(
            documents: List<ScanDocument>
        ): List<ScanDocument> {
            return documents.sortedByDescending {
                it.createdAt
            }
        }

        fun hasDocuments(
            documents: List<ScanDocument>
        ): Boolean {
            return documents.isNotEmpty()
        }
    }
}
