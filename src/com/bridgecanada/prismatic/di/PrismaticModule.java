package com.bridgecanada.prismatic.di;

import com.bridgecanada.net.PersistentCookieStore;
import com.bridgecanada.prismatic.data.IPersistentJsonStore;
import com.bridgecanada.prismatic.data.PersistentJsonStore;
import com.bridgecanada.prismatic.feed.*;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.assistedinject.FactoryProvider;
import roboguice.inject.SharedPreferencesName;


/**
 * User: bridge
 */
public class PrismaticModule extends AbstractModule {


    @Override
    protected void configure() {
        // TODO: Inject https://code.google.com/p/google-guice/wiki/FrequentlyAskedQuestions
        //String baseUrl = "https://www..com/Industry";
        String authBaseUrl = "http://auth.getprismatic.com";
        String apiBaseUrl = "http://auth.getprismatic.com";
        String apiVersion = "1.0";
        String userAgent =  "Prismatic/Android 0.0002";

        bind(IAuthService.class).to(AuthService.class);
        bind(IPrismaticFeed.class).to(PrismaticFeed.class);
        //bind(DispatchServiceBase.class).to(EventDispatchService.class);
        bind(IPersistentJsonStore.class).to(PersistentJsonStore.class);
        bind(IFeedCache.class).to(FeedCache.class);

        bindConstant().annotatedWith(AuthBaseUrlAnnotation.class).to(authBaseUrl);
        bindConstant().annotatedWith(ApiBaseUrlAnnotation.class).to(apiBaseUrl);
        bindConstant().annotatedWith(UserAgentAnnotation.class).to(userAgent);
        bindConstant().annotatedWith(ApiVersionAnnotation.class).to(apiVersion);

        bind(PersistentCookieStore.class).toProvider(PersistentCookieStoreProvider.class);
        bindConstant()
                .annotatedWith(SharedPreferencesName.class)
                .to("com.bridgecanada.prismatic_preferences");

        // TODO: Redo this with guice 3
        // SEE: https://code.google.com/p/google-guice/wiki/AssistedInject

        bind(HomeStrategy.HomeStrategyFactory.class).toProvider(
                FactoryProvider.newFactory(HomeStrategy.HomeStrategyFactory.class,
                        HomeStrategy.class));

        bind(PersonalKeyStrategy.PersonalKeyStrategyFactory.class).toProvider(
                FactoryProvider.newFactory(PersonalKeyStrategy.PersonalKeyStrategyFactory.class,
                    PersonalKeyStrategy.class));

        bind(SharedFeedStrategy.SharedFeedStrategyFactory.class).toProvider(
                FactoryProvider.newFactory(SharedFeedStrategy.SharedFeedStrategyFactory.class,
                        SharedFeedStrategy.class));

        bind(ReadFeedStrategy.ReadFeedStrategyFactory.class).toProvider(
                FactoryProvider.newFactory(ReadFeedStrategy.ReadFeedStrategyFactory.class,
                        ReadFeedStrategy.class));

        bind(SavedFeedStrategy.SavedFeedStrategyFactory.class).toProvider(
                FactoryProvider.newFactory(SavedFeedStrategy.SavedFeedStrategyFactory.class,
                        SavedFeedStrategy.class));


        bind(RecommendedFeedStrategy.RecommendedFeedStrategyFactory.class).toProvider(
                FactoryProvider.newFactory(RecommendedFeedStrategy.RecommendedFeedStrategyFactory.class,
                        RecommendedFeedStrategy.class));

        // TODO: Test if this works
        //install(new FactoryModuleBuilder()
        //        .implement(IFeedStrategy.class, SavedFeedStrategy.class)
        //        .build(SavedFeedStrategy.SavedFeedStrategyFactory.class));

        install(new FactoryModuleBuilder()
                .implement(DispatchServiceBase.class, EventDispatchService.class)
                        //.implement(DispatchServiceBase.class, AuthEventPublicDispatch.class)
                .build(DispatchServiceBase.IDispatchServiceBaseFactory.class));

    }
}
