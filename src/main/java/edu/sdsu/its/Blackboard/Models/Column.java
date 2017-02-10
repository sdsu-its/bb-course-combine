package edu.sdsu.its.Blackboard.Models;

import java.util.HashMap;
import java.util.Map;

/**
 * Models a Blackboard GradeCenter Column
 *
 * @author Tom Paulus
 *         Created on 2/10/17.
 */
public class Column {
    public String externalId;
    public String name;
    public String description;
    public Map<String, String> grading = new HashMap<>();
    public Map<String, String> availability = new HashMap<>();

    public Column(String externalId, String name, String description) {
        this.externalId = externalId;
        this.name = name;
        this.description = description;
        grading.put("type", "Manual");
        availability.put("available", "No");
    }
}
