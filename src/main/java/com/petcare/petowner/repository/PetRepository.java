package com.petcare.petowner.repository;

import com.petcare.petowner.entity.Pet;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    // Pets owned by a user
    @Query("""
        SELECT p
        FROM Pet p
        JOIN FETCH p.owners o
        WHERE o.id = :userId
          AND p.deceased = false
    """)
    List<Pet> findPetsByUserId(@Param("userId") Long userId);

    // Pets from a specific city
    @Query("""
    SELECT DISTINCT p
    FROM Pet p
    JOIN FETCH p.owners o
    JOIN FETCH o.address a
    WHERE a.city = :city
      AND p.deceased = false
      AND o.deceased = false
    """)
    List<Pet> findPetsByCity(@Param("city") String city);

    @Query("""
    SELECT DISTINCT p
    FROM Pet p
    JOIN FETCH p.owners o
    JOIN FETCH o.address a
    WHERE o.gender = 'FEMALE' AND a.city = :city
    """)
    List<Pet> findPetsByFemaleOwnersInCity(@Param("city") String city);

    @Query("""
    SELECT DISTINCT p
    FROM Pet p
    JOIN FETCH p.owners o
    WHERE LOWER(o.name) = LOWER(:name)
      AND LOWER(o.firstName) = LOWER(:firstName)
      AND p.deceased = false
      AND o.deceased = false
    """)
    List<Pet> findPetsOwnedByUser(@Param("name") String name, @Param("firstName") String firstName);
}

