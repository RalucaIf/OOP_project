package model;

import model.enums.UserRole;

public class AdminUser extends User{
    public AdminUser(int id, String name, String password){
        super(id, name, password, UserRole.ADMIN);
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public void doSomething(String action){
        System.out.println("Admin " + getName() + " is performing action: " + action);
    }
    @Override
    public String toString(){
        return "Admin " + getName() + " is managing now. " ;
    }
}
