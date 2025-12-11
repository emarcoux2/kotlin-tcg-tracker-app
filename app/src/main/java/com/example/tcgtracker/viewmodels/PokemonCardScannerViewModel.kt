package com.example.tcgtracker.viewmodels

//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import com.example.tcgtracker.model.ScanType
//import com.example.tcgtracker.model.ScannedItem
//
//class PokemonCardScannerViewModel : ViewModel() {
//    // holds the list of scanned results
//    var items by mutableStateOf<List<ScannedItem>>(emptyList())
//        private set
//
//    // creates a new ScannedItem object using the provided type and content
//    // and adds the new item to the front of the list
//    fun addItem(type: ScanType, content: String) {
//        items = listOf(ScannedItem(type = type, content = content)) + items
//    }
//
//    // removes items all scanned items from the list
//    fun clear() {
//        items = emptyList()
//    }
//}