package com.petcare.petowner.repository;

import com.petcare.petowner.entity.User;
import com.petcare.petowner.entity.Gender;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom: Users owning a specific pet type in a specific city
    @Query("""
        SELECT DISTINCT u
        FROM User u
        JOIN u.pets p
        WHERE p.type = :type
          AND u.address.city = :city
          AND u.deceased = false
          AND p.deceased = false
    """)
    List<User> findUsersByPetTypeAndCity(@Param("type") String type, @Param("city") String city);

    // Custom: Users (women) owning pets in a specific city
    @Query("""
        SELECT DISTINCT u
        FROM User u
        JOIN u.pets p
        WHERE u.gender = :gender
          AND u.address.city = :city
          AND u.deceased = false
          AND p.deceased = false
    """)
    List<User> findFemaleUsersWithPetsInCity(@Param("gender") Gender gender, @Param("city") String city);

    List<User> findByNameIgnoreCaseAndFirstNameIgnoreCase(String name, String firstName);

}

