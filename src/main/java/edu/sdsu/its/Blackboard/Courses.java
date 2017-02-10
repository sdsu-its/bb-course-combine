package edu.sdsu.its.Blackboard;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import edu.sdsu.its.Blackboard.Models.Column;
import edu.sdsu.its.Blackboard.Models.Course;
import edu.sdsu.its.Blackboard.Models.Enrollment;
import edu.sdsu.its.Blackboard.Models.Group;
import edu.sdsu.its.Vault;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO JavaDoc
 *
 * @author Tom Paulus
 *         Created on 1/27/17.
 */
public class Courses {
    private static final Logger LOGGER = Logger.getLogger(Courses.class);

    /**
     * Get all Courses from a given semester.
     * This assumes that the semester stub which is passed in as an argument is contained within
     * the courses' course-id.
     *
     * @param semester {@link String} Semester Component of Course ID
     * @return {@link Course[]} List of Courses
     */
    public static Course[] getAllCourses(String semester) {
        int offset = 0;
        int limit = 75;

        List<Course> courseList = new ArrayList<>();

        try {
//            courseList.size() % limit != 0 when less then the limit of courses have been
//            retrieved from the API

            while (courseList.size() % limit == 0 || offset == 0) {
                LOGGER.debug(String.format("Retrieving %d courses, offset %d - semester filter \"%s\"",
                        limit, offset, semester));
                final HttpResponse httpResponse = Unirest.get(Vault.getParam(Vault.getParam("API Secret"), "URL") + "/learn/api/public/v1/courses")
                        .header("Authorization", "Bearer " + Auth.getToken())
                        .queryString("offset", offset)
                        .queryString("limit", limit)
                        .queryString("id", semester)
                        .asString();

                LOGGER.debug("Request for Courses returned " + httpResponse.getStatus());

                Gson gson = new Gson();
                ListResult result = gson.fromJson(httpResponse.getBody().toString(), ListResult.class);
                LOGGER.debug(String.format("Retrieved %d courses from Bb Learn API", result.results.length));

                Collections.addAll(courseList, result.results);
                offset += limit;
            }
        } catch (UnirestException e) {
            LOGGER.error("Problem retrieving course list", e);
            return null;
        }

        return courseList.toArray(new Course[]{});
    }

    public static Course getCourse(String courseID) {
        LOGGER.debug(String.format("Retrieving Course with ID \"%s\"", courseID));
        Course course = null;

        try {
            final HttpResponse httpResponse = Unirest.get(Vault.getParam(Vault.getParam("API Secret"), "URL") + "/learn/api/public/v1/courses")
                    .header("Authorization", "Bearer " + Auth.getToken())
                    .queryString("id", courseID)
                    .asString();

            LOGGER.debug("Request for Courses returned " + httpResponse.getStatus());
            Gson gson = new Gson();
            ListResult result = gson.fromJson(httpResponse.getBody().toString(), ListResult.class);
            LOGGER.debug(String.format("Retrieved %d courses from Bb Learn API", result.results.length));
            LOGGER.warn(String.format("More than one course matched the requested Course ID - %d found", result.results.length));
            course = result.results[0];

        } catch (UnirestException e) {
            LOGGER.error(String.format("Problem Retrieving Course with ID \"%s\"", courseID), e);
        }

        return course;
    }

    public static void createCourse(Course course) {
        // TODO POST /learn/api/public/v1/courses
    }

    public static Enrollment[] getEnrollments(Course course) {
        // TODO GET /learn/api/public/v1/courses/{courseId}/users
        return null;
    }

    public static Column createColumn(Course course, Column column) {
        // TODO POST /learn/api/public/v1/courses/{courseId}/gradebook/columns
        return null;
    }

    public static void deleteColumn(Course course, Column column) {
        // TODO DELETE /learn/api/public/v1/courses/{courseId}/gradebook/columns/{columnId}
    }

    static class ListResult {
        Course[] results;
    }
}

