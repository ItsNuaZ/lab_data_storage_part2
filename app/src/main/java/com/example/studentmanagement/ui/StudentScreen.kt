package com.example.studentmanagement.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.studentmanagement.model.Student
import com.example.studentmanagement.viewmodel.StudentViewModel

@Composable
fun StudentScreen(
    viewModel: StudentViewModel,
    modifier: Modifier = Modifier
) {
    var selectedStudent by remember { mutableStateOf<Student?>(null) }
    var showAddForm by remember { mutableStateOf(false) }
    var studentCode by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var major by remember { mutableStateOf("") }

    selectedStudent?.let { currentStudent ->
        StudentDetailScreen(
            student = currentStudent,
            onBack = { selectedStudent = null },
            onEdit = { updatedStudent ->
                viewModel.updateStudent(updatedStudent)
                selectedStudent = updatedStudent
            },
            onDelete = { studentToDelete ->
                viewModel.deleteStudent(studentToDelete)
                selectedStudent = null
            },
            modifier = modifier
        )
        return
    }

    if (showAddForm) {
        AlertDialog(
            onDismissRequest = { showAddForm = false },
            title = {
                Text(
                    text = "Add Student",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = studentCode,
                        onValueChange = { studentCode = it },
                        label = { Text("Student Code") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = major,
                        onValueChange = { major = it },
                        label = { Text("Major") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (studentCode.isNotBlank() && name.isNotBlank() && major.isNotBlank()) {
                            viewModel.addStudent(studentCode, name, major)
                            studentCode = ""
                            name = ""
                            major = ""
                            showAddForm = false
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showAddForm = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (viewModel.isLoading) {
        CircularProgressIndicator(modifier = modifier)
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Student Management",
                style = MaterialTheme.typography.headlineSmall
            )
            viewModel.errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = viewModel.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = { Text("Search student") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(
                    onClick = {
                        viewModel.loadStudents()
                    }
                ) {
                    Text("Load Students")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        showAddForm = true
                    }
                ) {
                    Text("+ Add Student")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.filteredStudents) { student ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedStudent = student },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${student.studentCode} - ${student.name}",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (student.isOnline) "● Online" else "● Offline",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (student.isOnline) Color(0xFF4CAF50) else Color.Red
                                    )
                                }
                                Text(
                                    text = student.major,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Button(
                                onClick = {
                                    viewModel.deleteStudent(student)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red
                                )
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}