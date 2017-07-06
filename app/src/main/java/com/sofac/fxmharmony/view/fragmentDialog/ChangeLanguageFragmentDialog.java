package com.sofac.fxmharmony.view.fragmentDialog;


import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.sofac.fxmharmony.R;

public class ChangeLanguageFragmentDialog  extends DialogFragment {



    public static ChangeLanguageFragmentDialog newInstance() {
        return new ChangeLanguageFragmentDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View languageFragmentDialog = inflater.inflate(R.layout.dialog_change_language, null);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        return languageFragmentDialog;
    }
}