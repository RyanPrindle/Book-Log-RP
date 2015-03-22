package com.prindle.a.r.booklogrp.dbhandler;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prindle.a.r.booklogrp.classes.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RP on 2/8/2015.
 */
public class BookDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "bookDB.db";
    private static final String TABLE_BOOKS = "books";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BOOKTITLE = "booktitle";
    public static final String COLUMN_BOOKAUTHOR = "bookauthor";
    public static final String COLUMN_READORNOT = "readornot";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_SUMMARY = "summary";
    public static final String COLUMN_COVER_URL = "cover";


    public BookDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOKS_TABLE = "CREATE TABLE " +
                TABLE_BOOKS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_BOOKTITLE + " TEXT,"
                + COLUMN_BOOKAUTHOR + " TEXT,"
                + COLUMN_READORNOT + " TEXT,"
                + COLUMN_RATING + " INTEGER,"
                + COLUMN_SUMMARY + " TEXT,"
                + COLUMN_COVER_URL + " TEXT)";
        db.execSQL(CREATE_BOOKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        switch (oldVersion){
            case 2:
                db.execSQL("ALTER TABLE " + TABLE_BOOKS + " ADD COLUMN " + COLUMN_COVER_URL + " TEXT");
                break;
            default:
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
                onCreate(db);
        }
    }

    public boolean isDuplicateBook(Book book){
        String query = "Select * FROM " + TABLE_BOOKS + " WHERE " + COLUMN_BOOKTITLE + " = \""
                + book.getTitle() + "\" AND " + COLUMN_BOOKAUTHOR + " = \"" + book.getAuthor() + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() == 0 ) {
            cursor.close();
            db.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }

    public int updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOKTITLE, book.getTitle());
        values.put(COLUMN_BOOKAUTHOR, book.getAuthor());
        values.put(COLUMN_READORNOT, book.getReadOrNot());
        values.put(COLUMN_RATING, book.getRating());
        values.put(COLUMN_SUMMARY, book.getSummary());
        values.put(COLUMN_COVER_URL, book.getCoverUrl());
        // updating row
        return db.update(TABLE_BOOKS, values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(book.getID()) });
    }

    public void addBook(Book book) {

            ContentValues values = new ContentValues();
            values.put(COLUMN_BOOKTITLE, book.getTitle());
            values.put(COLUMN_BOOKAUTHOR, book.getAuthor());
            values.put(COLUMN_READORNOT, book.getReadOrNot());
            values.put(COLUMN_RATING, book.getRating());
            values.put(COLUMN_SUMMARY, book.getSummary());
            values.put(COLUMN_COVER_URL, book.getCoverUrl());

            //Insert row
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(TABLE_BOOKS, null, values);
            db.close();
    }

    public List<Book> getAllBooks(){
        List<Book> bookList = new ArrayList<Book>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BOOKS + " ORDER BY " + COLUMN_BOOKTITLE + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setID(Integer.parseInt(cursor.getString(0)));
                book.setTitle(cursor.getString(1));
                book.setAuthor(cursor.getString(2));
                book.setReadOrNot(cursor.getString(3));
                book.setRating(cursor.getInt(4));
                book.setSummary(cursor.getString(5));
                book.setCoverUrl(cursor.getString(6));
                // Adding book to list
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return book list
        return bookList;
    }

    public List<Book> getBookByTitle(String bookTitle) {
        List<Book> bookList = new ArrayList<Book>();
        //Select book title query
        String query = "Select * FROM " + TABLE_BOOKS + " WHERE " + COLUMN_BOOKTITLE + " =  \"" + bookTitle + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setID(Integer.parseInt(cursor.getString(0)));
                book.setTitle(cursor.getString(1));
                book.setAuthor(cursor.getString(2));
                book.setReadOrNot(cursor.getString(3));
                book.setRating(cursor.getInt(4));
                book.setSummary(cursor.getString(5));
                book.setCoverUrl(cursor.getString(6));
                // Adding book to list
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookList;
    }

    public List<Book> getBookByAuthor(String bookAuthor) {
        List<Book> bookList = new ArrayList<Book>();
        //Select book author query
        String query = "Select * FROM " + TABLE_BOOKS + " WHERE " + COLUMN_BOOKAUTHOR + " =  \"" + bookAuthor + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setID(Integer.parseInt(cursor.getString(0)));
                book.setTitle(cursor.getString(1));
                book.setAuthor(cursor.getString(2));
                book.setReadOrNot(cursor.getString(3));
                book.setRating(cursor.getInt(4));
                book.setSummary(cursor.getString(5));
                book.setCoverUrl(cursor.getString(6));
                // Adding book to list
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookList;
    }

    public List<Book> getBookByReadOrNot(String readOrNot) {
        List<Book> bookList = new ArrayList<Book>();
        //Select book readOrNot query
        String query = "Select * FROM " + TABLE_BOOKS + " WHERE " + COLUMN_READORNOT + " =  \"" + readOrNot + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setID(Integer.parseInt(cursor.getString(0)));
                book.setTitle(cursor.getString(1));
                book.setAuthor(cursor.getString(2));
                book.setReadOrNot(cursor.getString(3));
                book.setRating(cursor.getInt(4));
                book.setSummary(cursor.getString(5));
                book.setCoverUrl(cursor.getString(6));
                // Adding book to list
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookList;
    }

    public List<Book> getBookByRating(int rating) {
        List<Book> bookList = new ArrayList<Book>();
        //Select book title query
        String query = "Select * FROM " + TABLE_BOOKS + " WHERE " + COLUMN_RATING + " =  \"" + rating + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setID(Integer.parseInt(cursor.getString(0)));
                book.setTitle(cursor.getString(1));
                book.setAuthor(cursor.getString(2));
                book.setReadOrNot(cursor.getString(3));
                book.setRating(cursor.getInt(4));
                book.setSummary(cursor.getString(5));
                book.setCoverUrl(cursor.getString(6));

                // Adding book to list
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookList;
    }

    public List<Book> searchForBook(String search){
        List<Book> bookList = new ArrayList<Book>();
        String query = "Select * FROM " + TABLE_BOOKS + " WHERE " + COLUMN_BOOKTITLE + " LIKE '%"
                + search + "%' OR " + COLUMN_BOOKAUTHOR + " LIKE '%" + search + "%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setID(Integer.parseInt(cursor.getString(0)));
                book.setTitle(cursor.getString(1));
                book.setAuthor(cursor.getString(2));
                book.setReadOrNot(cursor.getString(3));
                book.setRating(cursor.getInt(4));
                book.setSummary(cursor.getString(5));
                book.setCoverUrl(cursor.getString(6));
                // Adding book to list
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookList;
    }



    public void deleteBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKS, COLUMN_ID + " = ?",
                new String[] { String.valueOf(book.getID()) });
        db.close();
    }
    public int getTotalBookCount(){
        String countQuery = "SELECT  * FROM " + TABLE_BOOKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int getReadOrNotBookCount( String readOrNot){
        readOrNot = readOrNot.toUpperCase();
        //Select book readOrNot query
        String query = "Select * FROM " + TABLE_BOOKS + " WHERE " + COLUMN_READORNOT + " =  \"" + readOrNot + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public Book getBookById(int id){
        Book book = new Book();
        String query = "Select * FROM " + TABLE_BOOKS + " WHERE " + COLUMN_ID + " =  \"" + id + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            book.setID(Integer.parseInt(cursor.getString(0)));
            book.setTitle(cursor.getString(1));
            book.setAuthor(cursor.getString(2));
            book.setReadOrNot(cursor.getString(3));
            book.setRating(cursor.getInt(4));
            book.setSummary(cursor.getString(5));
            book.setCoverUrl(cursor.getString(6));
        }
        cursor.close();
        db.close();
        return book;
    }
}