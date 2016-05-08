package apps.gen.genshelf.data.task;

import android.os.Handler;

import java.util.ArrayList;

import apps.gen.genshelf.data.DataController;

/**
 * Created by Gen on 2016/5/8.
 */
public class Task {

    public interface Callback {
        void onTaskStart(Task task);
        void onTaskComplete(Task task);
        void onTaskMoveCache(Task task);
        void onTaskFailed(Task task, String reason);
        void onTaskCancel(Task task);
        void onTaskProgress(Task task, float progress);
    }

    String identifier;
    DataController dataController;
    ArrayList<Task> subtasks = new ArrayList<>();
    boolean running;
    Handler handler;
    Callback subCallback = new Callback() {
        @Override
        public void onTaskStart(Task task) {

        }

        @Override
        public void onTaskComplete(Task task) {
            if (offset < subtasks.size() && subtasks.get(offset) == task) {
                offset ++;
                checkNextFrame();
            }
        }

        @Override
        public void onTaskMoveCache(Task task) {

        }

        @Override
        public void onTaskFailed(Task task, String reason) {
            if (offset < subtasks.size() && subtasks.get(offset) == task) {
                failed(reason);
            }
        }

        @Override
        public void onTaskCancel(Task task) {
            if (offset < subtasks.size() && subtasks.get(offset) == task) {
                offset ++;
                checkNextFrame();
            }
        }

        @Override
        public void onTaskProgress(Task task, float progress) {

        }
    };
    public boolean getRunning() {
        return running;
    }
    boolean cancel;
    public boolean getCancel() {
        return cancel;
    }
    Callback callback;
    public Callback getCallback() {
        return callback;
    }
    Callback parent;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    int retryCount = 0;
    int tryCount = 0;
    public int getRetryCount() {
        return retryCount;
    }

    int offset;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    int tag;

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    long timeDelay;

    public long getTimeDelay() {
        return timeDelay;
    }

    public void setTimeDelay(long timeDelay) {
        this.timeDelay = timeDelay;
    }

    public void addSubtask(Task task) {
        task.parent = subCallback;
        task.dataController = dataController;
        subtasks.add(task);
    }
    public void clearSubtasks() {
        subtasks.clear();
    }
    public void start() {
        if (running || cancel) return;
        running = true;
        if (callback != null) {
            callback.onTaskStart(this);
        }
        if (parent != null) {
            parent.onTaskStart(this);
        }
        onRun();
    }
    public void restart() {
        onReset();
        cancel = false;
        offset = 0;
        running = true;
        if (callback != null) {
            callback.onTaskStart(this);
        }
        if (parent != null) {
            parent.onTaskStart(this);
        }
        onRun();
    }
    public void cancel() {
        cancel = true;
        if (running) {
            if (offset < subtasks.size()) {
                subtasks.get(offset).cancel();
            }
            running = false;
            if (callback != null) {
                callback.onTaskCancel(this);
            }
            if (parent != null) {
                parent.onTaskCancel(this);
            }
        }
    }

    public void cache() {
        if (parent != null) {
            parent.onTaskMoveCache(this);
        }
    }
    public void complete() {
        if (!progressSubtask()) {
            delayComplete();
        }
    }
    public void failed(String reason) {
        running = false;
        if (tryCount < retryCount) {
            tryCount++;
            restart();
        }else {
            onFinalFailed(reason);
            if (callback != null) {
                callback.onTaskFailed(this, reason);
            }
            if (parent != null) {
                parent.onTaskFailed(this, reason);
            }
            tryCount = 0;
        }
    }
    public void fatalError(String reason) {
        running = false;
        onFinalFailed(reason);
        if (callback != null) {
            callback.onTaskFailed(this, reason);
        }
        if (parent != null) {
            parent.onTaskFailed(this, reason);
        }
        tryCount = 0;
    }
    public boolean progressSubtask() {
        if (subtasks.size() != 0) {
            checkSubtasks();
            return true;
        }
        return false;
    }

    protected void onReset() { }
    protected void onRun() { }
    protected void onFinalFailed(String reason) { }
    protected void onFinalComplete() { }
    int refCount;
    void release() {
        refCount --;
        if (refCount <= 0) {
            cancel();
        }
    }
    void retain() {
        refCount++;
    }

    private void delayComplete() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                running = false;
                if (callback != null) {
                    callback.onTaskComplete(Task.this);
                }
                if (parent != null) {
                    parent.onTaskComplete(Task.this);
                }
                tryCount = 0;
            }
        }, timeDelay);
    }

    private void checkSubtasks() {
        if (offset < subtasks.size()) {
            Task task = subtasks.get(offset);
            if (task.running) {
                failed("Subtask is already in progressing.");
            }else {
                task.start();
            }
        }else
            delayComplete();
    }

    boolean willCheck;
    Runnable nextHandle = new Runnable() {
        @Override
        public void run() {
            if (willCheck) {
                willCheck = false;
                checkSubtasks();
            }
        }
    };
    private void checkNextFrame() {
        willCheck = true;
        handler.postDelayed(nextHandle, 0);
    }
}
