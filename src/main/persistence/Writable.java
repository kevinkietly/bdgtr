package persistence;

import org.json.JSONObject;

/**
 * Converts objects to JSON objects.
 * Code referenced from:
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 */
public interface Writable {

    /**
     * Converts this object to a JSON object.
     *
     * @return this object as a JSON object
     */
    JSONObject toJson();
}
