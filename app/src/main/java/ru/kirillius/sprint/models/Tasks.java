package ru.kirillius.sprint.models;

public class Tasks {
    public int id, labelId;
    public String name, description, deadline;
    public Labels label;

    public Tasks() {}

    public Tasks(int id, int labelId, String name, String description, String deadline) {
        this.id = id;
        this.labelId = labelId;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Tasks other = (Tasks) obj;
        if (id != other.id)
            return false;

        return true;
    }
}
