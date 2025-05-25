package model;

import model.enums.UserRole;
import model.interfaces.InterfaceUser;

public class HackerUser implements InterfaceUser {
    private final int id;
    private String nickname;
    private UserRole role;
    private String attackMethod;
    private String password;

    public HackerUser(int id, String nickname, String attackMethod, String password){
        this.id = id;
        this.nickname = nickname;
        this.role = UserRole.HACKER;
        this.attackMethod = attackMethod;
        this.password = password;
    }

    // getters
    @Override
    public int getId() {
        return id;
    }
    @Override
    public String getName() {
        return nickname;
    }
    @Override
    public UserRole getRole() {
        return role;
    }

    @Override
    public String getPassword() {
        return "";
    }

    public String getAttackMethod() {
        return attackMethod;
    }
    // setters
    @Override
    public void setName(String nickname){
        this.nickname = nickname;
    }
    @Override
    public void setRole(UserRole role){
        this.role = role;
    }
    @Override
    public void setPassword(String password){
        this.password = password;
    }
    @Override
    public void doSomething(String action){
        System.out.println("Hacker " + getName() + " is performing action: " + action);
    }
    public void setAttackMethod(String attackMethod) {
        this.attackMethod = attackMethod;
    }
    // methods
    public void attackNetwork(String network){
        System.out.println("Hacker " + getName() + " is attacking network: " + network + " using " + getAttackMethod());
    }
}
