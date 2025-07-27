package com.petcare.petowner.controller;

import com.petcare.petowner.dto.UserRequestDTO;
import com.petcare.petowner.dto.UserResponseDTO;
import com.petcare.petowner.exception.Sha256Exception;
import com.petcare.petowner.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints for managing users")
@SuppressWarnings("unused")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create a new user", description = "Registers a new user with provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @SuppressWarnings("unused")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO userRequestDto) throws Sha256Exception{
        var created = userService.createUser(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update an existing user", description = "Updates the user with the given ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    @SuppressWarnings("unused")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody @Valid UserRequestDTO userRequestDto) throws Sha256Exception {
        var updated = userService.updateUser(id, userRequestDto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Mark user as deceased", description = "Marks a user as deceased using the user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User marked as deceased"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/death")
    @SuppressWarnings("unused")
    public ResponseEntity<Void> markUserAsDeceased(@PathVariable Long id) {
        userService.markUserAsDeceased(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get users by name and first name", description = "Retrieves users matching both name and first name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping("/by-name")
    @SuppressWarnings("unused")
    public ResponseEntity<List<UserResponseDTO>> getByNameAndFirstName(@RequestParam String name, @RequestParam String firstName) {
        return ResponseEntity.ok(userService.getUsersByNameAndFirstName(name, firstName));
    }

    @Operation(summary = "Get users by pet type and city", description = "Returns users based on pet type and residing city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping("/by-pet")
    @SuppressWarnings("unused")
    public ResponseEntity<List<UserResponseDTO>> getUsersByPetTypeAndCity(
            @RequestParam String type,
            @RequestParam String city) {
        return ResponseEntity.ok(userService.getUsersByPetTypeAndCity(type, city));
    }
}
