package edu.sdsu.its.API.Models;

/**
 * Models a Semester
 * The Name must be the same as the semester component of your course ID in Blackboard
 *
 * @author Tom Paulus
 *         Created on 2/10/17.
 */
public class Semester {
    public String name;
    public boolean active;

    public Semester(String name, boolean active) {
        this.name = name;
        this.active = active;
    }
}
