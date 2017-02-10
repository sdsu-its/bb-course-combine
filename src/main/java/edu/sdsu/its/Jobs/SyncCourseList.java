package edu.sdsu.its.Jobs;

import edu.sdsu.its.Blackboard.Courses;
import edu.sdsu.its.Blackboard.Models.Course;
import org.apache.log4j.Logger;
import org.mapdb.DB;
import org.quartz.*;

import java.util.concurrent.ConcurrentMap;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Sync the MapDB in the API Endpoint for Courses with the current courses in the Bb API.
 *
 * @author Tom Paulus
 *         Created on 2/3/17.
 */
public class SyncCourseList implements Job {
    private final Logger LOGGER = Logger.getLogger(this.getClass());

    public SyncCourseList() {
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Course[] courses = Courses.getAllCourses(edu.sdsu.its.DB.getActiveSemester().name);
        LOGGER.debug(String.format("%d courses were retrieved from Bb", courses.length));

        final DB db = edu.sdsu.its.API.Courses.getDb();
        final ConcurrentMap map = db.hashMap("map").createOrOpen();

        map.clear();
        for (Course c : courses) {
            //noinspection unchecked
            map.put(c.id, c);
        }
        db.commit();
    }

    /**
     * Schedule the Sync Job
     *
     * @param scheduler
     * @param intervalInHours How often the job should run in Hours
     * @throws SchedulerException
     */
    public static void schedule(Scheduler scheduler, int intervalInHours) throws SchedulerException {
        // define the job and tie it to our MyJob class
        JobDetail job = newJob(SyncCourseList.class)
                .withIdentity("SyncCourseListJob", "group1")
                .build();

        // Trigger the job to run now, and then repeat every 40 seconds
        Trigger trigger = newTrigger()
                .withIdentity("SyncTrigger", "group1")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInHours(intervalInHours)
                        .repeatForever())
                .build();

        // Tell quartz to schedule the job using our trigger
        scheduler.scheduleJob(job, trigger);
    }
}
