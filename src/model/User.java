package model;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String fullName;
    private String username;
    private String password;
    private String position;
    private String tel;
    private String email;
    private String address;

    public User() {
    }

    public User(int id, String fullName, String username, String password, String position) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.position = position;
    }

    public User(int id, String fullName, String username, String password, String position,
                String tel, String email, String address) {
        this(id, fullName, username, password, position);
        this.tel = tel;
        this.email = email;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public int getID() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getFullname() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setFullname(String fullname) {
        this.fullName = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
