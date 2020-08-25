package com.rexijie.mock;

import java.util.Objects;

public class Project {
    private String name;
    private long duration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Project) {
            Project other = (Project) obj;
            if (obj.getClass() == this.getClass()) {
                return other.getName().equals(this.getName()) &&
                        other.getDuration() == this.getDuration();
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, duration);
    }
}