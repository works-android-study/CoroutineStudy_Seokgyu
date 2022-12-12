package com.example.searchimagecoroutine

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.searchimagecoroutine.data.SearchImageApiItem
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: SearchImageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "search") {
                composable("search") {
                    Column {
                        SearchAppBar()
                        ImageList(navController)
                    }
                }
                composable("detail") {
                    DetailScreen()
                }
            }
        }
        bindObserver()
    }

    @Composable
    private fun SearchAppBar() {
        var query = remember { viewModel.inputText }

        TextField(
            value = query.value,
            onValueChange = { onQueryChanged ->
                query.value = onQueryChanged
            },
            leadingIcon = {
                IconButton(onClick = {
                    viewModel.setSearchText(viewModel.inputText.value.text)
                    viewModel.inputText.value = TextFieldValue("")
                    viewModel.isSearchMode = true
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        tint = MaterialTheme.colors.onBackground,
                        contentDescription = "Search Icon"
                    )
                }
            },
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            textStyle = MaterialTheme.typography.subtitle1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.background, shape = RectangleShape)
        )
    }

    @Composable
    fun ImageList(navHostController: NavHostController) {
        val list = viewModel.flow.collectAsLazyPagingItems()
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                items(list.itemCount) { idx ->
                    list[idx]?.let {
                        ImageView(it, navHostController)
                        if (viewModel.isSearchMode) {
                            Box {
                                Icon(painter = painterResource(id = androidx.appcompat.R.drawable.abc_ic_star_black_36dp),
                                    contentDescription = "start",
                                    tint = Color.Yellow,
                                    modifier = Modifier
                                        .clickable {
                                            viewModel.insertSearchItem(it)
                                        }
                                        .align(Alignment.TopEnd))
                            }
                        }
                    }
                }
            })
    }

    @Composable
    fun ImageView(item: SearchImageApiItem, navHostController: NavHostController) {
        GlideImage(
            imageModel = item.link,
            modifier = Modifier
                .size(128.dp)
                .clickable {
                    viewModel.setCurrentStateItem(item)
                    navHostController.navigate("detail")
                }
        )
    }

    @Composable
    fun DetailScreen() {
        val scrollState = rememberScrollState()
        Column(
            Modifier
                .fillMaxSize()
                .padding(12.dp)
                .verticalScroll(scrollState)
        ) {
            SetTitle()
            SetDetailImage()
            SetSizeHeight()
            SetSizeWidth()
            SetDownloadButton()
            SetDownloadStateText()
        }

    }

    @Composable
    fun SetTitle() {
        Text(text = viewModel.currentStateItem.value?.title.orEmpty())
    }

    @Composable
    fun SetDetailImage() {
        GlideImage(
            imageModel = viewModel.currentStateItem.value?.thumbnail,
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
                .border(2.dp, Color.Black)
        )
    }

    @Composable
    fun SetSizeHeight() {
        Text(text = viewModel.currentStateItem.value?.sizeheight.orEmpty())
    }

    @Composable
    fun SetSizeWidth() {
        Text(text =  viewModel.currentStateItem.value?.sizewidth.orEmpty())
    }
    @Composable
    fun SetDownloadButton() {
        Button(onClick = { viewModel.imageDownload(imageUrl =viewModel.currentStateItem.value?.link.orEmpty(), filePath = applicationContext.filesDir.absolutePath) }) {
            Text(text = applicationContext.getString(R.string.download))
        }
    }
    
    @Composable
    fun SetDownloadStateText() {
        val downloadLate = viewModel.downloadRateFlow.collectAsState()
        Text(text = if (downloadLate.value == -1) "" else "${downloadLate.value}")
    }

    fun bindObserver() {
        viewModel.isSuccessSaveSearchItem.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(applicationContext, R.string.save_success_to_db, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.isSuccessDownloadItem.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(applicationContext, R.string.save_success_to_dir, Toast.LENGTH_SHORT).show()
            }
        }

    }
}
