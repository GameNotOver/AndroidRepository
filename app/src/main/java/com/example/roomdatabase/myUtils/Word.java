package com.example.roomdatabase.myUtils;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Word {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "english_word")
    private String englishWord;
    @ColumnInfo(name = "chinese_meaning")
    private String chineseMean;
    @ColumnInfo(name = "chinese_visible")
    private boolean chineseVisible;

    public boolean isChineseVisible() {
        return chineseVisible;
    }

    public void setChineseVisible(boolean chineseVisible) {
        this.chineseVisible = chineseVisible;
    }

    public Word(String englishWord, String chineseMean) {
        this.englishWord = englishWord;
        this.chineseMean = chineseMean;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public void setChineseMean(String chineseMean) {
        this.chineseMean = chineseMean;
    }

    public int getId() {
        return id;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public String getChineseMean() {
        return chineseMean;
    }
}
