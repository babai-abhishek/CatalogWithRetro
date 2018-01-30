package com.example.abhishek.catalogwithretro.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.activity.book.RecyclerClickListener;
import com.example.abhishek.catalogwithretro.model.Book;

import java.util.List;

/**
 * Created by abhishek on 17/1/18.
 */

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookAdapter> {

    private List<Book> bookList;
    RecyclerClickListener recyclerClickListener;

    public BookListAdapter(List<Book> bookList, RecyclerClickListener recyclerClickListener) {
        this.bookList = bookList;
        this.recyclerClickListener = recyclerClickListener;
    }

    @Override
    public BookAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.book_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem,
                parent,false);
        return new BookAdapter(view);
    }

    @Override
    public void onBindViewHolder(BookAdapter holder, final int position) {
        final Book book = bookList.get(position);
        holder.Bind(book);
        holder.bookListCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
        notifyDataSetChanged();
    }

    class BookAdapter extends RecyclerView.ViewHolder{

        TextView tvBookName,tvBookId;
        CardView bookListCardView;

        public BookAdapter(View itemView) {
            super(itemView);

            tvBookName = (TextView) itemView.findViewById(R.id.tv_book_name_list);
            tvBookId = (TextView) itemView.findViewById(R.id.tv_book_id_list);
            bookListCardView = (CardView) itemView.findViewById(R.id.book_list_item_cardView);
        }

        void Bind(Book book){
            tvBookName.setText(book.getName());
            tvBookId.setText(book.getId());
        }
    }
}
