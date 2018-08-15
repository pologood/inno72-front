package com.inno72.system.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_role_function")
public class Inno72RoleFunction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "role_id")
    private String roleId;

    @Column(name = "function_id")
    private String functionId;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return role_id
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * @param roleId
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * @return function_id
     */
    public String getFunctionId() {
        return functionId;
    }

    /**
     * @param functionId
     */
    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }
}