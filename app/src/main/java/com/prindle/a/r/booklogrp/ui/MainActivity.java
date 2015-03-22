package com.prindle.a.r.booklogrp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.prindle.a.r.booklogrp.R;
import com.prindle.a.r.booklogrp.adapters.BookListAdapter;
import com.prindle.a.r.booklogrp.classes.Book;
import com.prindle.a.r.booklogrp.dbhandler.BookDBHandler;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends ActionBarActivity {
    @InjectView(R.id.allRadioButton)RadioButton mAllRadioButton;
    @InjectView(R.id.readRadioButton)RadioButton mReadRadioButton;
    @InjectView(R.id.notReadRadioButton)RadioButton mNotReadRadioButton;
    @InjectView(R.id.bookListView)ListView mBookListView;
    List<Book> mBooks;

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        //ToDo if empty database open add book activity
        mAllRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadBookListView();
            }
        });
        mReadRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadBookListView();
            }
        });
        mNotReadRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadBookListView();
            }
        });
        mBookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), BookDetailsActivity.class);
                intent.putExtra(Book.BOOKID, mBooks.get(position).getID());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Book.FOUNDBOOK) {
            if (resultCode == RESULT_OK) {
                int result = data.getIntExtra(Book.BOOKID, -1);
                if (result != -1) {
                    Intent intent = new Intent(getApplicationContext(), BookDetailsActivity.class);
                    intent.putExtra(Book.BOOKID, result);
                    startActivity(intent);
                }
            }
            //if (resultCode == RESULT_CANCELED) {
            //}
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadBookListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();

        //noinspection SimplifiableIfStatement
        Intent intent;
        switch (itemId) {
            case R.id.action_add_book:
                intent = new Intent(this, AddBookActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_search_book:
                intent = new Intent(this, SearchBookActivity.class);
                startActivityForResult(intent, Book.FOUNDBOOK);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void LoadBookListView() {
        BookDBHandler dbHandler = new BookDBHandler(this);
        if (dbHandler.getTotalBookCount() == 0) {
            Toast.makeText(MainActivity.this, "No books in database.  Add a book.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, AddBookActivity.class);
            startActivity(intent);
        }
        if (mAllRadioButton.isChecked()) {
            mBooks = dbHandler.getAllBooks();
        }
        if (mReadRadioButton.isChecked()) {
            mBooks = dbHandler.getBookByReadOrNot(Book.READ);
        }
        if (mNotReadRadioButton.isChecked()) {
            mBooks = dbHandler.getBookByReadOrNot(Book.UNREAD);
        }

        String allCount = "All (" + dbHandler.getTotalBookCount()+")";
        String readCount = "Read (" + dbHandler.getReadOrNotBookCount(Book.READ)+")";
        String unReadCount = "Unread (" + dbHandler.getReadOrNotBookCount(Book.UNREAD)+")";
        mAllRadioButton.setText(allCount);
        mReadRadioButton.setText(readCount);
        mNotReadRadioButton.setText(unReadCount);
        BookListAdapter adapter = new BookListAdapter(this, mBooks);
        mBookListView.setAdapter(adapter);
    }


}
