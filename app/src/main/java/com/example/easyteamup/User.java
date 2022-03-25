package com.example.easyteamup;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class User implements Serializable {

    private int userID;
    private String name;
    private String email;
    private String phone;
    private String username;
    private String password;
    private String profilePic;
    private String otherInfo;
    private int[] events;

    public User() {

    }
    // constructor for temp user for logging in
    public User(String username, String password) {
        this.username = username;
        this.password = hash(password);
    }

    // constructor for basic user with minimal info (sign up)
    public User(String name, String email, String username, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.phone = null;
        this.profilePic = null;
        this.otherInfo = null;
        this.password = hash(password);
    }

    // constructor for user from database info
    public User(String name, String email, String phone, String username,
                String profilePic, String otherInfo) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.phone = phone.length() == 0 ? null : phone;
        this.profilePic = profilePic.length() == 0 ? null : profilePic;
        this.otherInfo = otherInfo;
    }

    public void setUserID(int userID) { this.userID = userID; }


    public int getUserID() { return userID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getProfilePic() { return profilePic; }
    public String getOtherInfo() { return otherInfo; }
    public int[] getEvents() { return events; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = hash(password); }
    public void setProfilePic(String profilePic) { this.profilePic = profilePic; }
    public void setOtherInfo(String otherInfo) { this.otherInfo = otherInfo; }

    public static String hash(String password) {
        String hashedPass = null;
        try {
            SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            rand.nextBytes(salt);
            String saltStr = salt.toString();

            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(saltStr.getBytes());
            byte[] bytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            hashedPass = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPass;
    }

}
