package com.example.abhishek.catalogwithretro.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.model.Genre;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by abhishek on 8/11/17.
 */

public class GenreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ACTION_EDIT = 0;
    public static final int ACTION_DELETE = 1;
    public static final int ACTION_NAME_LONG_PRESS = 2;

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_EMPTY = 1;

    private List<Genre> genreList;
    private String TAG = "#";

    private boolean isLoading=false;

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyDataSetChanged();
    }

    private RecyclerEditDeleteClickActionListener recyclerEditDeleteClickActionListener;

    public GenreAdapter(List<Genre> list, RecyclerEditDeleteClickActionListener recyclerEditDeleteClickActionListener) {
        this.genreList = list;
        // this.context = context;
        this.recyclerEditDeleteClickActionListener = recyclerEditDeleteClickActionListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = inflater.inflate(R.layout.genre_list_item,
                        parent, false);
                GenreViewHolder genreViewHolder = new GenreViewHolder(view);
                return genreViewHolder;
            default:
                view = inflater.inflate(R.layout.genre_list_empty_item,
                        parent, false);
                EmptyViewHolder emptyViewHolder = new EmptyViewHolder(view);
                return emptyViewHolder;
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder rvHolder, int position) {

        if (genreList.size() < 1 || isLoading()) {

            ((EmptyViewHolder)rvHolder).message.setText(isLoading()?"Loading...":"No items found!");
            //show a template saying no genre found. and give a button to add a new genre

        } else {
            GenreViewHolder holder = (GenreViewHolder) rvHolder;
            final Genre genre = genreList.get(position);
            holder.bind(genre);
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerEditDeleteClickActionListener.onAction(genreList.indexOf(genre), ACTION_EDIT);
                }
            });
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerEditDeleteClickActionListener.onAction(genreList.indexOf(genre), ACTION_DELETE);
                }
            });
            holder.genreNameTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    recyclerEditDeleteClickActionListener.onAction(genreList.indexOf(genre), ACTION_NAME_LONG_PRESS);
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if (genreList.size() < 1 || isLoading())
            return VIEW_TYPE_EMPTY;
        else
            return VIEW_TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        int genreSize = genreList.size();
        return genreSize < 1 || isLoading() ? 1 : genreSize;
    }

    public void setGenreList(List<Genre> genres) {
        this.genreList = genres;
        notifyDataSetChanged();
    }

    class GenreViewHolder extends RecyclerView.ViewHolder {

        TextView genreNameTextView, genreIdTextView;
        Button btnEdit, btnDelete;

        public GenreViewHolder(View itemView) {
            super(itemView);

            genreNameTextView = (TextView) itemView.findViewById(R.id.genre_list_item_name);
            genreIdTextView = (TextView) itemView.findViewById(R.id.genre_list_item_id);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }

        void bind(final Genre genre) {
            genreNameTextView.setText(genre.getName());
            genreIdTextView.setText(genre.getId());
        }
    }

    private class EmptyViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        public EmptyViewHolder(View view) {
            super(view);
            message= (TextView) view.findViewById(R.id.message);
        }
    }
}
