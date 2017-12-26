package com.example.android.bookapppreference;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Noemi on 12/25/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, ArrayList<Book> books){

        super(context,0, books);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);
        }
        Book books = getItem(position);

        TextView bookAuthor = (TextView)listItemView.findViewById(R.id.book_autor);
        bookAuthor.setText(books.getAuthor());

        TextView bookTitle = (TextView)listItemView.findViewById(R.id.book_title);
        bookTitle.setText(books.getTitle());

        TextView bookDescription = (TextView)listItemView.findViewById(R.id.book_description);
        bookDescription.setText(books.getDescription());

        TextView bookPrice = (TextView)listItemView.findViewById(R.id.book_price);
        String amount = formattedPrice(books.getPrice());
        bookPrice.setText(amount);

        ImageView bookImage = (ImageView)listItemView.findViewById(R.id.book_image);
        bookImage.setImageBitmap(books.getImage());

        TextView bookPublished = (TextView) listItemView.findViewById(R.id.book_published);
        bookPublished.setText(books.getPublished());

        return listItemView;
    }

    private String formattedPrice(double price){
        DecimalFormat formatPrice = new DecimalFormat("0.00");
        return formatPrice.format(price);
    }
}
