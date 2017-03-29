package com.sofac.fxmharmony;

import android.app.Application;
import android.content.Context;

import com.sofac.fxmharmony.injection.component.ApplicationComponent;
import com.sofac.fxmharmony.injection.component.DaggerApplicationComponent;
import com.sofac.fxmharmony.injection.module.ApplicationModule;


public class FxmHarmonyApplication extends Application  {

    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public static FxmHarmonyApplication get(Context context) {
        return (FxmHarmonyApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}