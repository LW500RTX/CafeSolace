package com.example.cafesolace.Data

import com.example.cafesolace.Model.Main
import com.example.cafesolace.R

class FoodItems {

    fun loadFoodItems(): List<Main> {
        return listOf<Main>(
            Main(
                R.drawable.capuchino,
                R.string.item_espresso,
                R.string.price_espresso,
            ),
            Main(
                R.drawable.latte01,
                R.string.item_latte,
                R.string.price_espresso,
            ),
            Main(
                R.drawable.caffeine,
                R.string.item_cappuccino,
                R.string.price_espresso,
            ),
            Main(
                R.drawable.mocha,
                R.string.item_mocha,
                R.string.price_espresso,
            ),

            )
    }
}