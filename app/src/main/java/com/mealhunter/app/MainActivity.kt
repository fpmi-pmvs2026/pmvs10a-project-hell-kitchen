package com.mealhunter.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.mealhunter.app.di.ViewModelFactory
import com.mealhunter.app.ui.navigation.MealHunterNavGraph
import com.mealhunter.app.ui.screens.detail.DetailViewModel
import com.mealhunter.app.ui.screens.favorites.FavoritesViewModel
import com.mealhunter.app.ui.screens.fridge.FridgeViewModel
import com.mealhunter.app.ui.screens.home.HomeViewModel
import com.mealhunter.app.ui.screens.search.SearchViewModel
import com.mealhunter.app.ui.theme.MealHunterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val factory = ViewModelFactory(applicationContext)
        val homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        val searchViewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]
        val favoritesViewModel = ViewModelProvider(this, factory)[FavoritesViewModel::class.java]
        val fridgeViewModel = ViewModelProvider(this, factory)[FridgeViewModel::class.java]
        val detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        setContent {
            MealHunterTheme {
                MealHunterNavGraph(
                    homeViewModel = homeViewModel,
                    searchViewModel = searchViewModel,
                    favoritesViewModel = favoritesViewModel,
                    fridgeViewModel = fridgeViewModel,
                    detailViewModel = detailViewModel
                )
            }
        }
    }
}
