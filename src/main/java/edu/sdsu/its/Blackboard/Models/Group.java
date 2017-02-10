package edu.sdsu.its.Blackboard.Models;

import java.util.HashMap;
import java.util.Map;

/**
 * Models a Blackboard Group
 *
 * @author Tom Paulus
 *         Created on 2/10/17.
 */
public class Group {
    public String externalId;
    public String name;
    public String description;
    public Map<String, String> availability = new HashMap<>();

    public Group(String externalId, String name, String description) {
        this.externalId = externalId;
        this.name = name;
        this.description = description;
        availability.put("available", "No");
    }
}
