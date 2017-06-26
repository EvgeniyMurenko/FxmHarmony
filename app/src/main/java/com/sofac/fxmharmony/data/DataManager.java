package com.sofac.fxmharmony.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.data.dto.CommentDTO;
import com.sofac.fxmharmony.data.dto.ManagerInfoDTO;
import com.sofac.fxmharmony.data.dto.PostDTO;
import com.sofac.fxmharmony.data.dto.base.ServerRequest;
import com.sofac.fxmharmony.data.dto.base.ServerResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import timber.log.Timber;

public class DataManager {

    private static final DataManager ourInstance = new DataManager();
    RequestResponseService requestResponseService;

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {
        requestResponseService = RequestResponseService.Creator.newAuthorizationService();
    }


    public ServerResponse<ManagerInfoDTO> sendAuthorizationRequest(ServerRequest serverRequest) {


        String response = sendRequest(serverRequest , Constants.APP_EXCHANGE);
        Timber.i(response);

        if (!response.equals(Constants.SERVER_REQUEST_ERROR)) {

            Type authorizationType = new TypeToken<ServerResponse<ManagerInfoDTO>>() {
            }.getType();

            ServerResponse<ManagerInfoDTO> managerInfoServerResponse = new Gson().fromJson(response, authorizationType);

            return managerInfoServerResponse;

        }

        return null;
    }

    public ServerResponse postGroupRequest(ServerRequest serverRequest , String groupRequestType) {

        String response = sendRequest(serverRequest, Constants.GROUP_EXCHANGE);
        Log.i("TEST" , response);
        Timber.i(response);

        if (!response.equals(Constants.SERVER_REQUEST_ERROR)) {
            Type authorizationType = null;

            if (groupRequestType.equals(Constants.LOAD_ALL_POSTS_REQUEST)){
                Log.i("TEST" , "LOAD_ALL_POSTS_REQUEST");
                authorizationType = new TypeToken<ServerResponse<List<PostDTO>>>() {
                }.getType();
            } else if (groupRequestType.equals(Constants.LOAD_COMMENTS_REQUEST)){
                authorizationType = new TypeToken<ServerResponse<List<CommentDTO>>>() {
                }.getType();
            } else {
                authorizationType = new TypeToken<ServerResponse>(){}.getType();
            }

            ServerResponse<List<PostDTO>> serverResponsee =  new Gson().fromJson(response, authorizationType);
            return new Gson().fromJson(response, authorizationType);

        }

        return null;

    }
//
//    public ServerResponse<List<>> getCaseRequest (ServerRequest serverRequest) {
//
//        String response = sendRequest(serverRequest);
//        Timber.i(response);
//
//        if (!response.equals(Constants.SERVER_REQUEST_ERROR)) {
//
//            Type caseType = new TypeToken<ServerResponse<List<>>>() {
//            }.getType();
//
//            ServerResponse<List<>> listCaseDTOServerResponse = new Gson().fromJson(response, caseType);
//
//            return listCaseDTOServerResponse;
//
//        }
//
//        return null;
//    }


    private String sendRequest(ServerRequest serverRequest, String type) {

        Call<ResponseBody> call = null;

        if (type.equals(Constants.APP_EXCHANGE)) {
            call = requestResponseService.postAuthorizationRequest(serverRequest);
        } else if (type.equals(Constants.GROUP_EXCHANGE)) {
            call = requestResponseService.postGroupRequest(serverRequest);
        }
        String response = Constants.SERVER_REQUEST_ERROR;

        try (ResponseBody responseBody = call.execute().body()) {
            if (responseBody != null) {
                response = responseBody.string();
            } else {
                return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return response;
        }

        return response;
    }

}
