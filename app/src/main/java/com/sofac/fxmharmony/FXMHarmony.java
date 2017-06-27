package com.sofac.fxmharmony;

import android.app.Application;
import android.util.Log;

import com.orm.SchemaGenerator;
import com.orm.SugarApp;
import com.orm.SugarDb;
import com.sofac.fxmharmony.data.dto.CommentDTO;
import com.sofac.fxmharmony.util.FakeCrashLibrary;

import java.util.Date;

import timber.log.Timber;

/**
 * Created by Maxim on 30.03.2017.
 * Always run when app is start
 */

public class FXMHarmony  extends SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }

        // create table if not exists
        SchemaGenerator schemaGenerator = new SchemaGenerator(this);
        schemaGenerator.createDatabase(new SugarDb(this).getDB());

    }

    /** A tree which logs important information for crash reporting. */
    private static class CrashReportingTree extends Timber.Tree {

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            FakeCrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(t);
                }
            }
        }
    }

}
