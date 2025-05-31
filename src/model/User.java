package model;

import model.enums.UserRole;

public abstract class User {
    private final int id;
    private final String name;
    private String password;
    private UserRole role;

    public User(int id, String name, String password, UserRole role){
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
    }
    // getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public UserRole getRole() {
        return role;
    }
    public String getPassword() {
        return password;
    }

    // setters
    public void setPassword(String password){
        this.password = password;
    }
    public void setRole(UserRole role){
        this.role = role;
    }

    // methods
    public abstract void doSomething(String action);
}
