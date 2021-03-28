package persistence;

import org.json.JSONObject;

/**
 * Converts objects to JSON objects.
 * Code referenced from:
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 */
public interface Writable {

    /**
     * Converts this to a JSON object.
     * @return this as a JSON object
     */
    JSONObject toJson();
}
