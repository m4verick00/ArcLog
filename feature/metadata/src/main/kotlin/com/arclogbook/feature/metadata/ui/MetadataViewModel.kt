package com.arclogbook.feature.metadata.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arclogbook.database.dao.MetadataDao
import com.arclogbook.database.entity.MetadataExtractionEntity
import com.arclogbook.feature.metadata.domain.MetadataExtractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MetadataUiState(
    val extractions: List<MetadataExtractionEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MetadataViewModel @Inject constructor(
    private val metadataExtractor: MetadataExtractor,
    private val metadataDao: MetadataDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(MetadataUiState())
    val uiState: StateFlow<MetadataUiState> = _uiState.asStateFlow()

    init {
        loadExtractions()
    }

    private fun loadExtractions() {
        viewModelScope.launch {
            // In a real app, use Paging 3 for large datasets
            // For now, load recent extractions
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                // This is a simplified approach - in production use PagingSource
                val extractions = emptyList<MetadataExtractionEntity>() // metadataDao.getAllExtractions()
                _uiState.update { 
                    it.copy(
                        extractions = extractions,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun extractMetadata(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            metadataExtractor.extractMetadata(uri, enableOnlineGeocoding = false)
                .onSuccess { extraction ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            extractions = listOf(extraction) + currentState.extractions,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    fun openLocation(context: Context, latitude: Double, longitude: Double) {
        try {
            // Try to open Google Maps
            val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude(Location)")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            
            if (mapIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(mapIntent)
            } else {
                // Fallback to browser
                val browserUri = Uri.parse("https://maps.google.com/?q=$latitude,$longitude")
                val browserIntent = Intent(Intent.ACTION_VIEW, browserUri)
                context.startActivity(browserIntent)
            }
        } catch (e: Exception) {
            // Handle error silently or show toast
        }
    }
}