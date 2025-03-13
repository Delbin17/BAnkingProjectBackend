package com.Bank.DriftBank.repository;

import com.Bank.DriftBank.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<Users ,Long> {


    Boolean existsByEmail(String email);

    boolean existsByAccountNumber(String accountNumber);

    Users findByAccountNumber(String accountNumber);

//    @Query("SELECT u FROM Users u WHERE u.accountNumber = :accountNumber")
//    Users findByAccountNumber(@Param("accountNumber") String accountNumber);
}
