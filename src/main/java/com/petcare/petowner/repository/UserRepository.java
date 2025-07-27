package com.petcare.petowner.repository;

import com.petcare.petowner.entity.PetType;
import com.petcare.petowner.entity.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
    SELECT DISTINCT u
    FROM User u
    JOIN u.pets p
    WHERE p.type = :type
      AND u.address.city = :city
      AND u.deceased = false
      AND p.deceased = false
    """)
    List<User> findUsersByPetTypeAndCity(@Param("type") PetType type, @Param("city") String city);

    List<User> findByNameIgnoreCaseAndFirstNameIgnoreCase(String name, String firstName);

    @Query("""
        SELECT COUNT(DISTINCT u.address) = 1
        FROM User u
        WHERE u.id IN :ownerIds
    """)
    boolean allOwnersHaveSameAddress(@Param("ownerIds") Set<Long> ownerIds);

}

