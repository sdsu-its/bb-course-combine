package edu.sdsu.its.API;

import org.mapdb.DB;
import org.mapdb.DBMaker;

/**
 * Courses API Endpoint.
 * Also contains the Memory-Backed MapDB that has a list of all courses, to make the SearchUI more responsive, and
 * reduce calls to the Bb Learn API.
 *
 * @author Tom Paulus
 *         Created on 2/3/17.
 */
public class Courses {
    private static final DB db = DBMaker
            .memoryDB()
            .closeOnJvmShutdown()
            .make();

    public static DB getDb() {
        return db;
    }
}
