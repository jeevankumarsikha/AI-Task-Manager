package com.aitaskmanager.dto;

import com.aitaskmanager.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

    @NotBlank(message = "title is required")
    private String title;

    private String description;

    // Optional on create (defaults to TODO); used on update.
    private TaskStatus status;
}
