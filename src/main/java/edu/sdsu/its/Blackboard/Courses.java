package edu.sdsu.its.Blackboard;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import edu.sdsu.its.Blackboard.Models.Column;
import edu.sdsu.its.Blackboard.Models.Course;
import edu.sdsu.its.Blackboard.Models.Enrollment;
import edu.sdsu.its.Vault;
import org.apache.log4j.Logger;

import javax.ws.rs.core.MediaType;
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
    private static String mDSKId = "";

    public static void setmDSKId(String mDSKId) {
        Courses.mDSKId = mDSKId;
    }

    public static String getmDSKId() {
        return mDSKId;
    }

    /**
     * Get all Courses from a given semester.
     * This assumes that the semester stub which is passed in as an argument is contained within
     * the courses' course-courseId.
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
                        .queryString("courseId", semester)
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

    /**
     * Get a specific course based on the course's ID. The course is found via a contains search,
     * however if there are multiple results, only the first one will be returned.
     *
     * @param courseID {@link String} Course ID
     * @return {@link Course} Corresponding Course from Bb API
     */
    public static Course getCourse(String courseID) {
        LOGGER.debug(String.format("Retrieving Course with ID \"%s\"", courseID));
        Course course = null;

        try {
            final HttpResponse httpResponse = Unirest.get(Vault.getParam(Vault.getParam("API Secret"), "URL") + "/learn/api/public/v1/courses")
                    .header("Authorization", "Bearer " + Auth.getToken())
                    .queryString("courseId", courseID)
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

    /**
     * Create a Blackboard Course.
     * It must have a unique course ID.
     *
     * @param course {@link Course} Course to be Created
     * @return {@link boolean} If created correctly
     */
    public static boolean createCourse(Course course) {
        course.dataSourceId = mDSKId; // Set DSK to the one set in the Vault
        HttpResponse httpResponse = null;

        try {
            Gson gson = new Gson();
            httpResponse = Unirest.post(Vault.getParam(Vault.getParam("API Secret"), "URL") +
                    "/learn/api/public/v1/courses")
                    .header("Authorization", "Bearer " + Auth.getToken())
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .body(gson.toJson(course))
                    .asString();

            LOGGER.debug("Request for Courses returned " + httpResponse.getStatus());
        } catch (UnirestException e) {
            LOGGER.warn("Problem Creating Course - " + course.courseId, e);
        }

        return httpResponse != null && (httpResponse.getStatus() / 100 == 2);
    }

    /**
     * Delete a Blackboard course.
     * Only the CourseID in the course needs to be defined.
     *
     * @param course {@link Course} Course to be Deleted
     * @return {@link boolean}  If deleted correctly
     */
    public static boolean deleteCourse(Course course) {
        HttpResponse httpResponse = null;
        try {
            httpResponse = Unirest.delete(Vault.getParam(Vault.getParam("API Secret"), "URL") +
                    "/learn/api/public/v1/courses/courseId:" + course.courseId.toLowerCase())
                    .header("Authorization", "Bearer " + Auth.getToken())
                    .asString();

            LOGGER.debug("Request for Courses returned " + httpResponse.getStatus());
        } catch (UnirestException e) {
            LOGGER.warn("Problem Deleting Course - " + course.courseId, e);
        }


        return httpResponse != null && httpResponse.getStatus() == 204;

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

