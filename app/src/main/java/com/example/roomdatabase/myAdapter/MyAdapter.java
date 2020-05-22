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
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomdatabase.R;
import com.example.roomdatabase.myUtils.Word;
import com.example.roomdatabase.myUtils.WordViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ListAdapter<Word, MyAdapter.MyViewHolder> {

//    private List<Word> allWords = new ArrayList<>();
    private WordViewModel wordViewModel;

    public MyAdapter(WordViewModel wordViewModel) {
        super(new DiffUtil.ItemCallback<Word>() {
            @Override
            public boolean areItemsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
                return (oldItem.getEnglishWord().equals(newItem.getEnglishWord())
                && oldItem.getChineseMean().equals(newItem.getChineseMean())
                && oldItem.isChineseVisible() == newItem.isChineseVisible());
            }
        });

        this.wordViewModel = wordViewModel;
    }

//    public void setAllWords(List<Word> allWords) {
//        this.allWords = allWords;
//    }

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
        //对不同的单词进行分配
//        final Word word = allWords.get(position);
        final Word word = getItem(position);

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

//    @Override
//    public int getItemCount() {
//        return allWords.size();
//    }


    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.mTvId.setText(String.valueOf(holder.getAdapterPosition() + 1));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView mTvId;
        private TextView mTvWord;
        private TextView mTvMean;
        private Switch mSwtVisible;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvId = itemView.findViewById(R.id.tv_id);
            mTvWord = itemView.findViewById(R.id.tv_word);
            mTvMean = itemView.findViewById(R.id.tv_mean);
            mSwtVisible = itemView.findViewById(R.id.swt_visible);
        }
    }
}
