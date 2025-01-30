package com.example.cafesolace.Pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cafesolace.CommonSection.MasterView
import com.example.cafesolace.Model.Main
import com.example.cafesolace.R

@Composable
fun detailviewPage(main: Main, navController: NavController){



        MasterView(main = main, navController = navController)
    }

