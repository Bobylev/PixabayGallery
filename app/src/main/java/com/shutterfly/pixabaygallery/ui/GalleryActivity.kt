package com.shutterfly.pixabaygallery.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.shutterfly.pixabaygallery.R
import com.shutterfly.pixabaygallery.databinding.ActivityGalleryBinding
import com.shutterfly.pixabaygallery.models.GalleryItem
import com.shutterfly.pixabaygallery.repositories.GalleryRepository
import com.shutterfly.pixabaygallery.viewmodels.GalleryViewModel
import com.shutterfly.pixabaygallery.viewmodels.GalleryViewModelFactory

class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding

    private val viewModel by viewModels<GalleryViewModel> {
        GalleryViewModelFactory(GalleryRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContent {
            MaterialTheme {
                GalleryScreen()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun GalleryScreen() {
        var search by remember { mutableStateOf("") }
        val items = viewModel.imageListObservable.collectAsLazyPagingItems()

        Scaffold(
            modifier = Modifier.imePadding(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(stringResource(R.string.app_name))
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        titleContentColor = colorResource(R.color.white),
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
                searchView(
                    search = search,
                    onSearchClick = {
                        viewModel.onSearchButtonClicked(it)
                    },
                    onValueChange = { search = it })
                galleryView(items = items)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun searchView(
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
                placeholderColor = Color(0XFF888D91),
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
    fun galleryView(
        items: LazyPagingItems<GalleryItem>,
        modifier: Modifier = Modifier
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp)
        ) {
            items(items.itemCount) { id ->
                Card(
                    modifier = modifier
                        .height(100.dp)
                        .padding(2.dp),
                    shape = RectangleShape
                ) {
                    GlideImage(
                        modifier = modifier.fillMaxSize(),
                        model = items[id]?.previewUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}