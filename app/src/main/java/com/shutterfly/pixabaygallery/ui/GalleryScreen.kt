package com.shutterfly.pixabaygallery.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.shutterfly.pixabaygallery.R
import com.shutterfly.pixabaygallery.models.GalleryItem
import com.shutterfly.pixabaygallery.viewmodels.GalleryViewModel

private const val GRID_LANDSCAPE_COLUMNS_COUNT = 4
private const val GRID_PORTRAIT_COLUMNS_COUNT = 3
private val GRID_CELL_CARD_HEIGHT = 100.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(navController: NavController, viewModel: GalleryViewModel) {
    var search by rememberSaveable { mutableStateOf("") }
    val items = viewModel.imageListObservable.collectAsLazyPagingItems()

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                modifier = Modifier,
                title = {
                    Text(stringResource(R.string.app_name))
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    titleContentColor = colorResource(R.color.black),
                    containerColor = colorResource(R.color.purple_500)
                ),
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            SearchView(
                search = search,
                onSearchClick = {
                    viewModel.onSearchButtonClicked(it)
                },
                onValueChange = { search = it })
            GalleryView(
                items = items,
                onItemClick = { id, url ->
                    navController.navigate(route = Screen.GalleryItem.addParams(id, url))
                },
                viewModel = viewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    search: String,
    onSearchClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = search,
        onValueChange = onValueChange,
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = colorResource(R.color.teal_200),
            placeholderColor = Color.Black,
            textColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black,
        ),
        trailingIcon = {
            IconButton(
                modifier = modifier
                    .background(color = colorResource(R.color.teal_700)),
                onClick = {
                    onSearchClick(search)
                },
                enabled = search.isNotEmpty()
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = ""
                )
            }
        },
        placeholder = { Text(text = stringResource(R.string.search_hint)) }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GalleryView(
    items: LazyPagingItems<GalleryItem>,
    onItemClick: (Int, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel
) {
    var columnCount = GRID_PORTRAIT_COLUMNS_COUNT
    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) columnCount =
        GRID_LANDSCAPE_COLUMNS_COUNT
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnCount)
    ) {
        items(items.itemCount) { id ->
            Card(
                modifier = modifier
                    .height(GRID_CELL_CARD_HEIGHT)
                    .padding(2.dp)
                    .clickable {
                        items[id]?.let {
                            onItemClick(
                                it.id,
                                it.imageURL
                            )
                        }
                    },
                shape = RectangleShape
            ) {
                Box(modifier = modifier.fillMaxSize()) {
                    GlideImage(
                        modifier = modifier.fillMaxSize(),
                        model = items[id]?.previewUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    if (viewModel.isLiked(items[id]?.id ?: 0)) {
                        Icon(
                            modifier = modifier
                                .size(28.dp, 28.dp)
                                .align(Alignment.TopEnd)
                                .padding(PaddingValues(0.dp, 4.dp, 4.dp)),
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}