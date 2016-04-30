package com.example.anna.ketoapp2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by Anna on 23/10/2015.
 * Class is needed to set up the database and provide methods for communication with it (CRUD operations)
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    private final static String DATABASE_NAME="DiabetesApp.db";
    private static Context mContext;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        mContext=context;
    }

    //Method creates two tables the application
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table Users( user_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, dateOfBirth TEXT,insulinRegiment TEXT, usage INT, regDateTime DATETIME,profilePic BLOB)");
        }
        catch(SQLException sqle)
        {

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Users" );
        onCreate(db);
    }

    //Method that creates the row for the new user
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

    //Method returnes  user's information for the username specified
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
    //Method returnes the list of users registered in the system
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

    //Method edits user information using the parameters passed for specified username
    // if profile pisture was changed
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

    //Method edits user information using the parameters passed for specified username
    //when the profile picture was not updated
    public boolean editUser(String username, String newUsername, String dateOfBirth, String insulinR)
    {
        boolean success = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("username", newUsername);
            values.put("dateOfBirth", dateOfBirth);
            values.put("insulinRegiment", insulinR);
            db.update("Users", values, "username = '" + username + "'", null);
            success=true;

        }
        catch(SQLException e)
        {
            Log.d("login", e.toString());
        }
        return success;
    }

    //Method deletes the user data from the system for specified username
    public boolean deleteUser(String username)
    {
        boolean success = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("Users", "username" + "='" + username + "'", null);

        }
        catch(SQLException e)
        {
            Log.d("login", e.toString());
        }
        return success;
    }

    //Method returns the int value that represents how many times user opened the application
    //Usage information is never used within the system however this method could be used for later development
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
    //The method increses the users usage value by one
    //Usage information is never used within the system however this method could be used for later development
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

    //Method returns information about the user whose account was active last
    public User getCurrent()
    {
        User user = null;

        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery("select username, dateOfBirth, insulinRegiment,profilePic,regDateTime from Users order by regDateTime desc limit 1 ", null);
            while (cursor.moveToNext()) {
                user=new User();
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

    //Method updates the registration date to the date when the user opened the system for the last time.
    // This is done to know which user information to open next time the application is open
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




    //Method returnes the boolean value that identifies if the user with specified username is already registered within the system.
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

    //Method converts the Bitmap image to the format stored in database.
    //on registration the method corverts the default image
    public ByteArrayOutputStream insertPic(Bitmap pic)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if(pic==null) {
            pic = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.defaultpic);
        }
        pic.compress(Bitmap.CompressFormat.PNG, 100, out);
        return out;
    }

    //Method that returnes the String that contains the current DateTime in specified format
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
