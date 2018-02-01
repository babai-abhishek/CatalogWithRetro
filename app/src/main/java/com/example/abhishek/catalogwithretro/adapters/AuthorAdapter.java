package com.example.abhishek.catalogwithretro.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.model.Author;

import java.util.List;

/**
 * Created by abhishek on 13/11/17.
 */

public class AuthorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Author> authorList;
    private RecyclerEditDeleteClickActionListener recyclerEditDeleteClickActionListener;
    public static final int ACTION_EDIT = 0;
    public static final int ACTION_DELETE = 1;

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

    public AuthorAdapter(List<Author> authorListlist, RecyclerEditDeleteClickActionListener recyclerEditDeleteClickActionListener) {
        this.authorList = authorListlist;
        this.recyclerEditDeleteClickActionListener = recyclerEditDeleteClickActionListener;
    }

    @Override
    public int getItemViewType(int position) {
        /*return super.getItemViewType(position);*/
        if (authorList.size() < 1 || isLoading())
            return VIEW_TYPE_EMPTY;
        else
            return VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = inflater.inflate(R.layout.author_list_item,
                        parent, false);
                AuthorViewHolder authorViewHolder = new AuthorViewHolder(view);
                return authorViewHolder;

            default:
                view = inflater.inflate(R.layout.author_list_empty_item,
                        parent, false);
                EmptyListViewHolder emptyListViewHolder = new EmptyListViewHolder(view);
                return emptyListViewHolder;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (authorList.size() < 1 || isLoading()) {
            ((EmptyListViewHolder) holder).emptyListMessage.setText(isLoading() ? "Loading...." : "No items found!");
        } else {
            AuthorViewHolder authorViewHolder = (AuthorViewHolder) holder;
            final Author author = authorList.get(position);
            authorViewHolder.bind(author);
            authorViewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerEditDeleteClickActionListener.onAction(authorList.indexOf(author), ACTION_EDIT);
                }
            });
            authorViewHolder.btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerEditDeleteClickActionListener.onAction(authorList.indexOf(author), ACTION_DELETE);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int authorListSize = authorList.size();
        return authorListSize < 1 || isLoading() ? 1 : authorListSize;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
        notifyDataSetChanged();
    }

    class AuthorViewHolder extends RecyclerView.ViewHolder {

        TextView tv_auth_name, tv_auth_id, tv_auth_language, tv_auth_country;
        Button btnEdit, btnDel;

        public AuthorViewHolder(View itemView) {
            super(itemView);

            tv_auth_name = (TextView) itemView.findViewById(R.id.tv_author_name);
            tv_auth_id = (TextView) itemView.findViewById(R.id.tv_author_id);
            tv_auth_language = (TextView) itemView.findViewById(R.id.tv_author_language);
            tv_auth_country = (TextView) itemView.findViewById(R.id.tv_author_country);

            btnEdit = (Button) itemView.findViewById(R.id.btn_author_edit);
            btnDel = (Button) itemView.findViewById(R.id.btn_author_delete);
        }

        void bind(final Author author) {
            tv_auth_name.setText(author.getName());
            tv_auth_id.setText(author.getId());
            tv_auth_language.setText(author.getLanguage());
            tv_auth_country.setText(author.getCountry());
        }
    }

    private class EmptyListViewHolder extends RecyclerView.ViewHolder {

        TextView emptyListMessage;

        public EmptyListViewHolder(View itemView) {
            super(itemView);
            emptyListMessage = (TextView) itemView.findViewById(R.id.author_empty_message);

        }
    }
}
