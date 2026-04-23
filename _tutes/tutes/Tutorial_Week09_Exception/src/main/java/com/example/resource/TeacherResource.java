package com.example.resource;

import com.example.dao.GenericDAO;
import com.example.dao.MockDatabase;
import com.example.exception.DataNotFoundException;
import com.example.model.Teacher;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/teachers")
public class TeacherResource {
    
    private GenericDAO<Teacher> teacherDAO = new GenericDAO<>(MockDatabase.TEACHERS);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Teacher> getAllTeachers() {
        return teacherDAO.getAll();
    }

    @GET
    @Path("/{teacherId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Teacher getTeacherById(@PathParam("teacherId") int teacherId) {
        Teacher teacher = teacherDAO.getById(teacherId);
        if (teacher == null) {
            throw new DataNotFoundException("Teacher with ID " + teacherId + " not found.");
        }
        return teacher;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addTeacher(Teacher teacher) {
        teacherDAO.add(teacher);
    }

    @PUT
    @Path("/{teacherId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateTeacher(@PathParam("teacherId") int teacherId, Teacher updatedTeacher) {
        Teacher existingTeacher = teacherDAO.getById(teacherId);
        if (existingTeacher == null) {
            throw new DataNotFoundException("Teacher with ID " + teacherId + " not found.");
        }
        updatedTeacher.setId(teacherId);
        teacherDAO.update(updatedTeacher);
    }

    @DELETE
    @Path("/{teacherId}")
    public void deleteTeacher(@PathParam("teacherId") int teacherId) {
        Teacher existingTeacher = teacherDAO.getById(teacherId);
        if (existingTeacher == null) {
            throw new DataNotFoundException("Teacher with ID " + teacherId + " not found.");
        }
        teacherDAO.delete(teacherId);
    }
}