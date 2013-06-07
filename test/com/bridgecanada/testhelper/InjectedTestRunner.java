package com.bridgecanada.testhelper;

import android.app.Application;
import com.bridgecanada.prismatic.PrismaticApplication;
import com.bridgecanada.prismatic.di.PrismaticTestModule;
import com.google.inject.Injector;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.runners.model.InitializationError;
import roboguice.RoboGuice;
import roboguice.inject.ContextScope;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 05/05/13
 * Time: 11:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class InjectedTestRunner extends RobolectricTestRunner {
    public InjectedTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override public void prepareTest(Object test) {

        System.out.println("Preparing test for "+test);
        //PrismaticApplication sampleApplication = (PrismaticApplication) Robolectric.application;
        //Injector injector = sampleApplication.getInjector();
        Application application =  Robolectric.application;

        PrismaticTestModule module = new PrismaticTestModule();


        RoboGuice.setBaseApplicationInjector(application,
                RoboGuice.DEFAULT_STAGE,
                RoboGuice.newDefaultRoboModule(application),
                module);

        Injector injector = RoboGuice.getInjector(application);
        //.injectMembers(test);

//        Injector injector = application.getInjector();
        ContextScope scope = injector.getInstance(ContextScope.class);
        scope.enter(application);
        injector.injectMembers(test);


    }
}
