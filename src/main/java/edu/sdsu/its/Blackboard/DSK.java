package edu.sdsu.its.Blackboard;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import edu.sdsu.its.Blackboard.Models.DataSource;
import edu.sdsu.its.Vault;
import org.apache.log4j.Logger;

import javax.ws.rs.core.MediaType;

import static edu.sdsu.its.Blackboard.Courses.setmDSKId;

/**
 * TODO JavaDoc
 *
 * @author Tom Paulus
 *         Created on 2/10/17.
 */
public class DSK {
    private static final Logger LOGGER = Logger.getLogger(DSK.class);

    /**
     * The 'system.datasource.manager.VIEW' entitlement is needed.
     *
     * @param externalId {@link String} External DSK Name
     * @return {@link DataSource[]} Data Sources Array
     */
    public static DataSource getDataSoruce(String externalId) {
        try {
            final HttpResponse httpResponse = Unirest.get(Vault.getParam(Vault.getParam("API Secret"), "URL") + "/learn/api/public/v1/dataSources/externalId:" + externalId)
                    .header("Authorization", "Bearer " + Auth.getToken())
                    .asString();
            LOGGER.debug("Request to get DSK returned status - " + httpResponse.getStatus());
            if (httpResponse.getStatus() / 100 != 2) return null;

            Gson gson = new Gson();
            return gson.fromJson(httpResponse.getBody().toString(), DataSource.class);

        } catch (UnirestException e) {
            LOGGER.warn("Problem retrieving DSK from API", e);
        }

        return null;
    }

    /**
     * Create a New Data Source of Courses/Users.
     * The 'system.datasource.manager.VIEW' entitlement is needed.
     *
     * @param name        {@link String} External ID for Data Source
     * @param description {@link String} Data Source Description
     * @return Creation Completion Status
     */
    public static boolean createDataSource(String name, String description) {
        LOGGER.debug(String.format("Creating DSK - Name: %s & description: %s", name, description));

        DataSource dsk = new DataSource(name, description);
        Gson gson = new Gson();

        try {
            final HttpResponse httpResponse = Unirest.post(Vault.getParam(Vault.getParam("API Secret"), "URL") + "/learn/api/public/v1/dataSources")
                    .header("Authorization", "Bearer " + Auth.getToken())
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .body(gson.toJson(dsk))
                    .asString();

            LOGGER.debug("Request to create DSK returned status - " + httpResponse.getStatus());
            if (httpResponse.getStatus() == 409) {
                LOGGER.warn("DSK already exists");
                setmDSKId(getDataSoruce(name).id);
                return true;
            }

            dsk = gson.fromJson(httpResponse.getBody().toString(), DataSource.class);
            LOGGER.info(String.format("Created DSK has internal ID: \"%s\"", dsk.id));
            setmDSKId(dsk.id);
            return true;
        } catch (UnirestException e) {
            LOGGER.error(String.format("Problem Creating DSK - \"%s\"", name), e);
        }
        return false;
    }

    /**
     * Delete a Datasource
     * The 'system.datasource.manager.VIEW' entitlement is needed.
     *
     * @param id {@link String} Primary DSK Id
     * @return Deletion Completion Status
     */
    public static boolean deleteDataSource(String id) {
        try {
            final HttpResponse httpResponse = Unirest.delete(Vault.getParam(Vault.getParam("API Secret"), "URL") + "/learn/api/public/v1/dataSources/" + id)
                    .header("Authorization", "Bearer " + Auth.getToken())
                    .asString();
            LOGGER.debug(String.format("Request to delete DSK \"%s\" returned status %d", id, httpResponse.getStatus()));
            return httpResponse.getStatus() == 204;

        } catch (UnirestException e) {
            LOGGER.error("Problem deleting DSK", e);
            return false;
        }
    }
}
