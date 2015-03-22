package com.prindle.a.r.booklogrp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.prindle.a.r.booklogrp.classes.Book;
import com.prindle.a.r.booklogrp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookListAdapter extends BaseAdapter {
    private static final String TAG = BookListAdapter.class.getSimpleName();
    public static final String READ = "READ";
    public static final String UNREAD = "UNREAD";
    protected Context mContext;
    protected List<Book> mBooks;

    public BookListAdapter(Context context, List<Book> books) {
        mContext = context;
        mBooks = books;
    }


    @Override
    public int getCount() {
        return mBooks.size();
    }

    @Override
    public Object getItem(int position) {
        return mBooks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.book_list_item, null);
            holder = new ViewHolder();
            holder.titleLabel = (TextView) convertView.findViewById(R.id.titleTextView);
            holder.authorLabel = (TextView) convertView.findViewById(R.id.authorTextView);
            holder.summaryLabel = (TextView) convertView.findViewById(R.id.summaryTextView);
            holder.readLabel = (TextView) convertView.findViewById(R.id.readTextView);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            holder.coverImage = (ImageView) convertView.findViewById(R.id.bookCoverImageView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.titleLabel.setText(mBooks.get(position).getTitle());
        holder.authorLabel.setText(mBooks.get(position).getAuthor());
        holder.summaryLabel.setText(mBooks.get(position).getSummary());
        if (mBooks.get(position).getReadOrNot().equals(Book.READ)) {
            holder.readLabel.setText(mContext.getString(R.string.read));
        } else {
            holder.readLabel.setText(mContext.getString(R.string.not_read));
        }
        holder.ratingBar.setRating(mBooks.get(position).getRating());

            Picasso.with(mContext).load(mBooks.get(position).getCoverUrl())
                    .placeholder(R.drawable.book)
                    .error(R.drawable.book)
                    .fit()
                    .centerInside()
                    .into(holder.coverImage);

        return convertView;
    }

    private static class ViewHolder {
        TextView titleLabel;
        TextView authorLabel;
        TextView readLabel;
        RatingBar ratingBar;
        TextView summaryLabel;
        ImageView coverImage;
    }
}
