package com.bridgecanada.prismatic.queue;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.util.Transcript;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * User: bridge
 * Date: 01/06/13
 * Learning test
 * see: https://github.com/robolectric/robolectric/blob/master/src/test/java/org/robolectric/shadows/HandlerTest.java
 */
@RunWith(RobolectricTestRunner.class)
public class MessageHandlerTest {

    private Transcript _transcript;
    private HandlerThread _handlerThread;
    private boolean _hasHandlerCallbackHandledMessage;

    @Before
    public void setup() {
        _hasHandlerCallbackHandledMessage = false;
        _handlerThread = new HandlerThread("testHandlerThread");
        _handlerThread.start();
        _transcript = new Transcript();


    }

    @After
    public void teardown() {

        _handlerThread.quit();

    }


    @Test
    public void itShouldRunARunnable() {

        // Arrange
        Handler handler = new MessageHandler(_handlerThread.getLooper());
        String msg = "hello";

        // Act
        handler.post(new Say(msg));

        // run the runnables
        // http://robolectric.org/javadoc/org/robolectric/shadows/ShadowLooper.html#idle()
        shadowOf(_handlerThread.getLooper()).idle();

        // Assert
        _transcript.assertEventsSoFar(msg);
    }

    //MB: I don't think this works in robolectric
//    @Test
//    public void itShouldHandleAMessage() {
//
//        // Arrange
//        //MessageHandler handler = new MessageHandler(_handlerThread.getLooper(), callback);
//
//        //Handler handler = new Handler(callback);  // this works
//        MessageHandler handler = new MessageHandler(callback); // this doesn't
//        Message msg = Message.obtain(handler);
//        msg.setData(new Bundle());
//
//        // Act
//
//        handler.sendMessage(msg);
//
//        // run the runnables
//        // http://robolectric.org/javadoc/org/robolectric/shadows/ShadowLooper.html#idle()
//        //shadowOf(_handlerThread.getLooper()).idle();
//
//        // Assert
//
//        assertThat(_hasHandlerCallbackHandledMessage, is(true));
//
//    }

    private class Say implements Runnable {
        private String event;

        public Say(String event) {
            this.event = event;
        }

        @Override
        public void run() {
            _transcript.add(event);
        }
    }

    private Handler.Callback callback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            System.out.println("CALLBACK handling message");
            _hasHandlerCallbackHandledMessage = true;
            return false;
        }
    };

}
