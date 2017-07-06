/*
package com.sofac.fxmharmony.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.widget.Toast;

import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.dto.CommentDTO;
import com.sofac.fxmharmony.data.dto.PostDTO;
import com.sofac.fxmharmony.data.dto.base.ServerRequest;
import com.sofac.fxmharmony.data.dto.base.ServerResponse;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.CHANGE_AVATAR_REQUEST;
import static com.sofac.fxmharmony.Constants.CHANGE_NAME_REQUEST;
import static com.sofac.fxmharmony.Constants.DELETE_AVATAR_REQUEST;


public class SettingsExchangeOnServer<T> extends AsyncTask<String, Void, String> {

    private ServerResponse serverResponse;

    private String type;
    private T serverObject;
    private Context context;
    Boolean toDoProgressDialog = false;
    private ProgressDialog pd;

    public interface AsyncResponse {
        void processFinish(Boolean isSuccess);
    }

    private GroupExchangeOnServer.AsyncResponse asyncResponse = null;

    public SettingsExchangeOnServer(T serverObject, Boolean toDoProgressDialog, String type, Context context, GroupExchangeOnServer.AsyncResponse asyncResponse) {
        pd = new ProgressDialog(context, R.style.MyTheme);
        this.serverObject = serverObject;
        this.type = type;
        this.toDoProgressDialog = toDoProgressDialog;
        this.context = context;
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected void onPreExecute() {
        if (toDoProgressDialog) {
            pd.setCancelable(false);
            pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pd.show();
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    protected String doInBackground(String... urls) {

        ServerRequest serverRequest = new ServerRequest(type, null);
        DataManager dataManager = DataManager.getInstance();

        PostDTO postDTO;
        CommentDTO commentDTO;


        switch (type) {
            case CHANGE_NAME_REQUEST:
                Timber.i(serverRequest.toString());
                serverResponse = dataManager.postGroupRequest(serverRequest, type);//postID = serverRequest

                break;

            case CHANGE_AVATAR_REQUEST:

                serverRequest.setDataTransferObject(serverObject);

                serverResponse = dataManager.postGroupRequest(serverRequest, type);

                break;

            case DELETE_AVATAR_REQUEST:

                postDTO = (PostDTO) serverObject;

                serverRequest = new ServerRequest<>(type, postDTO);
                serverResponse = dataManager.postGroupRequest(serverRequest, type);

                break;
        }

        if (serverResponse != null) {
            return serverResponse.getResponseStatus();
        }


        return Constants.SERVER_REQUEST_ERROR;
    }


    @Override
    protected void onPostExecute(String result) {
        Timber.e("Response Server: " + result);

        if (result.equals(Constants.REQUEST_SUCCESS)) {
            asyncResponse.processFinish(true);
        } else {
            Toast.makeText(context, R.string.errorConnection, Toast.LENGTH_SHORT).show();
            asyncResponse.processFinish(false);
        }
        pd.dismiss();
    }

}
*/
