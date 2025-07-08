package com.sdc.controller;

import com.sdc.entity.Projects;
import com.sdc.models.ProjectModel;
import com.sdc.services.ProjectService;
import com.sdc.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/projects")
public class projectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> uploadProject(@ModelAttribute ProjectModel model) {
        try {
            Projects saved = projectService.saveProject(model);
            return ResponseEntity.ok(new ApiResponse(true, "Project saved", convertToResponse(saved)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Project not saved", null));
        }
    }

    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> updateProjectWithImage(
            @PathVariable Integer id,
            @ModelAttribute ProjectModel model) {

        try {
            return projectService.updateProjectWithImage(id, model)
                    .map(updated -> ResponseEntity.ok(new ApiResponse(true, "Project updated", convertToResponse(updated))))
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
        return ResponseEntity.ok("Project deleted successfully");
    }

    @GetMapping("/allproject")
    public ResponseEntity<ApiResponse> getAllProjects() {
        List<Projects> projects = projectService.getAllProjects();

        List<Object> modified = projects.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse(true, "Projects fetched successfully", modified));
    }

    // ✅ Convert Project + Team Members with Base64 image
    private Map<String, Object> convertToResponse(Projects project) {
        Map<String, Object> map = new HashMap<>();
        map.put("projectID", project.getProjectID());
        map.put("title", project.getTitle());
        map.put("description", project.getDescription());
        map.put("link", project.getLink());

        // Project image as base64
        if (project.getImage() != null && project.getImage().length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(project.getImage());
            map.put("image", "data:image/jpeg;base64," + base64Image); // or image/png
        } else {
            map.put("image", null);
        }

        // Team Members with image
        map.put("teamMembers", project.getTeamMembers().stream().map(member -> {
            Map<String, Object> tm = new HashMap<>();
            tm.put("memberId", member.getMemberId());
            tm.put("name", member.getName());
            tm.put("branch", member.getBranch());
            tm.put("position", member.getPosition());
            tm.put("linkdin_url", member.getLinkdin_url());
            tm.put("github_url", member.getGithub_url());
            tm.put("insta_url", member.getInsta_url());

            // Add Base64-encoded image of team member
            if (member.getImage() != null && member.getImage().length > 0) {
                String base64Image = Base64.getEncoder().encodeToString(member.getImage());
                tm.put("image", "data:image/jpeg;base64," + base64Image);
            } else {
                tm.put("image", null);
            }

            return tm;
        }).collect(Collectors.toList()));

        return map;
    }
}
