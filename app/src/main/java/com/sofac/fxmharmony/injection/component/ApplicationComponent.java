package com.sofac.fxmharmony.injection.component;

import android.app.Application;
import android.content.Context;

import com.sofac.fxmharmony.data.DataManager;
import com.sofac.fxmharmony.data.SyncService;
import com.sofac.fxmharmony.data.local.DatabaseHelper;
import com.sofac.fxmharmony.data.local.PreferencesHelper;
import com.sofac.fxmharmony.data.remote.RibotsService;
import com.sofac.fxmharmony.injection.ApplicationContext;
import com.sofac.fxmharmony.injection.module.ApplicationModule;
import com.sofac.fxmharmony.util.RxEventBus;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(SyncService syncService);

    @ApplicationContext
    Context context();
    Application application();
    RibotsService ribotsService();
    PreferencesHelper preferencesHelper();
    DatabaseHelper databaseHelper();
    DataManager dataManager();
    RxEventBus eventBus();

}
