package model.interfaces;

import model.enums.UserRole;

public interface InterfaceUser {
    // getters
    int getId();
    String getName();
    UserRole getRole();
    String getPassword();
    // setters
    void setPassword(String password);
    void setRole(UserRole role);
    void setName(String name);
    // methods
    void doSomething(String action);


}
