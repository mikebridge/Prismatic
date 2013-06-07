package com.bridgecanada.prismatic.queue;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 01/06/13
 * Time: 9:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageHandler extends Handler {

    public MessageHandler(Looper looper) {
        super(looper);
    }

    //public MessageHandler(Callback callback) {
    //    super(callback);
    //}

    public MessageHandler(Looper looper, Callback callback) {
        super(looper, callback);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        System.out.println("Handling message in thread " + Thread.currentThread().getId());

    }
}
