package com.sofac.fxmharmony.view.fragmentDialog;


import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.sofac.fxmharmony.R;

public class ChangeNameFragmentDialog extends DialogFragment {


    public static ChangeNameFragmentDialog newInstance() {
        return new ChangeNameFragmentDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View changeNameFragmentDialog = inflater.inflate(R.layout.dialog_change_name, null);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        return changeNameFragmentDialog;
    }


}
