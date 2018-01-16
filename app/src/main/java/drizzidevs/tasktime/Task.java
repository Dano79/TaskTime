package drizzidevs.tasktime;

import java.io.Serializable;

/**
 * Created by danielodorizzi on 1/6/18.
 */

class Task implements Serializable{
    public static final long serialVersionUID = 20161120L;

    private long m_id;
    private final String mName;
    private final String mDescription;
    private final int mSortOrder;

    public Task(long id, String name, String description, int sortOrder) {
        this.m_id = id;
        mName = name;
        mDescription = description;
        mSortOrder = sortOrder;
    }

    public long getId() {
        return m_id;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getSortOrder() {
        return mSortOrder;
    }

    public void setId(long id) {
        this.m_id = id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "m_id=" + m_id +
                ", mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mSortOrder=" + mSortOrder +
                '}';
    }
}
