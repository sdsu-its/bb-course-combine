package edu.sdsu.its.Blackboard;

import edu.sdsu.its.Blackboard.Models.DataSource;

/**
 * TODO JavaDoc
 *
 * @author Tom Paulus
 *         Created on 2/10/17.
 */
public class DSK {
    /**
     * The 'system.datasource.manager.VIEW' entitlement is needed.
     *
     * @return {@link DataSource[]} Data Sources Array
     */
    public static DataSource[] getDataSoruces() {
        // TODO GET /learn/api/public/v1/dataSources
        return null;
    }

    /**
     * Create a New Data Source of Courses/Users.
     * The 'system.datasource.manager.VIEW' entitlement is needed.
     *
     * @param name        {@link String} External ID for Data Source
     * @param description {@link String} Data Source Description
     */
    public static void createDataSource(String name, String description) {
        // TODO POST /learn/api/public/v1/dataSources
    }
}
