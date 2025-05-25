package model;

import model.enums.UserRole;
import model.interfaces.InterfaceUser;

public abstract class User implements InterfaceUser {
    private final int id;
    private String name;
    private String password;
    private UserRole role;

    public User(int id, String name, String password, UserRole role){
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
    }
    // getters
    @Override
    public int getId() {
        return id;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public UserRole getRole() {
        return role;
    }
    // setters
    @Override
    public void setName(String name){
        this.name = name;
    }
    @Override
    public void setPassword(String password){
        this.password = password;
    }
    @Override
    public void setRole(UserRole role){
        this.role = role;
    }
    // methods
    @Override
    public abstract void doSomething(String action);
}
