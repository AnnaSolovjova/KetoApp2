package com.example.anna.ketoapp2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TabLayout;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Anna on 23/10/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    private final static String DATABASE_NAME="DiabetesApp.db";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table Users( user_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, dateOfBirth TEXT,insulinRegiment TEXT, usage INT)");
            db.execSQL("create table Sessions( username TEXT PRIMARY KEY, insulin INT, iterations INT, screen TEXT");
        }
        catch(SQLException sqle)
        {

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Users" );
        db.execSQL("drop table if exists Sessions" );
        onCreate(db);
    }

    public boolean Register(String username,String password,  String dateOfBirth, String insulinR) {
        try {
            //to do try if user exists
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", username.trim());
            contentValues.put("password", password.trim());
            contentValues.put("dateOfBirth", dateOfBirth);
            contentValues.put("insulinRegiment", insulinR);
            contentValues.put("usage", 1);
            database.insert("Users", null, contentValues);
            return true;
        }
        catch(SQLException e)
        {
            return false;
        }
    }



    public boolean login(String username,String password)
    {
        boolean success=false;
        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery("select username, password from Users where username='"+username.trim()+"'", null);
            if (cursor.moveToFirst()) {
                String pass=cursor.getString(1).trim();
                success=pass.equals(password);
            }
        }
        catch(SQLException e)
        {
            Log.d("login", e.toString());
        }
        return success;
    }


    public User getUser(String username)
    {
        User user = new User();

        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery("select username, password, dateOfBirth, insulinRegiment from Users where username='"+username.trim()+"'", null);
            if (cursor.moveToFirst()) {

                user.setUsername(cursor.getString(0).trim());
                user.setUsername( cursor.getString(0).trim());
                user.setUsername( cursor.getString(0).trim());
                user.setUsername( cursor.getString(0).trim());
            }

        }
        catch(SQLException e)
        {
            Log.d("login", e.toString());
        }
        return user;
    }
    public LinkedList<User> getUsers()
    {
        LinkedList<User> users = new LinkedList<>();

        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery("select username, password, dateOfBirth, insulinRegiment from Users ", null);
            while(cursor.moveToNext())
            {
                User user=new User();
                user.setUsername(cursor.getString(0).trim());
                user.setUsername( cursor.getString(0).trim());
                user.setUsername( cursor.getString(0).trim());
                user.setUsername( cursor.getString(0).trim());
                users.add(user);
            }

        }
        catch(SQLException e)
        {
            Log.d("login", e.toString());
        }
        return users;
    }

    public boolean editUser(String username, String newUsername, String password, String dateOfBirth, String insulinR)
    {
        boolean success=false;
        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Connection conn = DriverManager.getConnection(DATABASE_NAME);
            PreparedStatement change = conn.prepareStatement("Update Users set username = ? ,password = ?, dateOfBirth=?, insulinRegiment=? where username=?");

            change.setString(1,newUsername );
            change.setString(2,password);
            change.setString(3,dateOfBirth );
            change.setString(4,insulinR);
            change.setString(5,username );
            change.executeQuery();

        }
        catch(SQLException e)
        {
            Log.d("login", e.toString());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    public boolean deleteUser(String username)
    {
        boolean success=false;
        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Connection conn = DriverManager.getConnection(DATABASE_NAME);
            PreparedStatement change = conn.prepareStatement("delete from Users where username=?");
            change.setString(1,username );
            change.executeQuery();
            deleteSession(username);
        }
        catch(SQLException e)
        {
            Log.d("login", e.toString());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    public int getUsage(String username, String password, String dateOfBirth, String insulinR)
    {
        int pass=0;
        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery("select usage from Users where username='" + username.trim() + "'", null);
            if (cursor.moveToFirst()) {
                pass=cursor.getInt(1);
            }
        }
        catch(SQLException e)
        {
            Log.d("login", e.toString());
        }
        return pass;

    }
    public boolean increaseUsage(String username)
    {
        boolean success=false;
        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Connection conn = DriverManager.getConnection(DATABASE_NAME);
            PreparedStatement change = conn.prepareStatement("Update Users set usage=usage+1 where username=?");
            change.executeQuery();
        }
        catch(SQLException e)
        {
            Log.d("login", e.toString());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    public void updateSession(String username, int insulin, int iteration, String screen)
    {
            boolean success=false;
            PreparedStatement change;
            try {
                Connection conn = DriverManager.getConnection(DATABASE_NAME);
                if(userExists(username)) {
                    SQLiteDatabase database = this.getReadableDatabase();
                    change = conn.prepareStatement("Update Sessions set insulin = ?, iterations=?, screen=? where username=?");


                }
                else{
                     change = conn.prepareStatement("insert into Sessions ( insulin, iterations, screen,username) values(?,?,?,?) ");

                }
                change.setInt(1, insulin);
                change.setInt(2, iteration);
                change.setString(3, screen);
                change.setString(4, username);
                change.executeQuery();

            }
            catch(SQLException e)
            {
                Log.d("login", e.toString());
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }

        }


    public boolean deleteSession(String username)
    {
        boolean success=false;
        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Connection conn = DriverManager.getConnection(DATABASE_NAME);
            PreparedStatement change = conn.prepareStatement("delete from Session where username=?");
            change.setString(1,username );
            change.executeQuery();
        }
        catch(SQLException e)
        {
            Log.d("login", e.toString());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    public boolean userExists(String username)
    {
        boolean exists = false;
        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery("select usernameusername='"+username.trim()+"'", null);
            if (cursor.moveToFirst()) {
              exists=true;
            }

        }
        catch(SQLException e)
        {
            Log.d("login", e.toString());
        }
        return exists;

    }



}
