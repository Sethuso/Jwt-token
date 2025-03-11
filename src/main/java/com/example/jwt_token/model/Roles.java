package com.example.jwt_token.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "roles")
public class Roles {
    @Id
    private String roleId;
    private String roleName;
    private Boolean addAdmin;
    private Boolean addEmployee;
    private Boolean addManager;
    private Boolean isActive = true;
    private Boolean deleteFlag = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String createdBy ;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Boolean getAddAdmin() {
        return addAdmin;
    }

    public void setAddAdmin(Boolean addAdmin) {
        this.addAdmin = addAdmin;
    }

    public Boolean getAddEmployee() {
        return addEmployee;
    }

    public void setAddEmployee(Boolean addEmployee) {
        this.addEmployee = addEmployee;
    }

    public Boolean getAddManager() {
        return addManager;
    }

    public void setAddManager(Boolean addManager) {
        this.addManager = addManager;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
