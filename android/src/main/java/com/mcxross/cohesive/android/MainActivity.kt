package com.mcxross.cohesive.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.mcxross.cohesive.common.App
import com.mcxross.cohesive.common.ui.common.AppTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colors = AppTheme.getColors()) {

                Surface(modifier = Modifier.fillMaxSize()) {
                    App()
                }

            }
        }
    }
}