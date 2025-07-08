package com.sdc.controller;

import com.sdc.entity.Projects;
import com.sdc.models.ProjectModel;
import com.sdc.services.ProjectService;
import com.sdc.utils.ApiResponse;
import jakarta.servlet.ServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class projectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping(value = "/upload",  consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> uploadProject(@ModelAttribute ProjectModel model) {

        try {
            Projects saved = projectService.saveProject(model);
            
            return ResponseEntity.ok(new ApiResponse(true,"project saved",saved));
        } 
        catch (Exception e) {
        	System.err.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false,"project not saved",null));
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getProjectImage(@PathVariable Integer id) {
        Projects project = projectService.getProjectById(id);

        if (project == null || project.getImage() == null) {
            return ResponseEntity.notFound().build();
        }

        // You can customize this MIME type based on file type if needed
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG) // or IMAGE_PNG, etc.
                .body(project.getImage());
    }
    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> updateProjectWithImage(
            @PathVariable Integer id,
            @ModelAttribute ProjectModel model) {

        try {
            return projectService.updateProjectWithImage(id, model)
                    .map(updated -> ResponseEntity.ok(new ApiResponse(true, "Project updated successfully", updated)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse(false, "Project not found", null)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error updating project", null));
        }
    }

    @DeleteMapping("/deleteproject/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Integer id) {
        projectService.deleteProjectById(id);
        return ResponseEntity.ok("project deleted successfully");
    }
//    @PutMapping("/update")
//    public ResponseEntity<String> updateProject(@RequestBody Projects project) {
//       String res= projectService.updateProject(project);
//        return ResponseEntity.ok(res);
//    }
    @GetMapping("/allproject")
    public ResponseEntity<ApiResponse> getAllProjects() {
        List<Projects> projects = projectService.getAllProjects();

        // Debug log to confirm data is loaded
        for (Projects project : projects) {
            System.out.println("Project: " + project.getTitle());
            if (project.getTeamMembers() != null) {
                System.out.println("Members count: " + project.getTeamMembers().size());
                project.getTeamMembers().forEach(member ->
                    System.out.println(" - " + member.getName()));
            } else {
                System.out.println("No members");
            }
        }

        return ResponseEntity.ok(new ApiResponse(true, "Projects fetched successfully", projects));
    }

}
