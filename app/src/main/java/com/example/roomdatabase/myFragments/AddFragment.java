package com.example.roomdatabase.myFragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.roomdatabase.R;
import com.example.roomdatabase.myUtils.Word;
import com.example.roomdatabase.myUtils.WordViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {

    private Button mBtnSubmit;
    private EditText mEtWord, mEtMean;
    private WordViewModel wordViewModel;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final FragmentActivity activity = requireActivity();
        mBtnSubmit = activity.findViewById(R.id.btn_submit);
        mEtWord = activity.findViewById(R.id.et_input_word);
        mEtMean = activity.findViewById(R.id.et_input_mean);

        wordViewModel = new ViewModelProvider(requireActivity()).get(WordViewModel.class);

        mBtnSubmit.setEnabled(false);
        mEtWord.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.showSoftInput(mEtWord, 0);

        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String word = mEtWord.getText().toString().trim();
                String mean = mEtMean.getText().toString().trim();
                mBtnSubmit.setEnabled( !word.isEmpty() && !mean.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mEtWord.addTextChangedListener(textWatcher);
        mEtMean.addTextChangedListener(textWatcher);
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = mEtWord.getText().toString().trim();
                String mean = mEtMean.getText().toString().trim();
                Word wordTemplete = new Word(word, mean);
                wordViewModel.insertWords(wordTemplete);
                NavController navController = Navigation.findNavController(v);
                navController.navigateUp();

                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
    }
}
