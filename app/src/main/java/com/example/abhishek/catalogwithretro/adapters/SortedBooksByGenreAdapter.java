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

public class SortedBooksByGenreAdapter extends RecyclerView.Adapter<SortedBooksByGenreAdapter.SortedBooksViewHolder>{

    List<Book> bookList;

    public SortedBooksByGenreAdapter(List<Book> bookList) {
        this.bookList = bookList;
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

        Book book = bookList.get(position);

        holder.bind(book.getName());

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void setBooks(List<Book> bookList){
        this.bookList = bookList;
        notifyDataSetChanged();
    }

    class SortedBooksViewHolder extends RecyclerView.ViewHolder{

        TextView bookname;

        public SortedBooksViewHolder(View itemView) {
            super(itemView);
            bookname = (TextView) itemView.findViewById(R.id.tv_sorted_book_name);
        }

        public void bind(String name) {
            bookname.setText(name);

        }
    }
}
