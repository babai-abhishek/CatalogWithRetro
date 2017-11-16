package com.example.abhishek.catalogwithretro.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.model.Book;

import java.util.List;

/**
 * Created by abhishek on 15/11/17.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder>{

    private List<Book> books;
    RecyclerClickListener recyclerClickListener;

    public static final int ACTION_EDIT=0;
    public static final int ACTION_DELETE=1;

    public BookAdapter(List<Book> books, RecyclerClickListener recyclerClickListener) {
        this.books = books;
        this.recyclerClickListener = recyclerClickListener;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.book_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem,
                parent,false);
        return new BookViewHolder(view);

    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        final Book book = books.get(position);
        holder.Bind(book);
        holder.btnEditBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerClickListener.onAction(books.indexOf(book),ACTION_EDIT);
            }
        });
        holder.btnDeleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerClickListener.onAction(books.indexOf(book),ACTION_DELETE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();

    }

    public void setAdapter(List<Book> books){
        this.books = books;
        notifyDataSetChanged();
    }

    class BookViewHolder extends RecyclerView.ViewHolder{

        TextView tvBookName, tvBookLanguage, tvBookPublishDate, tvBookNoOfPages, tvBookId;
        Button btnEditBook, btnDeleteBook;

        public BookViewHolder(View itemView) {
            super(itemView);

            tvBookName = (TextView) itemView.findViewById(R.id.tv_book_name);
            tvBookId = (TextView) itemView.findViewById(R.id.tv_book_id);
            tvBookLanguage = (TextView) itemView.findViewById(R.id.tv_book_language);
            tvBookPublishDate = (TextView) itemView.findViewById(R.id.tv_book_dateOfPublish);
            tvBookNoOfPages = (TextView) itemView.findViewById(R.id.tv_book_pages);

            btnEditBook = (Button) itemView.findViewById(R.id.btn_book_edit);
            btnDeleteBook = (Button) itemView.findViewById(R.id.btn_book_delete);
        }

        void Bind(final Book book){

            tvBookName.setText(book.getName());
            tvBookId.setText(book.getId());
            tvBookLanguage.setText(book.getLanguage());
            tvBookPublishDate.setText(book.getPublished());
            tvBookNoOfPages.setText(String.valueOf(book.getPages()));

        }
    }
}
