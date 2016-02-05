package com.example.anna.ketoapp2;

import android.graphics.Bitmap;

/**
 * Created by Anna on 29/01/2016.
 */
public class User {
    private String username;
    private String password;
    private String dateOfBirth;
    private String insulinRegiment;
    private int usage;
    private Bitmap profilePic;

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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getInsulinRegiment() {
        return insulinRegiment;
    }

    public void setInsulinRegiment(String insulinRegiment) {
        this.insulinRegiment = insulinRegiment;
    }

    public int getUsage() {
        return usage;
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }

    public Bitmap getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }
}
