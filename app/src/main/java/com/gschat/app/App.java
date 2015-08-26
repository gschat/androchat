package com.gschat.app;

import android.app.Application;
import android.content.Context;

import com.dd.processbutton.iml.ActionProcessButton;
import com.gschat.app.robobinding.ActionProcessButtonBinding;
import com.gschat.app.robobinding.FancyButtonBinding;
import com.gschat.cached.qiniu.GSQiNiuNetCached;
import com.gschat.sdk.GSChat;

import org.robobinding.ViewBinder;
import org.robobinding.binder.BinderFactory;
import org.robobinding.binder.BinderFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * GSChat App class
 */
public class App extends Application {

    /**
     * App logger
     */
    private final static Logger logger = LoggerFactory.getLogger("App");

    /**
     * gschat instance
     */
    private GSChat gsChat;

    /**
     * rebobinding binder factory
     */
    private final BinderFactory reusableBinderFactory;

    /**
     * create new App instance
     */
    public App() {

        reusableBinderFactory = new BinderFactoryBuilder()
                .mapView(ActionProcessButton.class,new ActionProcessButtonBinding())
                .mapView(FancyButton.class,new FancyButtonBinding())
                .build();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        gsChat = new GSChat(
                this,
                "com.gschat.app.Sample",
                new GSQiNiuNetCached(
                        "7xkg80.com2.z0.glb.qiniucdn.com",
                        "E1IAti19ARymn7YoJQNHwthFaAQnd5vjhP8AZv4r",
                        "RhNus6mN4nGtY1IamIY19Y7bERwttdY4xNKxowbq",
                        "devchatimagespace"
                )
        );
    }

    @Override
    public void onTerminate() {

        super.onTerminate();

        logger.debug("close gschat");

        gsChat.close();

        logger.debug("close gschat -- success");
    }

    public GSChat getGsChat() {
        return gsChat;
    }

    /**
     * create new robobinding viewbinder
     * @param context
     * @return
     */
    public ViewBinder createViewBinder(Context context) {
        return reusableBinderFactory.createViewBinder(context);
    }

}
