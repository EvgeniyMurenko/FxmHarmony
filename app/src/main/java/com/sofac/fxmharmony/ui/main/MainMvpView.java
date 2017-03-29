package com.sofac.fxmharmony.ui.main;

import com.sofac.fxmharmony.data.model.Ribot;
import com.sofac.fxmharmony.ui.base.MvpView;

import java.util.List;



public interface MainMvpView extends MvpView {

    void showRibots(List<Ribot> ribots);

    void showRibotsEmpty();

    void showError();

}
