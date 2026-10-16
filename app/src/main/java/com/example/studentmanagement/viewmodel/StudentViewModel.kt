package com.example.studentmanagement.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentmanagement.data.repository.StudentRepository
import com.example.studentmanagement.model.Student
import kotlinx.coroutines.launch

class StudentViewModel(
    private val repository: StudentRepository
) : ViewModel() {
    var students by mutableStateOf(
        emptyList<Student>()
    )
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var searchQuery by mutableStateOf("")
        private set

    val filteredStudents: List<Student>
        get() = if (searchQuery.isBlank()) {
            students
        } else {
            students.filter { student ->
                student.studentCode.contains(searchQuery, ignoreCase = true) ||
                        student.name.contains(searchQuery, ignoreCase = true) ||
                        student.major.contains(searchQuery, ignoreCase = true)
            }
        }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }
    fun loadStudents() {
        viewModelScope.launch {
            isLoading = true

            try {
                students =
                    repository.getStudents()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun addStudent(studentCode: String, name: String, major: String) {
        viewModelScope.launch {
            try {
                repository.addStudent(studentCode, name, major)
                loadStudents()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Add student failed"
            }
        }
    }

    fun deleteStudent(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteStudent(id)
                loadStudents()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Delete student failed"
            }
        }
    }

    fun updateStudent(student: Student) {
        viewModelScope.launch {
            try {
                repository.updateStudent(student)
                loadStudents()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Update student failed"
            }
        }
    }
}