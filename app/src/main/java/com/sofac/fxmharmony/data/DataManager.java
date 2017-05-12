package com.sofac.fxmharmony.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.data.dto.ManagerInfoDTO;
import com.sofac.fxmharmony.data.dto.base.ServerRequest;
import com.sofac.fxmharmony.data.dto.base.ServerResponse;

import java.io.IOException;
import java.lang.reflect.Type;

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


        String response = sendRequest(serverRequest);
        Timber.i(response);

        if (!response.equals(Constants.SERVER_REQUEST_ERROR)) {

            Type authorizationType = new TypeToken<ServerResponse<ManagerInfoDTO>>() {
            }.getType();

            ServerResponse<ManagerInfoDTO> managerInfoServerResponse = new Gson().fromJson(response, authorizationType);

            return managerInfoServerResponse;

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


    private String sendRequest(ServerRequest serverRequest) {

        Call<ResponseBody> call = requestResponseService.postAuthorizationRequest(serverRequest);

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
