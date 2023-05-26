package com.shutterfly.pixabaygallery.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.shutterfly.pixabaygallery.viewmodels.GalleryViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

private const val PARAMETER_ID_NAME = "id"
private const val PARAMETER_IMAGE_URL = "url"

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    viewModel: GalleryViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Gallery.route
    )
    {
        composable(route = Screen.Gallery.route) {
            GalleryScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = Screen.GalleryItem.route,
            arguments = listOf(
                navArgument(PARAMETER_ID_NAME) {
                    type = NavType.IntType
                },
                navArgument(PARAMETER_IMAGE_URL) {
                    type = NavType.StringType
                }
            )
        ) {
            GalleryItemScreen(
                navController = navController,
                id = it.arguments?.getInt(PARAMETER_ID_NAME) ?: 0,
                url = it.arguments?.getString(PARAMETER_IMAGE_URL) ?: ""
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Gallery : Screen(route = "gallery")
    object GalleryItem :
        Screen(route = "gallery_item/{$PARAMETER_ID_NAME}/{$PARAMETER_IMAGE_URL}") {
        fun addParams(id: Int, url: String): String {
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            return "gallery_item/$id/$encodedUrl"
        }
    }
}