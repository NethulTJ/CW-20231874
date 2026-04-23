package com.example.resource;

import com.example.dao.GenericDAO;
import com.example.dao.MockDatabase;
import com.example.exception.DataNotFoundException;
import com.example.model.Module;
import java.util.ArrayList;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/modules")
public class ModuleResource {
    
    private GenericDAO<Module> moduleDAO = new GenericDAO<>(MockDatabase.MODULES);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Module> getAllModules() {
        return moduleDAO.getAll();
    }

    @GET
    @Path("/{moduleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Module getModuleById(@PathParam("moduleId") int moduleId) {
        Module module = moduleDAO.getById(moduleId);
        if (module == null) {
            throw new DataNotFoundException("Module with ID " + moduleId + " not found.");
        }
        return module;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addModule(Module module) {
        moduleDAO.add(module);
    }

    @PUT
    @Path("/{moduleId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateModule(@PathParam("moduleId") int moduleId, Module updatedModule) {
        Module existingModule = moduleDAO.getById(moduleId);
        if (existingModule == null) {
            throw new DataNotFoundException("Module with ID " + moduleId + " not found.");
        }
        updatedModule.setId(moduleId);
        moduleDAO.update(updatedModule);
    }

    @DELETE
    @Path("/{moduleId}")
    public void deleteModule(@PathParam("moduleId") int moduleId) {
        Module existingModule = moduleDAO.getById(moduleId);
        if (existingModule == null) {
            throw new DataNotFoundException("Module with ID " + moduleId + " not found.");
        }
        moduleDAO.delete(moduleId);
    }

    @GET
    @Path("/teachers/{teacherId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Module> getModulesByTeacher(@PathParam("teacherId") int teacherId) {
        List<Module> modulesByTeacher = new ArrayList<>();
        List<Module> allModules = moduleDAO.getAll();

        for (Module module : allModules) {
            if (module.getTeacher().getId() == teacherId) {
                modulesByTeacher.add(module);
            }
        }
        
        if (modulesByTeacher.isEmpty()) {
            throw new DataNotFoundException("No modules found for Teacher ID " + teacherId);
        }
        
        return modulesByTeacher;
    }
}