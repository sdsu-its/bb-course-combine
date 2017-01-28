package edu.sdsu.its.Blackboard;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import edu.sdsu.its.Blackboard.Models.AuthPayload;
import edu.sdsu.its.Vault;
import org.apache.log4j.Logger;

/**
 * Authenticate with the Blackboard.
 *
 * @author Tom Paulus
 *         Created on 1/27/17.
 */
public class Auth {
    private static final Logger LOGGER = Logger.getLogger(Auth.class);
    private static String token = null;

    private static void BbAuthenticate() throws UnirestException {
        HttpResponse<String> httpResponse = Unirest.post(
                Vault.getParam(Vault.getParam("API Secret"), "URL") + "/learn/api/public/v1/oauth2/token")
                .queryString("grant_type", "client_credentials")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .basicAuth(Vault.getParam("application-key"),
                        Vault.getParam("secret"))
                .asString();

        if (httpResponse.getStatus() == 401) {
            LOGGER.fatal("Problem Authenticating with Learn Server", new Exception(httpResponse.getBody()));
            return;
        }

        Gson gson = new Gson();
        AuthPayload payload = gson.fromJson(httpResponse.getBody(), AuthPayload.class);
        LOGGER.debug("Recieved token from LEARN Server - " + payload.access_token);
        token = payload.access_token;
    }

    public static String getToken() {
        try {
            if (token == null) BbAuthenticate();
        } catch (UnirestException e) {
            LOGGER.error("Problem Authenticating with Learn Server", e);
        }
        return token;
    }

    public static String resetToken() {
        token = null;
        return getToken();
    }
}
