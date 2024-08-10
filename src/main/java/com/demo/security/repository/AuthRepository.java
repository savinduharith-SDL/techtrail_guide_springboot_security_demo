package com.demo.security.repository;

import com.demo.security.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigInteger;


import java.util.List;

public interface AuthRepository extends JpaRepository<User,Long> {

    User findFirstByEmailAndActiveTrueAndVerifiedTrue(String email);

    User findByEmail(String email);

    List<User> findUserByFirstName(String name);

    Page<User> findAllByRole(String role, Pageable pageable);

    User findUserByIdAndActiveTrue(Long id);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM USERS WHERE ROLE IS NULL")
    int findUserCount();

    @Query(nativeQuery = true, value = "SELECT role, COUNT(*) FROM USERS GROUP BY role")
    List<Object[]> countUsersByRole();

}
