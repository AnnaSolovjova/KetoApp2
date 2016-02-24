package com.example.anna.ketoapp2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.TabLayout;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by Anna on 23/10/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    private final static String DATABASE_NAME="DiabetesApp.db";
    private static Context mContext;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table Users( user_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, dateOfBirth TEXT,insulinRegiment TEXT, usage INT, regDateTime DATETIME,profilePic BLOB)");
            db.execSQL("create table Sessions( username TEXT PRIMARY KEY, insulin INT, iterations INT, screen TEXT, sesDateTime DATETIME");
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

    public boolean Register(String username,  String dateOfBirth, String insulinR) {
        try {
            //to do try if user exists

            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", username.trim());
            contentValues.put("dateOfBirth", dateOfBirth);
            contentValues.put("insulinRegiment", insulinR);
            contentValues.put("usage", 1);
            contentValues.put("regDateTime", getDateTime());
            contentValues.put("profilePic",insertPic(null).toByteArray());
            database.insert("Users", null, contentValues);
            return true;
        }
        catch(SQLException e)
        {
            return false;
        }
    }


    public User getUser(String username)
    {
        User user = new User();

        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery("select username, dateOfBirth, insulinRegiment,profilePic from Users where username='"+username.trim()+"'", null);
            if (cursor.moveToFirst()) {

                user.setUsername(cursor.getString(0).trim());
                user.setDateOfBirth(cursor.getString(1).trim());
                user.setInsulinRegiment(cursor.getString(2).trim());
                byte[] blob = cursor.getBlob(cursor.getColumnIndex("profilePic"));
                ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                user.setProfilePic(bitmap);
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
            Cursor cursor = database.rawQuery("select username, dateOfBirth, insulinRegiment,profilePic from Users ", null);
            while(cursor.moveToNext())
            {
                User user=new User();
                user.setUsername(cursor.getString(0).trim());
                user.setDateOfBirth( cursor.getString(1).trim());
                user.setInsulinRegiment( cursor.getString(2).trim());
                byte[] blob = cursor.getBlob(cursor.getColumnIndex("profilePic"));
                ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                user.setProfilePic(bitmap);
                users.add(user);
            }

        } catch (SQLException e)
        {
            Log.d("login", e.toString());
        }
        return users;
    }

    public boolean editUser(String username, String newUsername, String dateOfBirth, String insulinR,Bitmap profilePic)
    {
        boolean success=false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("username", newUsername);
            values.put("dateOfBirth", dateOfBirth);
            values.put("insulinRegiment", insulinR);
            values.put("profilePic", insertPic(profilePic).toByteArray());
            db.update("Users", values, "username = '" + username + "'", null);
            success=true;

        }
        catch(SQLException e)
        {
            Log.d("login", e.toString());
        }
        return success;
    }

    public boolean deleteUser(String username)
    {
        boolean success=false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("Users", "username" + "='" + username + "'", null);
            deleteSession(username);
        }
        catch(SQLException e)
        {
            Log.d("login", e.toString());
        }
        return success;
    }

    public int getUsage(String username)
    {
        int pass=0;
        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery("select usage from Users where username='" + username.trim() + "'", null);
            if (cursor.moveToFirst()) {
                pass=cursor.getInt(0);
            }
        } catch (SQLException e)
        {
            Log.d("login", e.toString());
        }
        return pass;

    }
    //check that working
    public boolean increaseUsage(String username)
    {
        boolean success=false;
        try {
            int usage = getUsage(username);
            usage++;
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("usage", usage);
            db.update("Users", values, "username = '" + username + "'", null);

        }
        catch(SQLException e) {
            Log.d("login", e.toString());
        }
        return success;
    }

    public void updateSession(String username, int insulin, int iteration, String screen)
    {
            boolean success=false;
             SQLiteDatabase db = this.getWritableDatabase();
             ContentValues values = new ContentValues();
            try {
                values.put("insulin", insulin);
                values.put("iterations", iteration);
                values.put("screen", screen);
                values.put("sesDateTime", "datetime()");
                Connection conn = DriverManager.getConnection(DATABASE_NAME);
                if(userExists(username)) {
                    values.put("insulin", insulin);
                    values.put("iterations", iteration);
                    values.put("screen", screen);
                    values.put("sesDateTime", "datetime()");
                    db.update("Sessions", values, "username = '" + username + "'", null);

                }
                else{
                     values.put("username",username);
                     db.insert("Sessions",null,values);

                }

            }
            catch(SQLException e)
            {
                Log.d("login", e.toString());
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }

        }

    public User getCurrent()
    {
        User user = new User();

        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery("select username, dateOfBirth, insulinRegiment,profilePic,regDateTime from Users order by regDateTime desc limit 1 ", null);
            while (cursor.moveToNext()) {
              String date=cursor.getString(cursor.getColumnIndex("regDateTime"));
                user.setUsername(cursor.getString(0).trim());
                user.setDateOfBirth(cursor.getString(1).trim());
                user.setInsulinRegiment(cursor.getString(2).trim());
                byte[] blob = cursor.getBlob(cursor.getColumnIndex("profilePic"));
                ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                user.setProfilePic(bitmap);
            }


        }
        catch(SQLException e)
        {
            Log.d("login", e.toString());
        }
        return user;
    }

    public void updateCurrent(String username){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
           values.put("regDateTime", getDateTime());
            db.update("Users", values, "username = '"+username+"'", null);

        }
        catch(SQLException e)
        {
            Log.d("login", e.toString());
        }
    }


    public boolean deleteSession(String username)
    {
        boolean success=false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("Sessions", "username" + "='" + username + "'", null);
        }
        catch(SQLException e)
        {
            Log.d("login", e.toString());
        }
        return success;
    }

    public boolean userExists(String username)
    {
        boolean exists = false;
        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery("select username from Users where username='" + username.trim() + "'", null);
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


    public ByteArrayOutputStream insertPic(Bitmap pic)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if(pic==null) {
            pic = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.defaultpic);
        }
        pic.compress(Bitmap.CompressFormat.PNG, 100, out);
        return out;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
