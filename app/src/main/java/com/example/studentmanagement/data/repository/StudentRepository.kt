package com.example.studentmanagement.data.repository

import com.example.studentmanagement.data.local.StudentDao
import com.example.studentmanagement.data.remote.StudentApi
import com.example.studentmanagement.model.Student

class StudentRepository(
    private val dao: StudentDao,
    private val api: StudentApi
) {
    suspend fun getStudents(): List<Student> {
        return try {
            val remoteStudents = api.getStudents()
            remoteStudents.forEach {
                dao.insertStudent(it)
            }
            remoteStudents
        } catch (_: Exception) {
            val localStudents = dao.getAllStudents()
            localStudents.ifEmpty {
                val sampleStudents = listOf(
                    Student(1, "66010001", "Somchai Jaidee", "Computer Science", isOnline = true),
                    Student(2, "66010002", "Somsri Sookjai", "Information Technology", isOnline = false),
                    Student(3, "66010003", "Anan Prasert", "Software Engineering", isOnline = true)
                )
                sampleStudents.forEach {
                    dao.insertStudent(it)
                }
                sampleStudents
            }
        }
    }

    suspend fun addStudent(
        student: Student
    ) {
        dao.insertStudent(student)
    }

    suspend fun deleteStudent(
        student: Student
    ) {
        dao.deleteStudent(student)
    }

    suspend fun updateStudent(
        student: Student
    ) {
        dao.insertStudent(student)
    }
}