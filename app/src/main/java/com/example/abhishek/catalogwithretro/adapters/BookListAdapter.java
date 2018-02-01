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

public class BookListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Book> bookList;
    RecyclerClickListener recyclerClickListener;

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_EMPTY = 1;

    private boolean isLoading = false;

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyDataSetChanged();
    }

    public boolean isLoading() {
        return isLoading;
    }
    public BookListAdapter(List<Book> bookList, RecyclerClickListener recyclerClickListener) {
        this.bookList = bookList;
        this.recyclerClickListener = recyclerClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if(bookList.size()<1 || isLoading())
            return VIEW_TYPE_EMPTY;
        else
            return VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        switch (viewType){
            case VIEW_TYPE_ITEM:
                view = inflater.inflate( R.layout.book_list_item,
                        parent,false);
                BookViewHolder bookViewHolder = new BookViewHolder(view);
                return bookViewHolder;
            default:
                view = inflater.inflate(R.layout.book_list_empty_item,
                        parent, false);
                EmptyListViewHolder emptyListViewHolder = new EmptyListViewHolder(view);
                return emptyListViewHolder;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (bookList.size() < 1 || isLoading()) {
            ((EmptyListViewHolder) holder).emptyListMessage.setText(isLoading() ? "Loading...." : "No items found!");
        }
        else {
            BookViewHolder bookViewHolder = (BookViewHolder) holder;
            final Book book = bookList.get(position);
            bookViewHolder.Bind(book);
            bookViewHolder.bookListCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerClickListener.onItemClick(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        int bookListSize = bookList.size();
        return bookListSize<1 || isLoading() ? 1 : bookListSize;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
        notifyDataSetChanged();
    }

    class BookViewHolder extends RecyclerView.ViewHolder{

        TextView tvBookName,tvBookId;
        CardView bookListCardView;

        public BookViewHolder(View itemView) {
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

    private class EmptyListViewHolder extends RecyclerView.ViewHolder {

        TextView emptyListMessage;

        public EmptyListViewHolder(View itemView) {
            super(itemView);
            emptyListMessage = (TextView) itemView.findViewById(R.id.book_empty_message);

        }
    }
}
