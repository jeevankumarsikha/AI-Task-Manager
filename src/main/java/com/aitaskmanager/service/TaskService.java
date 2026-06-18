package com.aitaskmanager.service;

import com.aitaskmanager.dto.TaskRequest;
import com.aitaskmanager.dto.TaskResponse;
import com.aitaskmanager.entity.Task;
import com.aitaskmanager.entity.TaskStatus;
import com.aitaskmanager.exception.ResourceNotFoundException;
import com.aitaskmanager.repository.TaskRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponse create(TaskRequest request, String owner) {
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO)
                .createdDate(LocalDateTime.now())
                .owner(owner)
                .build();
        return toResponse(taskRepository.save(task));
    }

    public List<TaskResponse> getAll(String owner) {
        return taskRepository.findByOwner(owner).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse getById(Long id, String owner) {
        Task task = taskRepository.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
        return toResponse(task);
    }

    public TaskResponse update(Long id, TaskRequest request, String owner) {
        Task task = taskRepository.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        return toResponse(taskRepository.save(task));
    }

    public void delete(Long id, String owner) {
        Task task = taskRepository.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
        taskRepository.delete(task);
    }

    public List<TaskResponse> searchByStatus(TaskStatus status, String owner) {
        return taskRepository.findByOwnerAndStatus(owner, status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .createdDate(task.getCreatedDate())
                .build();
    }
}
