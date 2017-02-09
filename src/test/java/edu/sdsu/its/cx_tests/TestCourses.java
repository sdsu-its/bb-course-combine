package edu.sdsu.its.cx_tests;

import edu.sdsu.its.Blackboard.Courses;
import edu.sdsu.its.Blackboard.Models.Course;
import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * TODO JavaDoc
 *
 * @author Tom Paulus
 *         Created on 2/8/17.
 */
public class TestCourses {
    private static final Logger LOGGER = Logger.getLogger(TestCourses.class);

    @Test
    public void getAllCourses() {
        Course[] courses = Courses.getAllCourses("");
        assertNotNull(courses);
        assertTrue("No courses found", courses.length > 0);
        LOGGER.info(String.format("Retrieved %d courses", courses.length));

        for (Course c : courses) {
            assertNotNull(c.id);
            assertNotNull(c.name);
        }
    }
}
