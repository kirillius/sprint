package ru.kirillius.sprint.interfaces;

import ru.kirillius.sprint.models.Tasks;

public interface OnSaveTask {
    void onSaveTask(Tasks task);
    void onNotSaveTask();
}
