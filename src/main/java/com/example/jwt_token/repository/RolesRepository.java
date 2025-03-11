package com.example.jwt_token.repository;

import com.example.jwt_token.model.Roles;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolesRepository extends MongoRepository<Roles,String> {

     Optional<Roles> findByRoleName(String roleName);
     @Query("{'isActive': true}")
     List<Roles> findAll();



}
