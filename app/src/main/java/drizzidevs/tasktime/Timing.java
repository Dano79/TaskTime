package drizzidevs.tasktime;

import android.util.Log;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by danielodorizzi on 3/20/18. Simple timing object
 * Sets its start time when created, and calculates how long since creation,
 * when setDuration is called
 */

 class Timing  implements Serializable {
     private static final long serialVersionUID = 20161120L;
     private static final String TAG = Timing.class.getSimpleName();

     private long m_Id;
     private Task mTask;
     private long mStartTime;
     private long mDuration;

    public Timing(Task task) {
        mTask = task;
        //init the start time to now and the duration to zero for a new object.
        Date currentTime = new Date();
        mStartTime = currentTime.getTime() / 1000; // we are only tracking whole seconds, not milliseconds
        mDuration = 0;
    }

    long getId() {
        return m_Id;
    }

    void setId(long id) {
        m_Id = id;
    }

    Task getTask() {
        return mTask;
    }

    void setTask(Task task) {
        mTask = task;
    }

    long getStartTime() {
        return mStartTime;
    }

    void setStartTime(long startTime) {
        mStartTime = startTime;
    }

    long getDuration() {
        return mDuration;
    }

    void setDuration() {
        // calculate the duration from m_Starttime to dateTime.
        Date currentTime = new Date();
        mDuration = (currentTime.getTime() / 1000) - mStartTime; // working in seconds, not milliseconds
        Log.d(TAG, mTask.getId() + " - Start time: " + mStartTime + " | Duration: " + mDuration);
    }
}
