package model;

import model.enums.UserRole;

public class HackerUser extends User{
    public HackerUser(int id, String name, String password){
        super(id, name, password, UserRole.HACKER);
    }
    @Override
    public void doSomething(String action){
        System.out.println("Hacker " + getName() + " is performing action: " + action);
    }
    @Override
    public String toString(){
        return "Hacker " + getName() + " is managing now. " ;
    }
}
