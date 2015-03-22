package com.prindle.a.r.booklogrp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prindle.a.r.booklogrp.R;
import com.prindle.a.r.booklogrp.adapters.BookListAdapter;
import com.prindle.a.r.booklogrp.classes.Book;
import com.prindle.a.r.booklogrp.dbhandler.BookDBHandler;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchBookActivity extends ActionBarActivity {
    List<Book> mBooks;
    @InjectView(R.id.searchButton)
    ImageButton mSearchButton;
    @InjectView(R.id.searchEditText)
    EditText mSearchEditText;
    @InjectView(R.id.searchListView)
    ListView mSearchListView;
    @InjectView(R.id.searchRelativeLayout)RelativeLayout mSearchRelativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_from_right, R.anim.push_left);
        setContentView(R.layout.activity_search_book);
        ButterKnife.inject(this);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mSearchEditText, InputMethodManager.SHOW_FORCED);
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    LoadSearchBookListView();
                }
                return true;
            }
        });
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadSearchBookListView();

            }
        });
        mSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Book.BOOKID, mBooks.get(position).getID());
                setResult(RESULT_OK, returnIntent);
                end();
            }
        });
    }

    public void LoadSearchBookListView() {
        if (mSearchEditText.getText().toString().equals("")) {
            Toast.makeText(SearchBookActivity.this, "Must enter a Title or Author", Toast.LENGTH_LONG).show();
        } else {
            BookDBHandler dbHandler = new BookDBHandler(SearchBookActivity.this);
            mBooks = dbHandler.searchForBook(mSearchEditText.getText().toString());
            if(mBooks.isEmpty()){
                Toast.makeText(SearchBookActivity.this, "No books found.", Toast.LENGTH_LONG).show();
                mSearchRelativeLayout.setBackgroundColor(Color.BLACK);
            }
            else {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
                mSearchRelativeLayout.setBackgroundColor(getResources().getColor(R.color.background_dark));
            }
            BookListAdapter adapter = new BookListAdapter(this, mBooks);
            mSearchListView.setAdapter(adapter);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mSearchEditText.clearFocus();
                imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
                end();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
