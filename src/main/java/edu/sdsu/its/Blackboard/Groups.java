package edu.sdsu.its.Blackboard;

import edu.sdsu.its.Blackboard.Models.Course;
import edu.sdsu.its.Blackboard.Models.Group;
import edu.sdsu.its.Blackboard.Models.User;

/**
 * TODO JavaDoc
 *
 * @author Tom Paulus
 *         Created on 2/10/17.
 */
public class Groups {
    public static Group[] getGroups(Course course) {
        // TODO GET /learn/api/public/v1/courses/{courseId}/groups
        return null;
    }

    public static Group createGroup(Course course, Group group) {
        // TODO POST /learn/api/public/v1/courses/{courseId}/groups
        return null;
    }

    public static void deleteGroup(Course course, Group group) {
        // TODO DELETE /learn/api/public/v1/courses/{courseId}/groups/{groupId}
    }

    public static void setGroupMembership(Course course, Group group, User user) {
        // TODO PUT /learn/api/public/v1/courses/{courseId}/groups/{groupId}/users/{userId}
    }

    public static void deleteGroupMembership(Course course, Group group, User user) {
        // TODO DELETE /learn/api/public/v1/courses/{courseId}/groups/{groupId}/users/{userId}
    }
}
