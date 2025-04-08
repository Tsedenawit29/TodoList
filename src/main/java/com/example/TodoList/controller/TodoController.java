package com.example.TodoList.controller;

import com.example.TodoList.entity.Todo;
import com.example.TodoList.repository.TodoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // Create a new todo
    @PostMapping
    public ResponseEntity<Void> createTodo(
            @RequestBody Todo newTodo,
            @AuthenticationPrincipal UserDetails user) {

        newTodo.setOwner(user.getUsername());
        Todo savedTodo = todoRepository.save(newTodo);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTodo.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // Get a single todo by ID
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodo(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user) {

        Optional<Todo> todo = todoRepository.findById(id);

        if (todo.isEmpty() || !todo.get().getOwner().equals(user.getUsername())) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(todo.get());
    }

    // Get all todos with pagination and sorting
    @GetMapping
    public ResponseEntity<Page<Todo>> getAllTodos(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dueDate,asc") String[] sort) {

        // Create sort object
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Sort sorting = Sort.by(direction, sort[0]);

        Page<Todo> todos = todoRepository.findByOwner(
                user.getUsername(),
                PageRequest.of(page, size, sorting));

        return ResponseEntity.ok(todos);
    }

    // Get todos by completion status
    @GetMapping("/completed/{status}")
    public ResponseEntity<Page<Todo>> getTodosByCompletion(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable boolean status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Todo> todos = todoRepository.findByOwnerAndCompleted(
                user.getUsername(),
                status,
                PageRequest.of(page, size));

        return ResponseEntity.ok(todos);
    }

    // Get todos due between dates
    @GetMapping("/due")
    public ResponseEntity<Page<Todo>> getTodosDueBetween(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (start == null) start = LocalDate.MIN;
        if (end == null) end = LocalDate.MAX;

        Page<Todo> todos = todoRepository.findByOwnerAndDueDateBetween(
                user.getUsername(),
                start,
                end,
                PageRequest.of(page, size));

        return ResponseEntity.ok(todos);
    }

    // Update a todo
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(
            @PathVariable Long id,
            @RequestBody Todo updatedTodo,
            @AuthenticationPrincipal UserDetails user) {

        Optional<Todo> existingTodo = todoRepository.findById(id);

        if (existingTodo.isEmpty() || !existingTodo.get().getOwner().equals(user.getUsername())) {
            return ResponseEntity.notFound().build();
        }

        Todo todo = existingTodo.get();
        todo.setTitle(updatedTodo.getTitle());
        todo.setDescription(updatedTodo.getDescription());
        todo.setCompleted(updatedTodo.isCompleted());
        todo.setDueDate(updatedTodo.getDueDate());

        Todo savedTodo = todoRepository.save(todo);
        return ResponseEntity.ok(savedTodo);
    }

    // Delete a todo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user) {

        Optional<Todo> todo = todoRepository.findById(id);

        if (todo.isEmpty() || !todo.get().getOwner().equals(user.getUsername())) {
            return ResponseEntity.notFound().build();
        }

        todoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}