package com.kita.skdnews.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.kita.skdnews.MainActivity;
import com.kita.skdnews.R;
import com.kita.skdnews.db.DSSettings;
import com.kita.skdnews.helper.Extra;
import com.tiper.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

public class SettingFragment extends Fragment {
    private static String TAG = SettingFragment.class.getSimpleName();

    MaterialSpinner spCountry, spLanguage, spCategory;
    TextInputEditText inApikey;
    ArrayAdapter<String> countryAdapter, languageAdapter, categoryAdapter;

    List<String> country = new ArrayList<String>();
    List<String> language = new ArrayList<String>();
    List<String> category = new ArrayList<String>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        ActionBar action = ((AppCompatActivity)getActivity()).getSupportActionBar();

        action.setTitle("Setting");
        action.setSubtitle("");

        MainActivity.btnSearch.setVisibility(View.GONE);

        inApikey = root.findViewById(R.id.input_apikey);
        spCountry = root.findViewById(R.id.input_country);
        spLanguage = root.findViewById(R.id.input_language);
        spCategory = root.findViewById(R.id.input_category);
        Button btnSubmit = root.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inApikey.getText().length() < 23 || spCountry.getSelection() < 0 ||
                        spLanguage.getSelection()<0 || spCategory.getSelection() < 0){
                    Toast.makeText(getContext(), "Update failed due to invalid entry!", Toast.LENGTH_LONG).show();
                }else{
                    MainActivity.API_KEY = String.valueOf(inApikey.getText());
                    MainActivity.Country = getResources().getStringArray(R.array.country)[spCountry.getSelection()].split(";")[0];
                    MainActivity.Language = getResources().getStringArray(R.array.language)[spLanguage.getSelection()].split(";")[0];
                    MainActivity.Category = getResources().getStringArray(R.array.category)[spCategory.getSelection()].split(";")[0];

                    Extra.updateSetting();
                    Toast.makeText(getContext(), "Update success ...", Toast.LENGTH_LONG).show();
                }
            }
        });

        country = Extra.getLabelFromResource(getResources().getStringArray(R.array.country));
        language = Extra.getLabelFromResource(getResources().getStringArray(R.array.language));
        category = Extra.getLabelFromResource(getResources().getStringArray(R.array.category));

        countryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, country);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCountry.setAdapter(countryAdapter);

        languageAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, language);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLanguage.setAdapter(languageAdapter);

        categoryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, category);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(categoryAdapter);

        getCurrentSetting();


        return root;
    }

    private void getCurrentSetting(){
        DSSettings st =  MainActivity.db.getDAOSetting().getSettingByID(1);
        if (st == null) return;

        inApikey.setText(String.valueOf(st.apikey));
        spCountry.setSelection(Extra.getIndexOfList(getResources().getStringArray(R.array.country),st.country));
        spLanguage.setSelection(Extra.getIndexOfList(getResources().getStringArray(R.array.language),st.language));
        spCategory.setSelection(Extra.getIndexOfList(getResources().getStringArray(R.array.category),st.category));
    }
}
