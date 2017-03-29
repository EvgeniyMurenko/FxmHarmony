package com.sofac.fxmharmony.injection.component;


import com.sofac.fxmharmony.injection.PerActivity;
import com.sofac.fxmharmony.injection.module.ActivityModule;
import com.sofac.fxmharmony.ui.main.MainActivity;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

}
