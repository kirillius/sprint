package ru.kirillius.sprint.interfaces;

import ru.kirillius.sprint.models.Labels;

public interface OnSaveLabel {
    void onSaveLabel(Labels label);
    void onNotSaveLabel();
}
