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
            val remoteStudents =
                api.getStudents()
            val localStudents =
                remoteStudents.map {
                    Student(
                        id = it.id,
                        studentCode = it.student_code,
                        name = it.name,
                        major = it.major
                    )
                }
            dao.deleteAll()
            dao.insertAll(localStudents)
            localStudents
        } catch (e: Exception) {
            dao.getAllStudents()
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