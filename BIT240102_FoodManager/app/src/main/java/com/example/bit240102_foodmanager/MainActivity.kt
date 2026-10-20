package com.example.bit240102_foodmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.bit240102_foodmanager.ui.FoodManagerViewModel
import com.example.bit240102_foodmanager.ui.FoodManagerApp

class MainActivity : ComponentActivity() {
    private val BIT240102_ViewModel: FoodManagerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BIT240102_ViewModel.syncIfConfigured()
        setContent {
            FoodManagerApp(BIT240102_ViewModel)
        }
    }
}
