package com.example.searchimagecoroutine

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.searchimagecoroutine.data.SearchImageApiItem
import com.example.searchimagecoroutine.detail.DetailActivity
import com.example.searchimagecoroutine.room.entity.SearchImageItem
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: SearchImageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                SearchAppBar()
                ImageList()
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
    fun ImageList() {
        val list = viewModel.flow.collectAsLazyPagingItems()
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                    items(list.itemCount) { idx ->
                        list[idx]?.let {
                            ImageView(it)
                            //Todo 즐겨찾기버튼 추가
                        }
                    }
            })
    }

    @Composable
    fun progressDialog() {
        AlertDialog(onDismissRequest = {},
        title = {
            Text(text = viewModel.downloadLate.observeAsState().value.toString())
        },
        confirmButton = {},
        dismissButton = {})
    }

    @Composable
    fun ImageView(item: SearchImageApiItem) {
        val context = LocalContext.current
        GlideImage(
            imageModel = item.link,
            modifier = Modifier
                .size(128.dp)
                .clickable {
                    viewModel.imageDownload(item.link, applicationContext.filesDir.absolutePath)
                    //viewModel.insertSearchItem(item)
//                    val intent = DetailActivity.createIntent(
//                        context,
//                        title = item.title,
//                        thumbnail = item.thumbnail,
//                        sizeheight = item.sizeheight,
//                        sizeWidth = item.sizewidth
//                    )
//                    context.startActivity(intent)
                }
        )
    }

    fun bindObserver() {
        viewModel.isSuccessSaveSearchItem.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(applicationContext, R.string.save_success_to_db, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.isSuccessDownloadItem.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(applicationContext, R.string.save_success_to_db, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.downloadLate.observe(this) { it ->
            Log.d("download", "$it")
        }
    }
}
