package com.example.studentmanagement.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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

@Composable
fun StudentDetailScreen(
    student: Student,
    onBack: () -> Unit,
    onEdit: (Student) -> Unit,
    onDelete: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var editCode by remember { mutableStateOf(student.studentCode) }
    var editName by remember { mutableStateOf(student.name) }
    var editMajor by remember { mutableStateOf(student.major) }
    var editIsOnline by remember { mutableStateOf(student.isOnline) }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Student") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = editCode,
                        onValueChange = { editCode = it },
                        label = { Text("Student Code") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = editMajor,
                        onValueChange = { editMajor = it },
                        label = { Text("Major") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Online Status")
                        Switch(
                            checked = editIsOnline,
                            onCheckedChange = { editIsOnline = it }
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editCode.isNotBlank() && editName.isNotBlank() && editMajor.isNotBlank()) {
                            val updatedStudent = student.copy(
                                studentCode = editCode,
                                name = editName,
                                major = editMajor,
                                isOnline = editIsOnline
                            )
                            onEdit(updatedStudent)
                            showEditDialog = false
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("Back")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Student Detail",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (student.isOnline) "● Online" else "● Offline",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (student.isOnline) Color(0xFF4CAF50) else Color.Red
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "ID : ${student.id}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Code : ${student.studentCode}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Name : ${student.name}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Major : ${student.major}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            editCode = student.studentCode
                            editName = student.name
                            editMajor = student.major
                            editIsOnline = student.isOnline
                            showEditDialog = true
                        }
                    ) {
                        Text("Edit")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = { onDelete(student.id) },
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
