package com.example.demo.Model;

public class User {
    
   private  long id;
   private String name;
   private String surname;
   private String email;
   private String username;
   private int password;
   private String base64;
   private Boolean state;
   
   public User(){}

   public User(Long id, String name,String surname,String email,String username,int password,String base64,Boolean state){
    this.id = id;
    this.name=name;
    this.surname=surname;
    this.email=email;
    this.username=username;
    this.password=password;
    this.base64=base64;
    this.state = state;
}
public Boolean getState() {
    return state;
}
public void setState(Boolean state) {
    this.state = state;
}
public String getBase64() {
    return base64;
}

public void setBase64(String base64) {
    this.base64 = base64;
}

public long getId() {
    return id;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getSurname() {
    return surname;
}

public void setSurname(String surname) {
    this.surname = surname;
}

public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}

public String getUserName() {
    return username;
}

public void setUserName(String username) {
    this.username = username;
}

public int getPassword() {
    return password;
}

public void setPassword(int password) {
    this.password = password;
}

public void setId(Long id){
    this.id = id;
}


    
}
