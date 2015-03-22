package com.prindle.a.r.booklogrp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.prindle.a.r.booklogrp.R;
import com.prindle.a.r.booklogrp.classes.Book;
import com.prindle.a.r.booklogrp.dbhandler.BookDBHandler;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BookDetailsActivity extends ActionBarActivity {

    @InjectView(R.id.titleEditText)
    EditText mTitle;
    @InjectView(R.id.authorEditText)
    EditText mAuthor;
    @InjectView(R.id.readRadioButton)
    RadioButton mRead;
    @InjectView(R.id.notReadRadioButton)
    RadioButton mNotRead;
    @InjectView(R.id.ratingBar)
    RatingBar mRatingBar;
    @InjectView(R.id.editButton)Button mEditButton;
    @InjectView(R.id.deleteButton)Button mDeleteButton;
    @InjectView(R.id.saveButton)
    Button mSaveButton;
    @InjectView(R.id.saveLinearLayout)LinearLayout mSaveLinearLayout;
    @InjectView(R.id.editLinearLayout)LinearLayout mEditLinearLayout;
    @InjectView(R.id.undoButton)
    Button mUndoButton;
    @InjectView(R.id.summaryEditText)
    EditText mSummary;
    @InjectView(R.id.bookCoverImageView)ImageView mCover;
    private Book mBook;
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_from_right, R.anim.push_left);
        setContentView(R.layout.activity_book_details);
        ButterKnife.inject(this);
        Bundle extras = getIntent().getExtras();

        Book book;
        if (extras != null) {
            int bookId = extras.getInt(Book.BOOKID);
            mBook = new Book();
            BookDBHandler dbHandler = new BookDBHandler(this);
            mBook = dbHandler.getBookById(bookId);
            showBookDetails();
        } else {
            Toast.makeText(BookDetailsActivity.this, "Sorry Database Error.", Toast.LENGTH_LONG).show();
            end();
        }

        mSummary.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (KeyEvent.KEYCODE_ENTER == event.getKeyCode()) {
                    saveBook();
                    return true;
                }
                return false;
            }
        });

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBook();
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(BookDetailsActivity.this);
                alertDialog.setTitle("Delete Book From Database");
                alertDialog.setMessage("Are you sure you want to delete this book?  This cannot be undone.");
                alertDialog.setPositiveButton("Yes, Delete Book",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                deleteBook();
                            }
                        });
                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        });

        mUndoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                isEditing = false;
                invalidateOptionsMenu();
                mTitle.setFocusable(false);
                mTitle.setEnabled(false);
                mAuthor.setFocusable(false);
                mAuthor.setEnabled(false);
                mSummary.setEnabled(false);
                mSummary.setFocusable(false);
                mRatingBar.setIsIndicator(true);
                mRead.setClickable(false);
                mNotRead.setClickable(false);
                mSaveLinearLayout.setVisibility(View.INVISIBLE);
                mEditLinearLayout.setVisibility(View.VISIBLE);
                showBookDetails();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveBook();
            }
        });
    }

    private void editBook() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        isEditing = true;
        invalidateOptionsMenu();
        mTitle.setFocusableInTouchMode(true);
        mTitle.setEnabled(true);
        mAuthor.setFocusableInTouchMode(true);
        mAuthor.setEnabled(true);
        mSummary.setEnabled(true);
        mSummary.setFocusableInTouchMode(true);
        mRatingBar.setIsIndicator(false);
        mRead.setClickable(true);
        mNotRead.setClickable(true);
        mSaveLinearLayout.setVisibility(View.VISIBLE);
        mEditLinearLayout.setVisibility(View.INVISIBLE);
    }

    private void saveBook() {
        if (mTitle.getText().toString().equals("") || mAuthor.getText().toString().equals("")) {
            Toast.makeText(this, "Must enter a Title and Author", Toast.LENGTH_LONG).show();
            if (mTitle.getText().toString().equals("")) {
                mTitle.requestFocus();
            }
            else {
                mAuthor.requestFocus();
            }
        } else {
            if(saveBookDetails()) {
                end();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_from_left, R.anim.push_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_details, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.action_edit);

        if (isEditing) {
            item.setEnabled(false);
            item.getIcon().setAlpha(100);
        } else {
            item.setEnabled(true);
            item.getIcon().setAlpha(255);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Edit
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                end();
                return true;

            //Edit
            case R.id.action_edit:
                editBook();
                return true;

            //Delete
            case R.id.action_delete:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(BookDetailsActivity.this);
                alertDialog.setTitle("Delete Book From Database");
                alertDialog.setMessage("Are you sure you want to delete this book?  This cannot be undone.");
                alertDialog.setPositiveButton("Yes, Delete Book",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                deleteBook();
                            }
                        });
                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void end(){
        finish();
        overridePendingTransition(R.anim.pull_from_left, R.anim.push_right);
    }

    private boolean saveBookDetails() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        BookDBHandler dbHandler = new BookDBHandler(BookDetailsActivity.this);
        Book book = setBookDetails();
        if(book.getTitle().equals(mBook.getTitle())&& book.getAuthor().equals(mBook.getAuthor())) {
            imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
            dbHandler.updateBook(book);
            end();
        }
        else{
            if (dbHandler.isDuplicateBook(book)) {
                Toast.makeText(this, "Another book with same title and author already exists.", Toast.LENGTH_LONG).show();
            } else {
                mBook = book;
                imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                dbHandler.updateBook(mBook);
                end();
            }
        }
        return false;
    }

    private Book setBookDetails() {
        Book book = new Book(mTitle.getText().toString(),mAuthor.getText().toString());
        book.setID(mBook.getID());
        book.setCoverUrl(mBook.getCoverUrl());
        book.setRating((int) mRatingBar.getRating());
        if (mRead.isChecked()) {
            book.setReadOrNot(Book.READ);
        } else {
            book.setReadOrNot(Book.UNREAD);
        }
        book.setSummary(mSummary.getText().toString());
        return book;
    }

    private void deleteBook() {
        BookDBHandler dbHandler = new BookDBHandler(BookDetailsActivity.this);
        dbHandler.deleteBook(mBook);
        finish();
        overridePendingTransition(R.anim.pull_from_left, R.anim.smoosh);
    }

    private void showBookDetails() {
        mTitle.setText(mBook.getTitle());
        mAuthor.setText(mBook.getAuthor());
        if (mBook.getReadOrNot().equals(Book.READ)) {
            mRead.setChecked(true);
        } else {
            mNotRead.setChecked(true);
        }
        mRatingBar.setRating(mBook.getRating());
        mSummary.setText(mBook.getSummary());
        Picasso.with(this)
                .load(mBook.getCoverUrl())
                .into(mCover);
    }

}
