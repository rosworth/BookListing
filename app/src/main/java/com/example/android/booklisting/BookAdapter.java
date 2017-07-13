package com.example.android.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);
        TextView book_title = (TextView) listItemView.findViewById(R.id.tv_title);
        TextView book_author = (TextView) listItemView.findViewById(R.id.tv_author);

        book_title.setText(currentBook.getTitle());
        book_author.setText(currentBook.getAuthors());

        return listItemView;
    }
}
