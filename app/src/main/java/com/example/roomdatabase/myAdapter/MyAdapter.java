package com.example.roomdatabase.myAdapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomdatabase.R;
import com.example.roomdatabase.myUtils.Word;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Word> allWords = new ArrayList<>();

    public void setAllWords(List<Word> allWords) {
        this.allWords = allWords;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Word word = allWords.get(position);
        holder.mTvId.setText(String.valueOf(position + 1));
        holder.mTvWord.setText(word.getEnglishWord());
        holder.mTvMean.setText(word.getChineseMean());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://m.youdao.com/dict?le=eng&q=" + holder.mTvWord.getText());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allWords.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mTvId, mTvWord, mTvMean;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvId = itemView.findViewById(R.id.tv_id);
            mTvWord = itemView.findViewById(R.id.tv_word);
            mTvMean = itemView.findViewById(R.id.tv_mean);
        }
    }
}
