package com.example.cafesolace.Data

import com.example.cafesolace.Model.MainsP
import com.example.cafesolace.R

class MainPItems {


    fun loadMainItems(): List<MainsP> {
        return listOf<MainsP>(
            MainsP(
                R.drawable.italiancuisine,
                R.string.Italian_Cuisine,
                R.string.Italian_Cuisine_Price,
            ),
            MainsP(
                R.drawable.margheritapizza,
                R.string.Margherita_Pizza,
                R.string.Margherita_Pizza_Price,
            ),
            MainsP(
                R.drawable.bangers,
                R.string.Bangers_and_Mash,
                R.string.Bangers_and_Mash_Price,
            ),
            MainsP(
                R.drawable.fishandchips,
                R.string.Fish_and_Chips,
                R.string.Fish_and_Chips_Price,
            ),
            MainsP(
                R.drawable.hamandchees,
                R.string.Ham_and_Cheese,
                R.string.Ham_and_Cheese_Price,
            ),
            )
    }
}