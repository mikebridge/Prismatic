package com.bridgecanada.prismatic;

import android.app.Application;
import android.graphics.Bitmap;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
//import com.novoda.imageloader.core.ImageManager;
//import com.novoda.imageloader.core.LoaderSettings;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 21/04/13
 */
public class PrismaticApplication extends Application {

    //private static ImageLoaderConfiguration imageLoaderConfiguration;


    @Override
    public void onCreate() {
        super.onCreate();
        //LoaderSettings settings = new LoaderSettings.SettingsBuilder()
        //        .withDisconnectOnEveryCall(true).build(this);
        //imageManager = new ImageManager(this, settings);

        setGlobalImageLoaderConfiguration();
    }

    public void setGlobalImageLoaderConfiguration() {
        // this is a singleton, but it needs to be initialized
        // globally somehow, see: https://github.com/nostra13/Android-Universal-Image-Loader#configuration-and-display-options
        ImageLoader.getInstance().init(getImageLoaderConfiguration());
    }


    private ImageLoaderConfiguration getImageLoaderConfiguration() {

        File cacheDir = StorageUtils.getCacheDirectory(this);
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.logo)
                .resetViewBeforeLoading()
                .cacheOnDisc()
                .build();

        if (!cacheDir.exists())
            cacheDir.mkdir();


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
//                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
//                .discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG, 75)
//                .taskExecutor(...)
//        .taskExecutorForCachedImages(...)
//        .threadPoolSize(3) // default
//                .threadPriority(Thread.NORM_PRIORITY - 1) // default
//                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
//                .denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
//                .memoryCacheSize(2 * 1024 * 1024)
                .discCache(new UnlimitedDiscCache(cacheDir)) // default
//                .discCacheSize(50 * 1024 * 1024)
//                .discCacheFileCount(100)
//                .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
//                .imageDownloader(new BaseImageDownloader(context)) // default
//                .imageDecoder(new BaseImageDecoder()) // default
                  .defaultDisplayImageOptions(displayImageOptions)

//                .enableLogging()
//

                .build();
        return config;
    }

}
