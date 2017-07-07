package com.sofac.fxmharmony.view.fragmentDialog;


import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;

import static com.sofac.fxmharmony.R.id.textView;

public class ChangeNameFragmentDialog extends DialogFragment {

    private EditText newUserNameInput;
    private Button changeNameButton;

    public static ChangeNameFragmentDialog newInstance() {
        return new ChangeNameFragmentDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View changeNameFragmentDialog = inflater.inflate(R.layout.dialog_change_name, null);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        newUserNameInput = (EditText) changeNameFragmentDialog.findViewById(R.id.newUserNameInput);
        changeNameButton = (Button) changeNameFragmentDialog.findViewById(R.id.changeNameButton);

        changeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setApiKey(Constants.CLOUD_API_KEY)
                        .build();
                Translate translate = options.getService();
                final Translation translation =
                        translate.translate(newUserNameInput.getText().toString(),
                                Translate.TranslateOption.targetLanguage("de"));
                Toast.makeText(getActivity(), translation.getTranslatedText(), Toast.LENGTH_SHORT).show();


            }
        });


        return changeNameFragmentDialog;
    }


}
