package com.example.tcgtracker.api

import com.example.tcgtracker.db.PokemonCardDao

class PokemonRepository(
    private val tcgdexService: PokemonTCGdexService,
    private val dao: PokemonCardDao
)