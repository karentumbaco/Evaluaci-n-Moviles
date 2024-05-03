/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.inventory.data.PLantasRepository
import com.example.inventory.data.Planta
import java.text.NumberFormat

class ItemEntryViewModel(private val plantasRepository: PLantasRepository) : ViewModel() {

    var itemUiState by mutableStateOf(ItemUiState())
        private set
    fun updateUiState(itemDetails: PlantaDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    suspend fun saveItem() {
        if (validateInput()) {
            plantasRepository.insertItem(itemUiState.itemDetails.toItem())
        }
    }

    private fun validateInput(uiState: PlantaDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            nombre.isNotBlank() && precio.isNotBlank() && cantidad.isNotBlank()
        }
    }
}

data class ItemUiState(
    val itemDetails: PlantaDetails = PlantaDetails(),
    val isEntryValid: Boolean = false
)

data class PlantaDetails(
    val id: Int = 0,
    val nombre: String = "",
    val precio: String = "",
    val cantidad: String = "",
)

fun PlantaDetails.toItem(): Planta = Planta(
    id = id,
    nombre = nombre,
    precio = precio .toDoubleOrNull() ?: 0.0,
    cantidad = cantidad.toIntOrNull() ?: 0
)

fun Planta.formatedPrice(): String {
    return NumberFormat.getCurrencyInstance().format(precio)
}

fun Planta.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun Planta.toItemDetails(): PlantaDetails = PlantaDetails(
    id = id,
    nombre = nombre,
    precio = precio.toString(),
    cantidad = cantidad.toString()
)
