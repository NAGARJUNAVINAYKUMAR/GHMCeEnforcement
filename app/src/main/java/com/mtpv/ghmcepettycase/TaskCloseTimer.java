package com.mtpv.ghmcepettycase;

import android.os.AsyncTask;

public class TaskCloseTimer implements Runnable{
    private AsyncTask task;

    public TaskCloseTimer(AsyncTask task) {
        this.task = task;
    }

     @Override
     public void run() {
        if (task.getStatus() == AsyncTask.Status.RUNNING )
            task.cancel(true);
     }
}
