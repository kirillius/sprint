package ru.kirillius.sprint.models;

public class Labels {
    public int id;
    public String name;

    public Labels() {}

    public Labels(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Labels other = (Labels) obj;
        if (id != other.id)
            return false;

        return true;
    }
}
