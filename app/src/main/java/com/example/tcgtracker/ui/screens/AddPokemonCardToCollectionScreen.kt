package com.example.tcgtracker.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tcgtracker.db.PokemonCardRepository
import com.example.tcgtracker.viewmodels.AddPokemonCardToCollectionViewModel
import com.example.tcgtracker.viewmodels.AddPokemonCardToCollectionViewModelFactory

@Composable
fun AddCardToCollectionScreen(
    navController: NavController,
    repository: PokemonCardRepository
) {
    val vm: AddPokemonCardToCollectionViewModel = viewModel(
        factory = AddPokemonCardToCollectionViewModelFactory(repository)
    )

    val results by vm.searchResults.collectAsState()
    val selected by vm.selected.collectAsState()
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()

    var query by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {

        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                vm.search(it)
            },
            label = { Text("Search by card name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        if (loading) {
            CircularProgressIndicator()
        }

        if (error != null) {
            Text("Error: $error", color = Color.Red)
        }

        LazyColumn {
            items(results) { card ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Checkbox(
                        checked = selected.contains(card.id),
                        onCheckedChange = { vm.toggleSelected(card.id) }
                    )
                    Text(card.name, Modifier.padding(start = 8.dp))
                }
            }
        }

        if (selected.isNotEmpty()) {
            Button(
                onClick = {
                    vm.addSelected()
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text("Add (${selected.size}) to My Cards")
            }
        }
    }
}