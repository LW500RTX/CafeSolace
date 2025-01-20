package com.example.cafesolace.Data

import com.example.cafesolace.Model.DessertP
import com.example.cafesolace.Model.MainsP
import com.example.cafesolace.R

class DessertPItems {


    fun loadDessertItems(): List<DessertP> {
        return listOf<DessertP>(
            DessertP(
                R.drawable.cupcakes,
                R.string.Cap_cakes,
                R.string.Cap_cakes_Price,
            ),
            DessertP(
                R.drawable.raspberrytart,
                R.string.Raspberry_Tart,
                R.string.Raspberry_Tart_Price,
            ),
            DessertP(
                R.drawable.icecream,
                R.string.Ice_Cream_Bowl,
                R.string.Ice_Cream_Bowl_Price,
            ),
            DessertP(
                R.drawable.macarons,
                R.string.Macarons,
                R.string.Macarons_Price,
            ),
            DessertP(
                R.drawable.berrywithwhippedcreamcake,
                R.string.Berry_with_Whipped_cream_cake,
                R.string.Berry_with_Whipped_cream_cake_Price,
            ),
        )
    }
}