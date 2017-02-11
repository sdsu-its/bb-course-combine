package edu.sdsu.its.cx_tests;

import edu.sdsu.its.Blackboard.Courses;
import edu.sdsu.its.Blackboard.Models.Course;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test Blackboard API functionality in regard to Courses
 *
 * @author Tom Paulus
 *         Created on 2/8/17.
 */
public class TestCourses {
    private static final Logger LOGGER = Logger.getLogger(TestCourses.class);
    private static final String TEST_COURSE_ID = "ITS-CX-Test-" + UUID.randomUUID();
    private static final String TEST_COURSE_NAME = "Course Combinations Test Course 1";
    private static Course testCourse = null;

    @Before
    public void setUp() throws Exception {
        LOGGER.info("Creating Test Course - ID: " + TEST_COURSE_ID);
        testCourse = new Course(TEST_COURSE_ID, TEST_COURSE_NAME);
        assertTrue("Problem Creating Course", Courses.createCourse(testCourse));
    }

    @Test
    public void getCourse() throws Exception {
        Course course = Courses.getCourse(TEST_COURSE_ID);
        assertNotNull(course);
        assertEquals(course, testCourse);
    }

    @Test
    public void getAllCourses() throws Exception {
        Course[] courses = Courses.getAllCourses("");
        assertNotNull(courses);
        assertTrue("No courses found", courses.length > 0);
        LOGGER.info(String.format("Retrieved %d courses", courses.length));

        for (Course c : courses) {
            assertNotNull(c.courseId);
            assertNotNull(c.name);
        }
    }

    @After
    public void tearDown() throws Exception {
        LOGGER.warn("Deleting Test Course - ID:" + TEST_COURSE_ID);
        assertTrue(Courses.deleteCourse(testCourse));
    }
}
