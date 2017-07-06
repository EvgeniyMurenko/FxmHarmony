package com.sofac.fxmharmony.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.util.AppMethods;
import com.sofac.fxmharmony.view.fragmentDialog.ChangeLanguageFragmentDialog;
import com.sofac.fxmharmony.view.fragmentDialog.ChangeNameFragmentDialog;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class SettingsActivity extends AppCompatActivity {

    private ImageView avatarImage;
    private PopupMenu photoMenu;

    private TextView userName;
    private TextView userPosition;

    private LinearLayout languageButton;
    private Switch pushMessageSwitch;

    private static final int PICK_PHOTO_FOR_AVATAR = 101;


    private ChangeNameFragmentDialog changeNameFragmentDialog;
    private ChangeLanguageFragmentDialog changeLanguageFragmentDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        avatarImage = (ImageView) findViewById(R.id.avatarImage);

        photoMenu = new PopupMenu(SettingsActivity.this, avatarImage);
        MenuInflater inflater = photoMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_settings_photo, photoMenu.getMenu());

        userName = (TextView) findViewById(R.id.userName);
        userPosition = (TextView) findViewById(R.id.userPosition);
        pushMessageSwitch = (Switch) findViewById(R.id.pushMessagesSwitch);
        languageButton = (LinearLayout) findViewById(R.id.languageButton);

        changeNameFragmentDialog = ChangeNameFragmentDialog.newInstance();
        changeLanguageFragmentDialog = ChangeLanguageFragmentDialog.newInstance();

        userName.setText("User userovich");
        userPosition.setText("Test user");

        avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoMenu.show();
            }
        });

        photoMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.photo_camera) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, PICK_PHOTO_FOR_AVATAR);
                    return true;
                }
                if (id == R.id.photo_gallery) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
                    return true;
                }
                if (id == R.id.photo_delete) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setTitle(R.string.delete_photo_question);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
                return onOptionsItemSelected(item);
            }
        });

        pushMessageSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppMethods.getPushState(SettingsActivity.this) != Constants.PUSH_ON) {
                    pushMessageSwitch.setChecked(true);
                } else {
                    pushMessageSwitch.setChecked(false);
                }
                AppMethods.changePushState(SettingsActivity.this);
            }
        });
        if (AppMethods.getPushState(this) == Constants.PUSH_ON) {
            pushMessageSwitch.setChecked(true);
        } else {
            pushMessageSwitch.setChecked(false);
        }

        languageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguageFragmentDialog.show(getFragmentManager().beginTransaction(), "ChangeLanguageFragmentDialog");
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            Bitmap photo = (Bitmap) data.getExtras().get("data");

           /* try {
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.editName) {
            changeNameFragmentDialog.show(getFragmentManager().beginTransaction(), "ChangeNameFragmentDialog");
            return true;
        }

        if (id == R.id.logout) {
            Toast.makeText(this, "Logout", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
