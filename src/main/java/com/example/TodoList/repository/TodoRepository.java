package com.example.TodoList.repository;

import com.example.TodoList.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // Find all todos for a specific user with pagination and sorting
    Page<Todo> findByOwner(String owner, Pageable pageable);

    // Find todos by completion status
    Page<Todo> findByOwnerAndCompleted(String owner, boolean completed, Pageable pageable);

    // Find todos due between two dates
    Page<Todo> findByOwnerAndDueDateBetween(
            String owner, LocalDate start, LocalDate end, Pageable pageable);

    // Find overdue todos (due date before today)
    Page<Todo> findByOwnerAndDueDateBeforeAndCompletedFalse(
            String owner, LocalDate date, Pageable pageable);
}