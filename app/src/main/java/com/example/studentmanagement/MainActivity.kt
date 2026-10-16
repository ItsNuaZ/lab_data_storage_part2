package com.example.studentmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.example.studentmanagement.data.local.AppDatabase
import com.example.studentmanagement.data.remote.RetrofitClient
import com.example.studentmanagement.data.repository.StudentRepository
import com.example.studentmanagement.ui.StudentScreen
import com.example.studentmanagement.ui.theme.StudentManagementTheme
import com.example.studentmanagement.viewmodel.StudentViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "student_db"
        ).fallbackToDestructiveMigration(true).build()
        val repository = StudentRepository(database.studentDao(), RetrofitClient.api)
        val viewModel = StudentViewModel(repository)

        setContent {
            StudentManagementTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StudentScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    StudentManagementTheme {
//        Greeting("Android")
//    }
//}