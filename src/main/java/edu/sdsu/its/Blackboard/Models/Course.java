package edu.sdsu.its.Blackboard.Models;

/**
 * TODO JavaDoc
 *
 * @author Tom Paulus
 *         Created on 1/27/17.
 */
public class Course {
    public String courseId;
    public String name;
    public boolean allowGuests = false;
    public String dataSourceId = "CombineService";

    public Course(String id, String name) {
        this.courseId = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        return courseId.equals(course.courseId);
    }

    @Override
    public int hashCode() {
        return courseId.hashCode();
    }
}
