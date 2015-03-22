package com.prindle.a.r.booklogrp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.prindle.a.r.booklogrp.R;
import com.prindle.a.r.booklogrp.classes.Book;
import com.prindle.a.r.booklogrp.dbhandler.BookDBHandler;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AddBookActivity extends ActionBarActivity {
    private static final String TAG = AddBookActivity.class.getSimpleName();
    private Book mIsbnBook;

    @InjectView(R.id.titleEditText)EditText mTitleEditText;
    @InjectView(R.id.authorEditText)EditText mAuthorEditText;
    @InjectView(R.id.readRadioButton)RadioButton mReadRadioButton;
    @InjectView(R.id.ratingBar)RatingBar mRatingBar;
    @InjectView(R.id.saveButton)Button mSaveButton;
    @InjectView(R.id.clearButton) Button mClearButton;
    @InjectView(R.id.summaryEditText)EditText mSummary;
    @InjectView(R.id.bookCoverImageView)ImageView mCoverImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_from_right, R.anim.push_left);
        setContentView(R.layout.activity_add_book);
        ButterKnife.inject(this);
        mTitleEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mTitleEditText, InputMethodManager.SHOW_IMPLICIT);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTitleEditText.getText().toString().equals("")) {
                    Toast.makeText(AddBookActivity.this, "Must enter a Title", Toast.LENGTH_LONG).show();
                    mTitleEditText.requestFocus();
                } else if (mAuthorEditText.getText().toString().equals("")) {
                    Toast.makeText(AddBookActivity.this, "Must enter an Author", Toast.LENGTH_LONG).show();
                    mAuthorEditText.requestFocus();
                } else {
                    saveBook();
                }
            }
        });

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearFields();
            }
        });

        mTitleEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (mTitleEditText.getText().toString().equals("")) {
                        Toast.makeText(AddBookActivity.this, "Must enter a Title", Toast.LENGTH_LONG).show();
                        return true;
                    }
                }
                return false;
            }
        });
        mAuthorEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (mAuthorEditText.getText().toString().equals("")) {
                        Toast.makeText(AddBookActivity.this, "Must enter an Author", Toast.LENGTH_LONG).show();
                        return true;
                    }
                }
                return false;
            }
        });

    }

    private void ClearFields() {
        mTitleEditText.setText("");
        mAuthorEditText.setText("");
        mSummary.setText("");
        mRatingBar.setRating(0);
        mReadRadioButton.setChecked(true);
        mCoverImage.setImageDrawable(null);
    }

    private void saveBook() {
        BookDBHandler dbHandler = new BookDBHandler(this);
        Book book = new Book(mTitleEditText.getText().toString(), mAuthorEditText.getText().toString());
        book.setReadOrNot(Book.UNREAD);
        if (mReadRadioButton.isChecked()) {
            book.setReadOrNot(Book.READ);
        }
        book.setRating((int) mRatingBar.getRating());
        book.setSummary(mSummary.getText().toString());
        try{
            book.setCoverUrl(mIsbnBook.getCoverUrl());
        }
        catch(NullPointerException e){
            Log.v(TAG, "No mIsbnBook coverUrl");
        }
        if(dbHandler.isDuplicateBook(book)){
            Toast.makeText(AddBookActivity.this, "Book already exists.", Toast.LENGTH_LONG).show();
        }
        else{
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mTitleEditText.clearFocus();
            mAuthorEditText.clearFocus();
            mSummary.clearFocus();
            imm.hideSoftInputFromWindow(mTitleEditText.getWindowToken(),0);
            imm.hideSoftInputFromWindow(mAuthorEditText.getWindowToken(),0);
            imm.hideSoftInputFromWindow(mSummary.getWindowToken(),0);
            dbHandler.addBook(book);
            end();
        }
    }

    private void end(){
        finish();
        overridePendingTransition(R.anim.pull_from_left, R.anim.push_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_from_left, R.anim.push_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_book, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mTitleEditText.clearFocus();
                mAuthorEditText.clearFocus();
                mSummary.clearFocus();
                imm.hideSoftInputFromWindow(mTitleEditText.getWindowToken(),0);
                imm.hideSoftInputFromWindow(mAuthorEditText.getWindowToken(),0);
                imm.hideSoftInputFromWindow(mSummary.getWindowToken(),0);
                end();
                return true;
            case R.id.action_add_book_isbn:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Look up book by ISBN");
                alert.setMessage("If the ISBN is found it will populate the information. You may review and edit the information before saving.");

                // Set an EditText view to get user input
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                input.setHint(R.string.isbn_hint);
                input.setEms(13);
                alert.setView(input);

                alert.setPositiveButton("Look Up ISBN", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        if(value.equals("")){
                            Toast.makeText(AddBookActivity.this, "No ISBN entered.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            ClearFields();
                            SearchForISBN(value);
                        }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean networkIsAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void SearchForISBN(String isbn){
        final String isbnUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
        if (networkIsAvailable()) {
            //get isbn json data
            Log.v(TAG, isbnUrl);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(isbnUrl)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // toggleRefresh();
                        }
                    });
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            //save ISBN data
                            parseJSONData(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showISBNBook();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //toggleRefresh();
                                    Toast.makeText(AddBookActivity.this, "Sorry, network error", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } catch (IOException e) {
                        Log.e(TAG, " IO Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Sorry No Book Found.", e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //toggleRefresh();
                                Toast.makeText(AddBookActivity.this, "Sorry, no book found", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        } else {
            Toast.makeText(AddBookActivity.this, "Sorry, network unavailable.", Toast.LENGTH_LONG).show();
        }
    }

    private void showISBNBook(){
        mTitleEditText.setText(mIsbnBook.getTitle());
        mAuthorEditText.setText(mIsbnBook.getAuthor());
        mSummary.setText(mIsbnBook.getSummary());
        Picasso.with(AddBookActivity.this)
            .load(mIsbnBook.getCoverUrl())
            .into(mCoverImage);
    }

    private void parseJSONData(String jsonData)throws JSONException{
        mIsbnBook = new Book();
        JSONObject book = new JSONObject(jsonData);
        JSONArray items = book.getJSONArray("items");
        JSONObject item = items.getJSONObject(0);
        JSONObject volumeInfo = item.getJSONObject("volumeInfo");

        JSONArray authors = volumeInfo.getJSONArray("authors");
        String author = authors.getString(0);
        for (int i=1;i < authors.length();i++){
            author += " & " + authors.getString(i);
        }
        mIsbnBook.setTitle(volumeInfo.getString("title"));
        mIsbnBook.setAuthor(author);
        try{
            mIsbnBook.setSummary(volumeInfo.getString("description"));
        }
        catch (JSONException e) {
            Log.e(TAG, "No Description", e);
        }
        try{
            JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
            mIsbnBook.setCoverUrl(imageLinks.getString("thumbnail"));
        }
        catch (JSONException e) {
            Log.e(TAG, "No Thumbnail", e);
        }

    }
}
