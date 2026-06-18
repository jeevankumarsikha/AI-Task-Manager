package com.aitaskmanager.repository;

import com.aitaskmanager.entity.Task;
import com.aitaskmanager.entity.TaskStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByOwner(String owner);

    Optional<Task> findByIdAndOwner(Long id, String owner);

    List<Task> findByOwnerAndStatus(String owner, TaskStatus status);
}
