package com.example.jwt_token.serviceImpl;

import com.example.jwt_token.exception.CustomException;
import com.example.jwt_token.exception.ErrorCode;
import com.example.jwt_token.model.Roles;
import com.example.jwt_token.repository.RolesRepository;
import com.example.jwt_token.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolesServiceImpl implements RolesService {
    @Autowired
    private RolesRepository rolesRepository;
    @Override
    public Roles saveRole(Roles roles) throws CustomException {
        if(roles != null){
            roles.setCreatedBy(roles.getRoleName());
            return rolesRepository.save(roles);
        }else{
            throw new CustomException(ErrorCode.EXP_1003);
        }
    }

    @Override
    public List<Roles> getAllRoles() throws CustomException {
       try {
           return rolesRepository.findAll();
       }catch (Exception e){
           throw new CustomException(ErrorCode.EXP_1003);
       }
    }

    @Override
    public Roles getById(String id) {
        return null;
    }

    @Override
    public Roles getByName(String RoleName) {
        return null;
    }

    @Override
    public String deleteByName(String RoleName) {
        return "";
    }

    @Override
    public Roles updateRoles(Roles roles,String roleName) throws CustomException {
       if (roleName != null){
           Optional<Roles> existingId = rolesRepository.findByRoleName(roleName);
           if (existingId.isPresent()){
              Roles updateRole = existingId.get();
              updateRole.setRoleId(updateRole.getRoleId());
              updateRole.setRoleName(updateRole.getRoleName());
              updateRole.setAddAdmin(updateRole.getAddAdmin());
              updateRole.setAddEmployee(updateRole.getAddEmployee());
              updateRole.setIsActive(roles.getIsActive());
              updateRole.setAddManager(updateRole.getAddManager());
              return rolesRepository.save(roles);
           }else {
               throw new CustomException(ErrorCode.EXP_1004);
           }
       }else{
                throw new CustomException(ErrorCode.EXP_1003);
       }
    }
}
