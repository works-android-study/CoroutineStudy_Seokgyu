package com.example.searchimagecoroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
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
                        ImageView(link = it.link)
                    }
                }
            })
    }

    @Composable
    fun ImageView(link: String) {
        GlideImage(
            imageModel = link,
            modifier = Modifier.size(128.dp)
        )
    }
}
