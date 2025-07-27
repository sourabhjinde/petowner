package com.petcare.petowner.controller;

import com.petcare.petowner.dto.PetRequestDTO;
import com.petcare.petowner.dto.PetResponseDTO;
import com.petcare.petowner.entity.Pet;
import com.petcare.petowner.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
@Tag(name = "Pets", description = "Operations related to pet management")
public class PetController {

    private final PetService petService;

    @Operation(summary = "Create a new pet", description = "Creates a new pet and assigns owners by their IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pet created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<PetResponseDTO> createPet(@RequestBody @Valid PetRequestDTO petRequestDTO) {
        var created = petService.createPet(petRequestDTO, petRequestDTO.ownerIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update a pet", description = "Updates an existing pet by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet updated successfully"),
            @ApiResponse(responseCode = "404", description = "Pet not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDTO> updatePet(@PathVariable Long id, @RequestBody @Valid PetRequestDTO petRequestDTO) {
        var updated = petService.updatePet(id, petRequestDTO);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Mark pet as deceased", description = "Marks a pet as deceased using its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pet marked as deceased"),
            @ApiResponse(responseCode = "404", description = "Pet not found")
    })
    @PatchMapping("/{id}/death")
    public ResponseEntity<Void> markPetAsDeceased(@PathVariable Long id) {
        petService.markPetAsDeceased(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get pets by user ID", description = "Fetches all pets owned by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of pets retrieved"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<PetResponseDTO>> getPetsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(petService.getPetsByUserId(userId));
    }

    @Operation(summary = "Get pets by city", description = "Fetches all pets associated with users from a specific city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of pets retrieved")
    })
        @GetMapping("/by-city")
    public ResponseEntity<List<PetResponseDTO>> getPetsByCity(@RequestParam String city) {
        return ResponseEntity.ok(petService.getPetsByCity(city));
    }

    @Operation(summary = "Get pets owned by women in a city", description = "Returns pets owned by female users in a specific city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of pets retrieved")
    })
    @GetMapping("/women-owned-pets/by-city")
    public ResponseEntity<List<PetResponseDTO>> getWomenOwnedPetsByCity(@RequestParam String city) {
        return ResponseEntity.ok(petService.findPetsByFemaleOwnersInCity(city));
    }

    @Operation(summary = "Get pets owned by a user", description = "Returns pets owned by user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of pets retrieved")
    })
    @GetMapping("/by-user")
    public ResponseEntity<List<PetResponseDTO>> getPetsOwnedByUser(@RequestParam String name, @RequestParam String firstName) {
        return ResponseEntity.ok(petService.findPetsOwnedByUser(name, firstName));
    }

    // DTO to handle pet + ownerIds
    public record PetRequest(Pet pet, Set<Long> ownerIds) {}
}

