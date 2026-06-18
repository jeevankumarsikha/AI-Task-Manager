package com.aitaskmanager.controller;

import com.aitaskmanager.dto.TaskRequest;
import com.aitaskmanager.dto.TaskResponse;
import com.aitaskmanager.entity.TaskStatus;
import com.aitaskmanager.service.TaskService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.create(request, user.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAll(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(taskService.getAll(user.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(taskService.getById(id, user.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(taskService.update(id, request, user.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user) {
        taskService.delete(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }

    // GET /api/tasks/search?status=TODO
    @GetMapping("/search")
    public ResponseEntity<List<TaskResponse>> searchByStatus(
            @RequestParam TaskStatus status,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(taskService.searchByStatus(status, user.getUsername()));
    }
}
