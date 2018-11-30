package com.example.android.popularmovie;

public interface AsyncTaskCompleteListener<Task> {
    void onTaskStart();

    void onTaskComplete(Task result);
}
