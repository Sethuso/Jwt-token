package com.example.jwt_token.repository;

import com.example.jwt_token.model.Roles;
import com.example.jwt_token.model.User;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {

    @Nullable
    @Query("{'email':?0}")
    Optional<User> findByEmail(String email);

    @Query("{'isActive':true}")
    List<User> findAllUsers();

    @Aggregation(pipeline = {
            "{'$match':{'isActive':true}}",
            "{'$lookup':{'from': 'roles', 'localField': 'roles', 'foreignField': '_id', 'as': 'roleDetails'}}",
            "{'$match':{'roleDetails.roleName':'Employee'}"
    })
    List<User> getAllEmployees();

    @Aggregation(pipeline = {
            "{'$lookup':{'from': 'roles', 'localField': 'roles', 'foreignField': '_id', 'as': 'roleDetails'}}",
            "{'$match':{'roleDetails.roleName':'Admin'}}"
    })
    List<User> getAllAdmins();

    Optional<User> findByVerificationToken(String token);

    @Query("{'email':?0,'isActive':true}")
    Optional<User>  deleteByEmailId(String email);

}
