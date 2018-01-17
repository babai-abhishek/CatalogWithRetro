package com.example.abhishek.catalogwithretro.activity.book.AuthorNameSelection;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.activity.book.GenreTypeSelection.Select_genre_type_adapter;
import com.example.abhishek.catalogwithretro.activity.book.SelectionListener;
import com.example.abhishek.catalogwithretro.model.Author;
import com.example.abhishek.catalogwithretro.model.Genre;

import java.util.List;

/**
 * Created by abhishek on 15/1/18.
 */

public class Select_author_name_adapter extends RecyclerView.Adapter<Select_author_name_adapter.AuthorNameViewHolder>{

    private List<Author> authorList;
    private SelectionListener selectionListener;

    public Select_author_name_adapter(List<Author> list, SelectionListener selectionListener) {
        this.authorList = list;
        this.selectionListener = selectionListener;
    }

    @Override
    public AuthorNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.select_author_name_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem,
                parent,false);
        AuthorNameViewHolder nameViewHolder = new AuthorNameViewHolder(view);
        return nameViewHolder;
    }

    @Override
    public void onBindViewHolder(AuthorNameViewHolder holder, int position) {
        final Author author = authorList.get(position);
        holder.bind(author);
        holder.authorNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionListener.onSelect(authorList.indexOf(author));
            }
        });
    }

    @Override
    public int getItemCount() {
        return authorList.size();
    }

    public void setAuthorNames(List<Author> authorNames) {
        this.authorList=authorNames;
        notifyDataSetChanged();
    }


    class AuthorNameViewHolder extends RecyclerView.ViewHolder{

        TextView authorNameTextView;

        public AuthorNameViewHolder(View itemView) {
            super(itemView);
            authorNameTextView = (TextView) itemView.findViewById(R.id.select_author_name_list_item);
        }

        public void bind(Author author) {
            authorNameTextView.setText(author.getName());
        }

    }
}
