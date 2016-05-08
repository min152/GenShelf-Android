package apps.gen.genshelf.data.task;

import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;

import apps.gen.genshelf.data.DataController;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by Gen on 2016/5/8.
 */
public class Queue implements Task.Callback {

    private ArrayList<Task> tasks = new ArrayList<>();
    private HashMap<String, Task> tasksIndexes = new HashMap<>();
    private DataController dataController;
    private Handler handler = new Handler();
    private ArrayList<Task> cacheTasks = new ArrayList<>();

    @Override
    public void onTaskStart(Task task) {

    }

    @Override
    public void onTaskComplete(Task task) {
        if (tasks.contains(task)) {
            tasks.remove(task);
            running = false;
            checkQueue();
        }else if (cacheTasks.contains(task)) {
            cacheTasks.remove(task);
        }
    }

    @Override
    public void onTaskMoveCache(Task task) {
        if (tasks.contains(task)) {
            tasks.remove(task);
            cacheTasks.add(task);
            running = false;
            checkQueue();
        }
    }

    @Override
    public void onTaskFailed(Task task, String reason) {
        if (tasks.contains(task)) {
            tasks.remove(task);
            running = false;
            checkQueue();
        }else if (cacheTasks.contains(task)) {
            cacheTasks.remove(task);
        }
    }

    @Override
    public void onTaskCancel(Task task) {
        if (tasks.contains(task)) {
            tasks.remove(task);
            running = false;
            checkQueue();
        }else if (cacheTasks.contains(task)) {
            cacheTasks.remove(task);
        }
    }

    @Override
    public void onTaskProgress(Task task, float progress) {

    }

    public interface TaskCreator<T extends Task> {
        T create();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public DataController getDataController() {
        return dataController;
    }

    public Task task(String identifier) {
        return tasksIndexes.get(identifier);
    }

    public <T extends Task> T createTask(String identifier, TaskCreator<T> creator) {
        T task = (T)task(identifier);
        if (task == null && creator != null) {task = creator.create();
            task.identifier = identifier;
            task.dataController = dataController;
            task.refCount = 0;
            task.parent = this;
            task.handler = handler;
            tasks.add(task);
            tasksIndexes.put(identifier, task);
            checkQueue();
        }
        return task;
    }
    public void retain(Task task) {
        if (tasks.contains(task)) {
            task.retain();
        }
    }
    public void retain(String identifier) {
        if (tasksIndexes.containsKey(identifier)) {
            tasksIndexes.get(identifier).retain();
        }
    }
    public void release(Task task) {
        if (tasks.contains(task)) {
            task.release();
        }
    }
    public void release(String identifier) {
        if (tasksIndexes.containsKey(identifier)) {
            tasksIndexes.get(identifier).release();
        }
    }
    public boolean has(Task task) {
        return tasks.contains(task);
    }
    public boolean has(String identifier) {
        return tasksIndexes.containsKey(identifier);
    }
    private boolean willCheck = false;
    private boolean running = false;
    private void checkQueue() {
        willCheck = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (willCheck) {
                    willCheck = false;
                    if (tasks.size() == 0) {
                        running = false;
                        return;
                    }
                    if (running) {
                        return;
                    }
                    running = true;
                    tasks.get(0).start();
                }
            }
        }, 0);
    }
}
