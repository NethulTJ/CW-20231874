package com.example.resource;

import com.example.model.Module;
import com.example.model.Student;
import com.example.dao.GenericDAO;
import com.example.dao.MockDatabase;
import com.example.exception.DataNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/students")
public class StudentResource {

    private GenericDAO<Student> studentDAO = new GenericDAO<>(MockDatabase.STUDENTS);
    private GenericDAO<Module> moduleDAO = new GenericDAO<>(MockDatabase.MODULES);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> getAllStudents() {
        return studentDAO.getAll();
    }

    @GET
    @Path("/{studentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Student getStudentById(@PathParam("studentId") int studentId) {
        Student student = studentDAO.getById(studentId);
        if (student == null) {
            throw new DataNotFoundException("Student with ID " + studentId + " not found.");
        }
        return student;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addStudent(Student student) {
        studentDAO.add(student);
    }

    @PUT
    @Path("/{studentId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateStudent(@PathParam("studentId") int studentId, Student updatedStudent) {
        Student existingStudent = studentDAO.getById(studentId);
        if (existingStudent == null) {
            throw new DataNotFoundException("Student with ID " + studentId + " not found.");
        }
        updatedStudent.setId(studentId);
        studentDAO.update(updatedStudent);
    }

    @DELETE
    @Path("/{studentId}")
    public void deleteStudent(@PathParam("studentId") int studentId) {
        Student existingStudent = studentDAO.getById(studentId);
        if (existingStudent == null) {
            throw new DataNotFoundException("Student with ID " + studentId + " not found.");
        }
        studentDAO.delete(studentId);
    }

    @GET
    @Path("/{studentId}/modules")
    @Produces(MediaType.APPLICATION_JSON)
    public String getModulesForStudent(@PathParam("studentId") int studentId) {
        Student student = studentDAO.getById(studentId);
        if (student == null) {
            throw new DataNotFoundException("Student with ID " + studentId + " not found.");
        }

        int moduleId = 1; 
        Module selectedModule = moduleDAO.getById(moduleId);

        if (selectedModule != null) {
            return "{\"module\": \"" + selectedModule.getName()
                    + "\", \"teacher\": \"" + selectedModule.getTeacher().getName() + "\"}";
        }
        
        throw new DataNotFoundException("Module with ID " + moduleId + " not found.");
    }
}