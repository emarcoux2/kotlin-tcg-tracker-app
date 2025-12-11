package com.example.tcgtracker
//
//import android.content.Context
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.ui.Modifier
//import com.example.tcgtracker.ui.screens.SignInScreen
//import com.example.tcgtracker.ui.theme.TCGTrackerTheme
//
//class SignInActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            TCGTrackerTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    val context: Context = applicationContext
//                    SignInScreen(context = context, modifier = Modifier.padding(innerPadding))
//                }
//            }
//        }
//    }
//}