package com.example.abhishek.catalogwithretro.activity.book.GenreTypeSelection;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.activity.book.SelectionListener;
import com.example.abhishek.catalogwithretro.model.Genre;

import java.util.List;

/**
 * Created by abhishek on 14/1/18.
 */

public class Select_genre_type_adapter extends RecyclerView.Adapter<Select_genre_type_adapter.GenreTypeViewHolder> {

    private List<Genre> genreList;
    private SelectionListener selectionListener;

    public Select_genre_type_adapter(List<Genre> list, SelectionListener selectionListener) {
        this.genreList = list;
        this.selectionListener = selectionListener;
    }

    @Override
    public GenreTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.select_genre_type_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem,
                parent,false);
        GenreTypeViewHolder genreTypeViewHolder = new GenreTypeViewHolder(view);
        return genreTypeViewHolder;
    }

    @Override
    public void onBindViewHolder(GenreTypeViewHolder holder, int position) {
        final Genre genre = genreList.get(position);
        holder.bind(genre);
        holder.genreTypeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionListener.onSelect(genreList.indexOf(genre));
            }
        });
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    public void setGenreTypes(List<Genre> genres) {
        this.genreList=genres;
        notifyDataSetChanged();
    }

    class GenreTypeViewHolder extends RecyclerView.ViewHolder{

        TextView genreTypeTextView;

        public GenreTypeViewHolder(View itemView) {
            super(itemView);

            genreTypeTextView = (TextView) itemView.findViewById(R.id.select_genre_type_list_item);
        }

        public void bind(Genre genre) {
            genreTypeTextView.setText(genre.getName());
        }
    }
}
