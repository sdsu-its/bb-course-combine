package edu.sdsu.its.Blackboard;

import edu.sdsu.its.Blackboard.Models.Column;
import edu.sdsu.its.Blackboard.Models.Course;
import edu.sdsu.its.Blackboard.Models.User;

/**
 * TODO JavaDoc
 *
 * @author Tom Paulus
 *         Created on 1/27/17.
 */
public class Users {
    public static void enrollInCourse(Course course, User user, String role) {
        // TODO PUT /learn/api/public/v1/courses/{courseId}/users/{userId}
    }

    public static void changeAvalibility(Course course, User user, Boolean available) {
        // TODO PATCH /learn/api/public/v1/courses/{courseId}/users/{userId}
    }

    public static void updateColumnGrade(Course course, Column column, User user){
        // TODO PATCH /learn/api/public/v1/courses/{courseId}/gradebook/columns/{columnId}/users/{userId}
    }
}
