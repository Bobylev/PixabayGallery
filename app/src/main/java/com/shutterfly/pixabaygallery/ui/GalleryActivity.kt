package com.shutterfly.pixabaygallery.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.shutterfly.pixabaygallery.repositories.FavoritesRepository
import com.shutterfly.pixabaygallery.repositories.GalleryRepository
import com.shutterfly.pixabaygallery.viewmodels.GalleryViewModel
import com.shutterfly.pixabaygallery.viewmodels.GalleryViewModelFactory

class GalleryActivity : AppCompatActivity() {

    private val viewModel by viewModels<GalleryViewModel> {
        GalleryViewModelFactory(GalleryRepository(), FavoritesRepository(context = applicationContext))
    }

    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                navController = rememberNavController()
                SetupNavGraph(
                    navController = navController,
                    viewModel =  viewModel
                )
            }
        }
    }
}