package com.example.searchimagecoroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: SearchImageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                SearchAppBar()
                ImageView()
            }
        }
    }

    @Composable
    private fun SearchAppBar() {
        var query = remember { mutableStateOf(TextFieldValue("")) }

        TextField(
            value = query.value,
            onValueChange = { onQueryChanged ->
                query.value = onQueryChanged
            },
            leadingIcon = {
                IconButton(onClick = {
                    viewModel.fetchSearchImage(query.value.text)
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
    fun ImageView() {
        GlideImage(imageModel = viewModel.firstImageItem.observeAsState().value?.link)
    }
}
