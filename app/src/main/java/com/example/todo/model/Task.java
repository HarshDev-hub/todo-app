package com.example.todo.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "task_table")
public class Task implements Parcelable {
    @ColumnInfo(name = "task_id")
    @PrimaryKey(autoGenerate = true)
    public long taskId;
    public String task;
    public Priority priority;
    @ColumnInfo(name = "Due_date")
    public Date dueDate;
    @ColumnInfo(name = "created_at")
    public Date dataCreated;

    @ColumnInfo(name = "is_done")
    public  boolean isDone;

    public Task(String task, Priority priority, Date dueDate, Date dataCreated, boolean isDone) {
        this.task = task;
        this.priority = priority;
        this.dueDate = dueDate;
        this.dataCreated = dataCreated;
        this.isDone = isDone;
    }

    //Parcable implements
    protected Task(Parcel in){
        taskId = in.readLong();
        task = in.readString();
        priority = Priority.valueOf(in.readString());
        dueDate = new Date(in.readLong());
        dataCreated = new Date(in.readLong());
        isDone = in.readByte() != 0;
    }

    // prt of alarm
    public int getTaskId() {
        return (int) taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public long getDueDate() {
        return dueDate.getTime();
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getDataCreated() {
        return dataCreated;
    }

    public void setDataCreated(Date dataCreated) {
        this.dataCreated = dataCreated;
    }



    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        this.isDone = done;

    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", task='" + task + '\'' +
                ", priority=" + priority +
                ", dueDate=" + dueDate +
                ", dataCreated=" + dataCreated +
                ", isDone=" + isDone +
                '}';
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
         dest.writeLong(taskId);
         dest.writeString(task);
         dest.writeString(priority.name());
         dest.writeLong(dueDate.getTime());
         dest.writeLong(dataCreated.getTime());
         dest.writeByte((byte) (isDone ? 1 : 0));
    }
}
