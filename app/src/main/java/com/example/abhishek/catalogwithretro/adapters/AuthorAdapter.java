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
import com.example.abhishek.catalogwithretro.model.Genre;

import java.util.List;

/**
 * Created by abhishek on 13/11/17.
 */

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder>{

    private List<Author> authorList;
    private RecyclerClickListener recyclerClickListener;
    public static final int ACTION_EDIT=0;
    public static final int ACTION_DELETE=1;


    public AuthorAdapter(List<Author> authorListlist, RecyclerClickListener recyclerClickListener) {
        this.authorList = authorListlist;
        this.recyclerClickListener = recyclerClickListener;
    }

    @Override
    public AuthorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.author_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem,
                parent,false);
        AuthorViewHolder authorViewHolder = new AuthorViewHolder(view);
        return authorViewHolder;
    }

    @Override
    public void onBindViewHolder(AuthorViewHolder holder,int position) {
        final Author author = authorList.get(position);
        holder.bind(author);
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerClickListener.onAction(authorList.indexOf(author),ACTION_EDIT);
            }
        });
        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerClickListener.onAction(authorList.indexOf(author),ACTION_DELETE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return authorList.size();
    }

    public void setAuthors(List<Author> authorList) {
        this.authorList=authorList;
        notifyDataSetChanged();
    }
    class AuthorViewHolder extends RecyclerView.ViewHolder{

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

        void bind(final Author author){
            tv_auth_name.setText(author.getName());
            tv_auth_id.setText(author.getId());
            tv_auth_language.setText(author.getLanguage());
            tv_auth_country.setText(author.getCountry());
        }
    }
}
