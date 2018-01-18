package com.example.abhishek.catalogwithretro.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.model.Book;

import java.util.List;

/**
 * Created by abhishek on 18/1/18.
 */

public class SortedBooksByAuthorAdapter extends RecyclerView.Adapter<SortedBooksByAuthorAdapter.SortedBooksViewHolder> {

    private List<Book> books;

    public SortedBooksByAuthorAdapter(List<Book> books) {
        this.books = books;
    }

    @Override
    public SortedBooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.sorted_book_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem,
                parent,false);
        return new SortedBooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SortedBooksViewHolder holder, int position) {
        holder.bind(books.get(position));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setBooks(List<Book> book){
        this.books = book;
        notifyDataSetChanged();
    }

    class SortedBooksViewHolder extends RecyclerView.ViewHolder{

        TextView tv_sorted_book;

        public SortedBooksViewHolder(View itemView) {
            super(itemView);
            tv_sorted_book = (TextView) itemView.findViewById(R.id.tv_sorted_book_name);
        }

        public void bind(Book book) {
            tv_sorted_book.setText(book.getName());
        }
    }
}
