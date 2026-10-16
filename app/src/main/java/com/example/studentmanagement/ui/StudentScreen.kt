package com.example.studentmanagement.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.studentmanagement.viewmodel.StudentViewModel

@Composable
fun StudentScreen(viewModel: StudentViewModel) {
    LaunchedEffect(Unit) { viewModel.loadStudents() }

    Column {
        if (viewModel.isLoading) Text("Loading...")
        viewModel.errorMessage?.let { Text("Error: $it") }

        LazyColumn {
            items(viewModel.students) { student ->
                Text("${student.studentCode} - ${student.name}")
            }
        }
    }
}