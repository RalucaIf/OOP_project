package model;

import model.enums.UserRole;

public class RegularUser extends User {
    public RegularUser(int id, String name, String password){
        super(id, name, password, UserRole.REGULAR_USER);
    }
    @Override
    public void doSomething(String action){
        System.out.println("Regular user " + getName() + " is performing action: " + action);
    }
    @Override
    public String toString(){
        return "Regular user " + getName() + " is managing now. " ;
    }
}
