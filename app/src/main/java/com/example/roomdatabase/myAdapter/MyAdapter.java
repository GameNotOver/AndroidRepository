package com.example.roomdatabase.myAdapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomdatabase.R;
import com.example.roomdatabase.myUtils.Word;
import com.example.roomdatabase.myUtils.WordViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Word> allWords = new ArrayList<>();
    private WordViewModel wordViewModel;

    public MyAdapter(WordViewModel wordViewModel) {
        this.wordViewModel = wordViewModel;
    }

    public void setAllWords(List<Word> allWords) {
        this.allWords = allWords;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_normal, parent, false);

        final MyViewHolder holder = new MyViewHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://m.youdao.com/dict?le=eng&q=" + holder.mTvWord.getText());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.mSwtVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Word word = (Word) holder.itemView.getTag(R.id.word_for_view_holder);

                if(isChecked){
                    holder.mTvMean.setVisibility(View.VISIBLE);
                    word.setChineseVisible(true);
                    wordViewModel.updateWords(word);
                }else {
                    holder.mTvMean.setVisibility(View.GONE);
                    word.setChineseVisible(false);
                    wordViewModel.updateWords(word);
                }
            }
        });


//        return new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Word word = allWords.get(position);

        holder.itemView.setTag(R.id.word_for_view_holder, word);

        holder.mTvId.setText(String.valueOf(position + 1));
        holder.mTvWord.setText(word.getEnglishWord());
        holder.mTvMean.setText(word.getChineseMean());

        if(word.isChineseVisible()){
            holder.mTvMean.setVisibility(View.VISIBLE);
            holder.mSwtVisible.setChecked(true);
        }else {
            holder.mTvMean.setVisibility(View.GONE);
            holder.mSwtVisible.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return allWords.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mTvId, mTvWord, mTvMean;
        Switch mSwtVisible;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvId = itemView.findViewById(R.id.tv_id);
            mTvWord = itemView.findViewById(R.id.tv_word);
            mTvMean = itemView.findViewById(R.id.tv_mean);
            mSwtVisible = itemView.findViewById(R.id.swt_visible);
        }
    }
}
