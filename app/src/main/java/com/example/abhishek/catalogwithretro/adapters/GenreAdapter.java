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

import java.util.List;

/**
 * Created by abhishek on 8/11/17.
 */

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder>{
    public static final int ACTION_EDIT=0;
    public static final int ACTION_DELETE=1;
    public static final int ACTION_NAME_LONG_PRESS=2;
    private List<Genre> genreList;
    private Context context;
    private String TAG = "#";

   /* public interface OnItemClickListener {
        void onItemClick(Genre genre);
    }

    private final OnItemClickListener listener;*/

    private RecyclerEditDeleteClickActionListener recyclerEditDeleteClickActionListener;

    public GenreAdapter(List<Genre> list, RecyclerEditDeleteClickActionListener recyclerEditDeleteClickActionListener) {
        this.genreList = list;
       // this.context = context;
        this.recyclerEditDeleteClickActionListener = recyclerEditDeleteClickActionListener;
    }

    @Override
    public GenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.genre_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem,
                parent,false);
        GenreViewHolder genreViewHolder = new GenreViewHolder(view);
        return genreViewHolder;
    }

    @Override
    public void onBindViewHolder(final GenreViewHolder holder, int position) {
        final Genre genre = genreList.get(position);
        holder.bind(genre);
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerEditDeleteClickActionListener.onAction(genreList.indexOf(genre),ACTION_EDIT);
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


    @Override
    public int getItemCount() {
        return genreList.size();
    }

    public void setGenres(List<Genre> genres) {
        this.genreList=genres;
        notifyDataSetChanged();
    }

    class GenreViewHolder extends RecyclerView.ViewHolder{

        TextView genreNameTextView;
        Button btnEdit, btnDelete;

        public GenreViewHolder(View itemView) {
            super(itemView);

            genreNameTextView = (TextView) itemView.findViewById(R.id.genre_list_item_name);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            btnDelete=(Button)itemView.findViewById(R.id.btnDelete);
        }

        void bind(final Genre genre){
            genreNameTextView.setText(genre.getName());
        }
    }
}
