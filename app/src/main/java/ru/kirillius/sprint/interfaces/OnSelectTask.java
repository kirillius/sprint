package ru.kirillius.sprint.interfaces;

import ru.kirillius.sprint.models.Tasks;

public interface OnSelectTask {
    void onSelectTask(Tasks task);
    void onLongSelectTask(Tasks task);
}
