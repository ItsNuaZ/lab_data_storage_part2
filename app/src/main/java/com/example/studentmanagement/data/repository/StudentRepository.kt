package com.example.studentmanagement.data.repository

import com.example.studentmanagement.data.local.StudentDao
import com.example.studentmanagement.data.remote.StudentApi
import com.example.studentmanagement.data.remote.StudentCreateRequest
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

    suspend fun addStudent(studentCode: String, name: String, major: String) {
        val created = api.createStudent(
            StudentCreateRequest(
                student_code = studentCode,
                name = name,
                major = major
            )
        )
        dao.insertStudent(
            Student(
                id = created.id,
                studentCode = created.student_code,
                name = created.name,
                major = created.major
            )
        )
    }

    suspend fun updateStudent(student: Student) {
        api.updateStudent(
            student.id,
            StudentCreateRequest(
                student_code = student.studentCode,
                name = student.name,
                major = student.major
            )
        )
        dao.updateStudent(student)
    }

    suspend fun deleteStudent(id: Int) {
        api.deleteStudent(id)
        dao.deleteById(id)
    }
}