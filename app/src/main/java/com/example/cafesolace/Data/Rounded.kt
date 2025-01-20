package com.example.cafesolace.Data

import com.example.cafesolace.Model.Round
import com.example.cafesolace.R

class RoundedItems {

    fun loadRoundedItems(): List<Round> {
        return listOf(
            Round(imageResourceId = R.drawable.latte25),
            Round(imageResourceId = R.drawable.a),
            Round(imageResourceId = R.drawable.b),
            Round(imageResourceId = R.drawable.c)
        )
    }
}
