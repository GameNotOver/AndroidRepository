package com.example.roomdatabase.myFragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.example.roomdatabase.R;
import com.example.roomdatabase.myAdapter.MyAdapter;
import com.example.roomdatabase.myUtils.Word;
import com.example.roomdatabase.myUtils.WordViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WordsFragment extends Fragment {
    private WordViewModel wordViewModel;
    private RecyclerView mRvWords;
    private MyAdapter myAdapter;
    private LiveData<List<Word>> filteredWords;

    public WordsFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_words, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        final FloatingActionButton mAddBtn = requireActivity().findViewById(R.id.word_btn_add);

        super.onActivityCreated(savedInstanceState);
        wordViewModel = new ViewModelProvider(requireActivity()).get(WordViewModel.class);
        mRvWords = requireActivity().findViewById(R.id.rv_words);
        mRvWords.setLayoutManager(new LinearLayoutManager(requireActivity()));
        myAdapter = new MyAdapter(wordViewModel);
        mRvWords.setAdapter(myAdapter);
        mRvWords.setItemAnimator(new DefaultItemAnimator(){
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                super.onAnimationFinished(viewHolder);
                LinearLayoutManager manager = (LinearLayoutManager) mRvWords.getLayoutManager();
                if (manager != null) {
                    int firstPosition = manager.findFirstVisibleItemPosition();
                    int lastPosition = manager.findLastVisibleItemPosition();
                    for (int i = firstPosition; i<lastPosition; i++){
                        MyAdapter.MyViewHolder holder = (MyAdapter.MyViewHolder) mRvWords.findViewHolderForAdapterPosition(i);
                        if (holder != null){
                            holder.mTvId.setText(String.valueOf(i + 1));
                        }
                    }
                }

            }
        });

        filteredWords = wordViewModel.getAllWordLive();

        wordViewModel.getAllWordLive().observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                int temp = myAdapter.getItemCount();
//                myAdapter.setAllWords(words);
                if(temp != words.size()){
                    myAdapter.submitList(words);
//                    myAdapter.notifyDataSetChanged();
                }
            }
        });

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_wordsFragment_to_addFragment);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow( getView().getWindowToken(), 0);
        }
    }

    //菜单选项
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.clear_data:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("清空数据");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wordViewModel.clearWords();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setMaxWidth(1000);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }


            //搜索框
            @Override
            public boolean onQueryTextChange(String newText) {
                String patten = newText.trim();

                filteredWords.removeObservers(requireActivity()); //!!!!!!!!重要，搜索时会重新添加观察，所以应该首先清除原有的观察

                filteredWords = wordViewModel.findWordsLiveWithPatten(patten);
                filteredWords.observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        int temp = myAdapter.getItemCount();
//                        myAdapter.setAllWords(words);
                        if(temp != words.size()){
                            mRvWords.smoothScrollBy(0, -200);
                            myAdapter.submitList(words);
//                            myAdapter.notifyDataSetChanged();
                        }
                    }
                });
                return true;
            }
        });
    }


}
