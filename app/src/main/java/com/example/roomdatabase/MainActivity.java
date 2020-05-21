package com.example.roomdatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.roomdatabase.myAdapter.MyAdapter;
import com.example.roomdatabase.myUtils.Word;
import com.example.roomdatabase.myUtils.WordViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    WordViewModel wordViewModel;

    Button mBtnInsert, mBtnDelete, mBtnUpdate, mBtnClear;
    RecyclerView mRvContainer;
    MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        mRvContainer.setLayoutManager(new LinearLayoutManager(this));
        mRvContainer.setAdapter(myAdapter);


        wordViewModel.getAllWordLive().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                myAdapter.setAllWords(words);
                myAdapter.notifyDataSetChanged();
            }
        });

        setListeners();
    }

    private void init(){
//        mTvShow = findViewById(R.id.tv_show);
        mBtnInsert = findViewById(R.id.btn_add);
        mBtnDelete = findViewById(R.id.btn_del);
        mBtnUpdate = findViewById(R.id.btn_update);
        mBtnClear = findViewById(R.id.btn_clear);
        mRvContainer = findViewById(R.id.rv_container);

//        wordViewModel = new WordViewModel(this.getApplication());
        wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        myAdapter = new MyAdapter();
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        mBtnInsert.setOnClickListener(onClick);
        mBtnDelete.setOnClickListener(onClick);
        mBtnUpdate.setOnClickListener(onClick);
        mBtnClear.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_add:
                    Word word_1 = new Word("Hello", "你好");
                    Word word_2 = new Word("append", "添加");
                    Word word_3 = new Word("stupid", "愚蠢的");
                    wordViewModel.insertWords(word_1, word_2, word_3);
//                    wordDao.insertWords(word_1, word_2, word_3);
                    break;
                case R.id.btn_del:
                    Word word_del= new Word("Hi", "你好");
                    word_del.setId(6);
                    wordViewModel.deleteWords(word_del);
                    break;
                case R.id.btn_update:
                    Word word_update  = new Word("Hi", "你好");
                    word_update.setId(10);
                    wordViewModel.updateWords(word_update);
//                    wordDao.updataWords(word_update);
                    break;
                case R.id.btn_clear:
                    wordViewModel.clearWords();
                    break;
            }
        }
    }



}
