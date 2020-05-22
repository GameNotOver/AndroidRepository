package com.example.roomdatabase.myUtils;

import android.app.Application;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.roomdatabase.myInterface.WordDao;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    private WordDao wordDao;
    private WordRepository wordRepository;

    public WordViewModel(@NonNull Application application) {
        super(application);
        wordRepository = new WordRepository(application);
    }

    public LiveData<List<Word>> getAllWordLive(){
        return wordRepository.getAllWordsLive();
    }

    public void insertWords(Word... words){
        wordRepository.insertWords(words);
    }

    public void deleteWords(Word... words){
        wordRepository.deleteWords(words);
    }

    public void updateWords(Word... words){
        wordRepository.updateWords(words);
    }

    public void clearWords(Word... words){
        wordRepository.clearWords();
    }




}
