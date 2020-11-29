package com.kita.skdnews.helper;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.kita.skdnews.R;

public class BottomDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    public static final String TAG = BottomDialog.class.getName();

    private ItemClickListener mListener;
    private TextInputEditText inputKeyword;

    public static BottomDialog newInstance() {
        return new BottomDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.bottom_layout, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView btnSearch = view.findViewById(R.id.bottomBtnSearch);
        inputKeyword = view.findViewById(R.id.inputKeyword);
        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListener) {
            mListener = (ItemClickListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override public void onClick(View view) {
        mListener.onItemClick(String.valueOf(inputKeyword.getText()));
        dismiss();
    }

    public interface ItemClickListener {
        void onItemClick(String str);
    }
}
