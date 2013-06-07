package com.bridgecanada.prismatic;

import android.content.Intent;
import com.bridgecanada.net.IHttpResultCallback;
import com.bridgecanada.prismatic.data.PrismaticFeed;
import com.bridgecanada.prismatic.di.PrismaticTestModule;
import com.bridgecanada.prismatic.feed.HttpError;
import com.bridgecanada.prismatic.feed.IFeedStrategy;
import com.bridgecanada.prismatic.feed.IPrismaticFeed;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowIntent;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 21/04/13
 * Time: 7:06 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    com.bridgecanada.prismatic.feed.PrismaticFeed _prismaticFeed;
    PrismaticTestModule _module;

    @Before
    public void setUp() throws Exception {
        _prismaticFeed = mock(com.bridgecanada.prismatic.feed.PrismaticFeed.class);
        //MockitoAnnotations.initMocks(this); // mock everything marked with @Mock
        _module = new PrismaticTestModule();

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    @SuppressWarnings("unchecked")
    public void On_Create_Should_Load_Feed() throws JSONException {

        // Arrange
        _module.addBinding(com.bridgecanada.prismatic.feed.PrismaticFeed.class, _prismaticFeed);
        PrismaticTestModule.setUp(this, _module);
        MainActivity activity = new MainActivity();

        // Act
        activity.onCreate(null);

        // Assert

        verify(_prismaticFeed).GetFeed(any(IFeedStrategy.class), any(IHttpResultCallback.class), any(IHttpResultCallback.class));


    }

    @Test
    public void On_First_Load_Should_GetFeed_Without_Id() {

        // Arrange

        // Act

        // Assert
        fail("Not Implemented yet");

    }

    @Test
    public void onSecondLoadShouldUsePreviousId() {

        // Arrange

        // Act

        // Assert
        fail("Not Implemented yet");

    }

    @Test
    public void When_Need_Auth_Should_Send_Intent_To_LoginActivity() throws JSONException {

        // Arrange
        _module.addBinding(com.bridgecanada.prismatic.feed.PrismaticFeed.class, new PrismaticFailingFeed());
        PrismaticTestModule.setUp(this, _module);
        MainActivity mainActivity = new MainActivity();

        // Act
        mainActivity.onCreate(null);

        // Assert
        ShadowActivity shadowActivity = shadowOf(mainActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        assertThat(startedIntent, notNullValue());
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(LoginActivity.class.getName()));
    }


    /**
     * Test class which simulates a failing feed
     * TODO: make response in async thread?
     */
     class PrismaticFailingFeed implements IPrismaticFeed {



        @Override
        public void GetFeed(IFeedStrategy strategy, IHttpResultCallback<PrismaticFeed> successCallback, IHttpResultCallback<HttpError> failureCallback) {

            HttpError errorResult = new HttpError();
            errorResult.setErrorMessage("Test Error");
            errorResult.setStatus(401);

            failureCallback.onComplete(errorResult, "...", 401);
        }


        public void GetFeed(String id, int start, int end, IHttpResultCallback<PrismaticFeed> successCallback, IHttpResultCallback<HttpError> failureCallback) {

            HttpError errorResult = new HttpError();
            errorResult.setErrorMessage("Test Error");
            errorResult.setStatus(401);

            failureCallback.onComplete(errorResult,"...",  401);
        }
    }

}
