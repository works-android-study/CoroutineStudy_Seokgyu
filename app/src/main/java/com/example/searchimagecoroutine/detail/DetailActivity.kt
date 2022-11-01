package com.example.searchimagecoroutine.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage

class DetailActivity: ComponentActivity() {

    lateinit var title: String
    lateinit var thumbnail: String
    var sizeHeight: Int = 0
    var sizeWidth: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseIntent()

        setContent {
            val scrollState = rememberScrollState()
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .verticalScroll(scrollState)
            ) {
                setTitle()
                setDetailImage()
                setSizeHeight()
                setSizeWidth()

            }
        }
    }
    @Composable
    fun setTitle() {
        Text(text = title)
    }

    @Composable
    fun setDetailImage() {
        GlideImage(
            imageModel = thumbnail,
            modifier = Modifier.size(width = sizeWidth.dp, height = sizeHeight.dp)
        )
    }

    @Composable
    fun setSizeHeight() {
        Text(text = sizeHeight.toString())
    }

    @Composable
    fun setSizeWidth() {
        Text(text = sizeWidth.toString())
    }


    private fun parseIntent() {
        title = intent.getStringExtra(TITLE) ?: ""
        thumbnail = intent.getStringExtra(THUMBNAIL) ?: ""
        sizeHeight = intent.getStringExtra(SIZEHEIGHT)?.toInt() ?: 100
        sizeWidth = intent.getStringExtra(SIZEWIDTH)?.toInt() ?: 100
    }

    companion object {
        const val TITLE = "title"
        const val THUMBNAIL = "thumbnail"
        const val SIZEHEIGHT = "sizeheight"
        const val SIZEWIDTH = "sizewidth"
        fun createIntent(context: Context, title: String, thumbnail: String, sizeheight: String, sizeWidth: String): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(TITLE, title)
            intent.putExtra(THUMBNAIL, thumbnail)
            intent.putExtra(SIZEHEIGHT, sizeheight)
            intent.putExtra(SIZEWIDTH, sizeWidth)

            return  intent
        }
    }
}
