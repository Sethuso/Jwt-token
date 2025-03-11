package com.example.jwt_token.service;

import com.example.jwt_token.exception.CustomException;
import com.example.jwt_token.model.Roles;

import java.util.List;

public interface RolesService {
    public Roles saveRole(Roles roles) throws CustomException;
    public List<Roles> getAllRoles() throws CustomException;
    public Roles getById(String id);
    public Roles getByName(String RoleName);
    public String deleteByName (String RoleName);
    public Roles updateRoles(Roles roles,String roleName) throws CustomException;
}
