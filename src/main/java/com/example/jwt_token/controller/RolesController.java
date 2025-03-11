package com.example.jwt_token.controller;

import com.example.jwt_token.exception.CustomException;
import com.example.jwt_token.model.Roles;
import com.example.jwt_token.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/com/roles")
public class RolesController {
    @Autowired
    private RolesService rolesService;

    @PostMapping("/createRole")
    public ResponseEntity<Roles> CreateRole(@RequestBody Roles roles) throws CustomException {
        return ResponseEntity.ok(rolesService.saveRole(roles));
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<Roles>> getByAll() throws CustomException {
        return ResponseEntity.ok(rolesService.getAllRoles());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Roles> getById(@PathVariable String id){
        return ResponseEntity.ok(rolesService.getById(id));
    }

    @GetMapping("/getById/{roleName}")
    public ResponseEntity<Roles> getByName(@PathVariable String roleName){
        return ResponseEntity.ok(rolesService.getByName(roleName));
    }

    @DeleteMapping("/deleteByRole/{roleName}")
    public ResponseEntity<String> deleteRoleByName(@PathVariable String roleName){
        return ResponseEntity.ok(rolesService.deleteByName(roleName));
    }

    @PutMapping("/updateRole/{roleName}")
    public ResponseEntity<Roles> updateRole(@RequestBody Roles roles,@PathVariable String roleName) throws CustomException{
        return ResponseEntity.ok(rolesService.updateRoles(roles,roleName));
    }


}
