package com.bridgecanada.prismatic.di;


import android.app.Application;

import com.bridgecanada.prismatic.feed.EventDispatchService;
import com.bridgecanada.prismatic.feed.DispatchServiceBase;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import roboguice.RoboGuice;
import roboguice.config.DefaultRoboModule;
import roboguice.inject.RoboInjector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 05/05/13
 * Time: 10:55 PM
 * To change this template use File | Settings | File Templates.
 */
// TODO: integrate this somewhat with PrismaticModule

// SEE: http://eclipsesource.com/blogs/2012/09/25/advanced-android-testing-with-roboguice-and-robolectric/

public class PrismaticTestModule extends AbstractModule {


    private HashMap<Class<?>, Object> bindings;

    public PrismaticTestModule() {
        bindings = new HashMap<Class<?>, Object>();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void configure() {
        // TODO: Inject https://code.google.com/p/google-guice/wiki/FrequentlyAskedQuestions
        //String baseUrl = "https://www..com/Industry";
        String authBaseUrl = "http://localhost:8080";
        String apiBaseUrl = "http://localhost:8080";
        String userAgent = "Prismatic Android Tester";
        String apiVersion = "1.0";

        //bind(IAuthService.class).to(AuthService.class);
        bindConstant().annotatedWith(AuthBaseUrlAnnotation.class).to(authBaseUrl);
        bindConstant().annotatedWith(ApiBaseUrlAnnotation.class).to(apiBaseUrl);
        bindConstant().annotatedWith(UserAgentAnnotation.class).to(userAgent);
        bindConstant().annotatedWith(ApiVersionAnnotation.class).to(apiVersion);
        //bind(PersistentCookieStore.class).to(PersistentCookieStore.class);
        //bind(SharedPreferences.class).toProvider(SharedPreferencesProvider.class);

        //bind(Activity.class).toProvider(ActivityProvider.class).in(ContextSingleton.class);
        Set<Map.Entry<Class<?>, Object>> entries = bindings.entrySet();
        for (Map.Entry<Class<?>, Object> entry : entries) {
            bind((Class<Object>) entry.getKey()).toInstance(entry.getValue());
        }

        install(new FactoryModuleBuilder()
                .implement(DispatchServiceBase.class, EventDispatchService.class)
                        //.implement(DispatchServiceBase.class, AuthEventPublicDispatch.class)
                .build(DispatchServiceBase.IDispatchServiceBaseFactory.class));


    }

    public void addBinding(Class<?> type, Object object) {
        bindings.put(type, object);
    }

    public static void setUp(Object testObject, PrismaticTestModule module) {
        Module roboGuiceModule = RoboGuice.newDefaultRoboModule(Robolectric.application);
        Module productionModule = Modules.override(roboGuiceModule).with(new PrismaticModule());
        Module testModule = Modules.override(productionModule).with(module);
        RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE, testModule);
        RoboInjector injector = RoboGuice.getInjector(Robolectric.application);
        injector.injectMembers(testObject);
    }

    public static void tearDown() {
        RoboGuice.util.reset();
        Application app = Robolectric.application;
        DefaultRoboModule defaultModule = RoboGuice.newDefaultRoboModule(app);
        RoboGuice.setBaseApplicationInjector(app, RoboGuice.DEFAULT_STAGE, defaultModule);
    }



}
