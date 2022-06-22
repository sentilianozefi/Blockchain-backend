package com.example.demo.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Base64.Decoder;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.Model.User;

@Service
public class UserServiceImp implements UserService {
    Connection c;

    public UserServiceImp() throws SQLException {
        c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "Sentiliano07");

    }

    @Override
    public void createUser(String name, String surname, String email, String username, String password) {
        Statement statement2;
        try {
            statement2 = c.createStatement();
            ResultSet rs2 = statement2.executeQuery("SELECT MAX (id) FROM users");
            while (rs2.next()) {
                User user = new User(rs2.getLong("max") + 1, name, surname, email, username.toLowerCase(),
                        password.hashCode(), null,false);

                try {
                    Statement statement = c.createStatement();
                    String query1 = "SELECT username FROM users WHERE username = '" + user.getUserName() + "'";
                    ResultSet rs = statement.executeQuery(query1);
                    if (rs.next() == false) {
                        Pattern pattern = Pattern
                                .compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
                        Matcher matcher = pattern.matcher(password);
                        boolean isStringContainsSpecialCharacter = matcher.find();
                        if (isStringContainsSpecialCharacter == true) {
                            Pattern pattern2 = Pattern.compile(
                                    "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
                            Matcher matcher2 = pattern2.matcher(email);
                            boolean isStringContainsSpecialCharacter2 = matcher2.find();
                            String query3 = "SELECT email FROM users WHERE email = '" + user.getEmail() + "'";
                            ResultSet rs3 = statement.executeQuery(query3);
                            if (isStringContainsSpecialCharacter2 == true) {

                                if (rs3.next() == false) {

                                    String query2 = "INSERT INTO users VALUES (" + user.getId() + ", '" + user.getName()
                                            + "', '"
                                            + user.getSurname() + "', '" + user.getEmail().toLowerCase() + "','"
                                            + user.getUserName()
                                            + "',"
                                            + user.getPassword() + ")";
                                    statement.executeUpdate(query2);
                                } else {
                                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                            " Email already In use");

                                }
                            } else {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "Email Doesn't exist");
                            }

                        } else {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                    "Password doesn't match the requirements");
                        }
                    } else {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Existing username");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

    }

    @Override
    public void deleteUser(String username, String password) {
        try {
            Statement statement = c.createStatement();
            String query1 = "SELECT username FROM users WHERE username = '" + username.toLowerCase() + "'"
                    + "AND password = " + password.hashCode();
            ResultSet rs = statement.executeQuery(query1);
            if (rs.next() == true) {
                String query2 = "DELETE FROM users WHERE username= '" + username.toLowerCase() + "' AND password = "
                        + password.hashCode();
                statement.executeUpdate(query2);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public User getUser(String username, String password) {
        Statement statement;
        User user = new User();

        try {
            statement = c.createStatement();
            String query1 = "SELECT * FROM users WHERE username = '" + username.toLowerCase() + "'" + "AND password = "
                    + password.hashCode();
            ResultSet rs = statement.executeQuery(query1);
            if (rs.next()) {
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
                user.setEmail(rs.getString("email"));
                user.setUserName(rs.getString("username"));
                user.setPassword(rs.getInt("password"));
                user.setState(rs.getBoolean("state"));
                try {

                    InputStream input = rs.getBinaryStream("image");
                    if (input != null) {

                        byte[] imagebytes = input.readAllBytes();

                        input.read(imagebytes, 0, imagebytes.length);

                        input.close();

                        String encode = Base64.getEncoder().encodeToString(imagebytes);

                        user.setBase64(encode);
                    } else {
                        user.setBase64(null);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return user;
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void updateUserProfileData(String username, String name, String surname, String email) {
        try {
            Statement statement = c.createStatement();
            String query1 = "UPDATE users SET name='" + name + "',  surname='" + surname + "', email='"
                    + email.toLowerCase()
                    + "' WHERE username='" + username.toLowerCase() + "'";
            statement.executeUpdate(query1);
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    @Override
    public void changePassword(String username, String old_password, String new_password) {
        Pattern pattern = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
        Matcher matcher = pattern.matcher(new_password);
        boolean isStringContainsSpecialCharacter = matcher.find();
        if (isStringContainsSpecialCharacter == true) {
            try {
                Statement statement = c.createStatement();
                String query1 = "UPDATE users SET password= '" + new_password.hashCode() + "' WHERE username='"
                        + username.toLowerCase() + "' AND password=" + old_password.hashCode();
                statement.executeUpdate(query1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New Password doesn't match the requirements");
        }
    }

    @Override
    public void setpic(String path, String username) throws SerialException, SQLException {
        Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(path);
        InputStream imageStream = new ByteArrayInputStream(bytes);

        Statement statement = c.createStatement();
        ResultSet rs = statement
                .executeQuery("SELECT username FROM users where username= '" + username.toLowerCase() + "'");

        if (rs.next()) {
            PreparedStatement st = c.prepareStatement("UPDATE users set image = ?  WHERE username= ?");
            st.setBinaryStream(1, imageStream);
            st.setString(2, username);
            st.executeUpdate();

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username Doesn't exist");
        }

    }

    @Override
    public String generateToken(String username) throws SQLException {
        String token = "";
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery("SELECT username FROM users WHERE username='" + username.toLowerCase() + "'");
        if (rs.next()) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = Calendar.getInstance().getTime();
            date = DateUtils.addMinutes(date, 5); // add minute
            String strDate = dateFormat.format(date);
            final SecureRandom secureRandom = new SecureRandom(); // threadsafe
            final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); // threadsafe
            byte[] randomBytes = new byte[24];
            secureRandom.nextBytes(randomBytes);
            token = base64Encoder.encodeToString(randomBytes);// generated token
            PreparedStatement statement = c
                    .prepareStatement("UPDATE users SET passrestoken=? , expirationdate=? WHERE username=?");// insert
                                                                                                             // generated
                                                                                                             // token
                                                                                                             // into
                                                                                                             // database
            statement.setString(1, token);
            statement.setString(2, strDate);
            statement.setString(3, username);
            statement.executeUpdate();

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username doesn't exist");
        }
        Statement st2 = c.createStatement();
        ResultSet rs2 = st2
                .executeQuery("SELECT passrestoken , email FROM users WHERE username='" + username.toLowerCase() + "'");
        String email = "";
        if (rs2.next()) {
            email = rs2.getString("email");
            token = rs2.getString("passrestoken");
        }
        // Recipient's email ID needs to be mentioned.
        String to = email;

        // Sender's email ID needs to be mentioned
        String from = "nsiproject@outlook.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "outlook.office365.com");
        properties.put("mail.smtp.port", "587");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(from, "apaimadh123");

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Your password recovery link is here!");

            // Now set the actual message
            message.setText("Hello " + username
                    + "!  We are sorry you had problems logging in . Here is your password recovery link http://192.168.70.108:3000/respas/"
                    + token);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
        return token;
    }


    @Override
    public void resetPass(String token, String newPassword) throws SQLException, ParseException {
        // Date currentdate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
        // DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date currentdate = Calendar.getInstance().getTime();
        String expdate1 = "";
        Statement st1 = c.createStatement();
        ResultSet rs = st1.executeQuery("SELECT * FROM users where passrestoken='" + token + "'");
        if (rs.next()) {
            expdate1 = rs.getString("expirationdate");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old request");
        }
        Date expdate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(expdate1);

        if (currentdate.before(expdate)) {
            Pattern pattern = Pattern
                    .compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
            Matcher matcher = pattern.matcher(newPassword);
            boolean isStringContainsSpecialCharacter = matcher.find();
            if (isStringContainsSpecialCharacter == true) {
                Statement st2 = c.createStatement();
                st2.executeUpdate("Update users SET password =" + newPassword.hashCode() + " WHERE passrestoken ='"
                        + token + "'");
                Statement st3 = c.createStatement();
                st3.executeUpdate(
                        "Update users SET passrestoken=null , expirationdate=null WHERE passrestoken='" + token + "'");

            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is too weak");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired");
        }

    }

    @Override

    public Collection<User> getallUsers() {

        AtomicInteger atomic = new AtomicInteger();

        
        Map<Integer, User> usernames = new HashMap<>();
        
        try {
            
            Statement st = c.createStatement();
            
            ResultSet rs = st.executeQuery("SELECT * FROM users");
            
            while (rs.next()) {
                User user = new User();

                user.setName(rs.getString("name"));

                user.setSurname(rs.getString("surname"));

               user.setEmail(rs.getString("email"));

               user.setUserName(rs.getString("username"));

               user.setState(rs.getBoolean("state"));

                usernames.put(atomic.incrementAndGet(), user);

            }

        } catch (SQLException e) {



            e.printStackTrace();

        }

        return usernames.values();

    }

    @Override
    public void adminChangepass(String adminUsername, String userUsername, String newpass) throws SQLException {
        PreparedStatement st = c.prepareStatement("SELECT * from admins where username=?");
        st.setString(1, adminUsername);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            PreparedStatement st3 = c.prepareStatement("SELECT * from users where username=?");
            st3.setString(1, userUsername.toLowerCase());
            ResultSet rs3 = st3.executeQuery();
            if (rs3.next()) {

                PreparedStatement st2 = c.prepareStatement("UPDATE users SET password=? WHERE username=?");
                st2.setLong(1, newpass.hashCode());
                st2.setString(2, userUsername);
                st2.executeUpdate();
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist");
            }

        } else {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin account doesn't exist");
        }

    }

    @Override
    public void adminDisableUser(String adminUsername, String userUsername,String userstate) throws SQLException {
        PreparedStatement st = c.prepareStatement("SELECT * from admins where username=?");
        st.setString(1, adminUsername);
        ResultSet rs = st.executeQuery();
        if (rs.next() == true) {
            PreparedStatement st3 = c.prepareStatement("SELECT * from users where username=?");
            st3.setString(1, userUsername);
            ResultSet rs3 = st.executeQuery();
            if (rs3.next() == true) {

                PreparedStatement st2 = c.prepareStatement("UPDATE users SET state=? WHERE username=?");
                st2.setBoolean(1, Boolean.parseBoolean(userstate));
                st2.setString(2, userUsername);
                st2.executeUpdate();
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist");
            }

        } else {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin account doesn't exist");
        }
    }

    @Override
    public User getAdmin(String username, String password) throws SQLException {
        User user = new User();
        PreparedStatement st = c.prepareStatement("SELECT * FROM admins WHERE username=? AND password =?");
        st.setString(1, username);
        st.setInt(2, password.hashCode());
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            user.setName(rs.getString("name"));
            user.setSurname(rs.getString("surname"));
            user.setEmail(rs.getString("email"));
            user.setUserName(rs.getString("username"));

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin account doesn't exist");
        }
        return user;
    }

}
