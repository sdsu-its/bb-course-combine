package edu.sdsu.its.cx_tests;

import edu.sdsu.its.Blackboard.Auth;
import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Authenticate with Application Key and Secret to retrieve Token from the Learn Server
 *
 * @author Tom Paulus
 *         Created on 1/27/17.
 */
public class TestAuth {
    private static final Logger LOGGER = Logger.getLogger(TestAuth.class);

    @Test
    public void TestToken() {
        String token = Auth.getToken();
        assertNotNull("Unable to retrieve token", token);
        assertFalse("Empty Token", token.isEmpty());
        LOGGER.info("Token Obtained from Learn Server");
        LOGGER.debug("Token: " + token);
    }
}
