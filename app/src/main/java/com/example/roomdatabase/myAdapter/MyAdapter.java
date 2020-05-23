package com.example.roomdatabase.myAdapter;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomdatabase.R;
import com.example.roomdatabase.myUtils.Word;
import com.example.roomdatabase.myUtils.WordViewModel;
import com.example.roomdatabase.myWidget.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ListAdapter<Word, MyAdapter.MyViewHolder> {

//    private List<Word> allWords = new ArrayList<>();
    private WordViewModel wordViewModel;

    private SlidingMenu mScrollingMenu;
    private SlidingMenu mOpenMenu;

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
        View itemView = layoutInflater.inflate(R.layout.cell_slide, parent, false);

        final MyViewHolder holder = new MyViewHolder(itemView);
//        holder.mLlContent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri uri = Uri.parse("https://m.youdao.com/dict?le=eng&q=" + holder.mTvWord.getText());
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(uri);
//                holder.itemView.getContext().startActivity(intent);
//            }
//        });
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
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

        //与SlidingMenu的点击联系
        holder.mSmContainer.setCustomOnClickListener(new SlidingMenu.CustomOnClickListener() {
            @Override
            public void onClick() {
                if(mOnClickListener != null){
                    mOnClickListener.onContentClick(position, word);
                }
            }
        });

        holder.mTvDelMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeOpenMenu();
                if(mOnClickListener != null){
                    mOnClickListener.onDelMenuClick(position);
                }
            }
        });
    }

    public interface OnClickListener{
        void onDelMenuClick(int position);
        void onContentClick(int position, Word word);
    }

    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener){
        this.mOnClickListener = onClickListener;
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
        public TextView mTvWord;
        private TextView mTvMean;
        private Switch mSwtVisible;
        private TextView mTvDelMenu;
        private LinearLayout mLlContent;
        private SlidingMenu mSmContainer;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvId = itemView.findViewById(R.id.tv_id);
            mTvWord = itemView.findViewById(R.id.tv_word);
            mTvMean = itemView.findViewById(R.id.tv_mean);
            mSwtVisible = itemView.findViewById(R.id.swt_visible);
            mTvDelMenu = itemView.findViewById(R.id.tv_menuDel);
            mLlContent = itemView.findViewById(R.id.ll_content);
            mSmContainer = itemView.findViewById(R.id.slidingMenu);
        }
    }

    public SlidingMenu getScrollingMenu(){
        return mScrollingMenu;
    }

    public void setScrollingMenu(SlidingMenu slidingMenu){
        mScrollingMenu = slidingMenu;
    }

    public void holdOpenMenu(SlidingMenu slidingMenu){
        Log.d("catch","yes, i got a menu");
        mOpenMenu = slidingMenu;
    }

    public void closeOpenMenu(){
        if(mOpenMenu != null && mOpenMenu.isOpen()){
            Log.d("catch222222","yes, i close a menu");
            mOpenMenu.closeMenu();
            mOpenMenu = null;
        }
    }
}
