package com.example.demo.Controller;
import java.sql.SQLException;
import java.text.ParseException;

import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin (origins ={"http://localhost:3000/","http://192.168.70.108:3000/"})
@RestController
public class UserController {
    @Autowired
UserService userService;

   @RequestMapping(value="/CreateUser/{name}/{surname}/{email}/{username}/{password}" , method = RequestMethod.POST)
    public ResponseEntity<Object> createUser(@PathVariable("name")String name ,@PathVariable("surname") String surname,@PathVariable("email")String email,@PathVariable("username")String username,@PathVariable("password")String password){
        userService.createUser(name,surname,email,username,password);
        return new ResponseEntity<>("User has been created", HttpStatus.CREATED);
    }
    @RequestMapping(value="/GetUser/{username}/{password}" , method = RequestMethod.GET)
    public ResponseEntity<Object> GetUser(@PathVariable("username")String username,@PathVariable("password")String password){
        return new ResponseEntity<>(userService.getUser(username, password), HttpStatus.CREATED);
    }
    @RequestMapping(value ="/DeleteUser/{username}/{password}", method = RequestMethod.DELETE)
public ResponseEntity<Object> deleteUser(@PathVariable("username") String username,@PathVariable("password")String password){
    userService.deleteUser(username,password);
    return new ResponseEntity<>("User has been deleted", HttpStatus.OK);
}
@RequestMapping(value ="/UpdateUserProfile/{username}/{name}/{surname}/{email}", method = RequestMethod.PUT)
public ResponseEntity<Object> updateUser(@PathVariable("username")String username,@PathVariable("name")String name,@PathVariable("surname")String surname,@PathVariable("email")String email){
    userService.updateUserProfileData(username, name, surname, email);
    return new ResponseEntity<>("Updated",HttpStatus.OK);
}
@RequestMapping(value ="/changePassword/{username}/{old_password}/{new_password}", method = RequestMethod.PUT)
public ResponseEntity<Object> updateUserPassword(@PathVariable("username")String username,@PathVariable("old_password")String old_password,@PathVariable("new_password")String new_password){
    userService.changePassword(username,old_password,new_password);
    return new ResponseEntity<>("Updated",HttpStatus.OK);
}


@RequestMapping(value ="/setpic/{username}", method = RequestMethod.POST)
public ResponseEntity<Object> setpic(@RequestBody String path,@PathVariable("username")String username) throws SQLException{
userService.setpic(path,username);
return new ResponseEntity<>("Pic set ", HttpStatus.OK);
}
@RequestMapping(value ="/generatetoken/{username}", method = RequestMethod.GET)
public ResponseEntity<Object> generatetoken(@PathVariable("username")String username) throws SQLException{
return new ResponseEntity<>(userService.generateToken(username), HttpStatus.OK);
}

@RequestMapping(value ="/respas/{token}/{newPassword}", method = RequestMethod.GET)
public ResponseEntity<Object> respas(@PathVariable("token")String token,@PathVariable("newPassword")String newPassword) throws SQLException, ParseException{
    userService.resetPass(token,newPassword);
return new ResponseEntity<>("Password is changed", HttpStatus.OK);
}
@RequestMapping(value="/GetUsers" , method = RequestMethod.GET)
    public ResponseEntity<Object> GetUsers(){
        return new ResponseEntity<>(userService.getallUsers(), HttpStatus.CREATED);
    }
    @RequestMapping(value="/GetAdmin/{username}/{password}" , method = RequestMethod.GET)
    public ResponseEntity<Object> GetAdmin(@PathVariable("username")String username,@PathVariable("password")String password) throws SQLException{
        return new ResponseEntity<>(userService.getAdmin(username, password), HttpStatus.CREATED);
    }
    @RequestMapping(value="/adminchangepass/{adminUsername}/{userUsername}" , method = RequestMethod.POST)
    public ResponseEntity<Object> adminChangepass(@PathVariable("adminUsername")String adminUsername,@PathVariable("userUsername")String userUsername,@RequestBody String newpass) throws SQLException{
        userService.adminChangepass(adminUsername, userUsername,newpass);
        return new ResponseEntity<>("Password for selected user is changed", HttpStatus.CREATED);
    }
    @RequestMapping(value="/admindisableuser/{adminUsername}/{userUsername}/{userstate}" , method = RequestMethod.POST)
    public ResponseEntity<Object> adminDisableUser(@PathVariable("adminUsername")String username,@PathVariable("userUsername")String password,@PathVariable("userstate")String userstate) throws SQLException{
        userService.adminDisableUser(username, password,userstate);
        return new ResponseEntity<>("User has been disabled", HttpStatus.CREATED);
    }
}
