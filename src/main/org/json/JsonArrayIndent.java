package org.json;

import java.io.IOException;
import java.io.Writer;

// Represents an indentation tool that indents a JSON array
// Code referenced from https://stackoverflow.com/questions/58076045/how-to-make-my-json-nicer-in-java-strictly-using-org-json-json
public class JsonArrayIndent extends JSONArray {

    // MODIFIES: this
    // EFFECTS: writes indented JSON representation
    @Override
    public Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
        try {
            int length = this.length();
            writer.write('[');

            indent(writer, indentFactor, indent, false, length);
            writer.write(']');
            return writer;
        } catch (IOException exception) {
            throw new JSONException(exception);
        }
    }

    // MODIFIES: this
    // EFFECTS: indents JSON representation
    public void indent(Writer writer, int indentFactor, int indent, boolean addCommas, int length) throws IOException {
        if (length > 0) {
            final int newIndent = indent + indentFactor;
            for (Object next : this) {
                if (addCommas) {
                    writer.write(',');
                }
                if (indentFactor > 0) {
                    writer.write('\n');
                }
                JSONObject.indent(writer, newIndent);
                try {
                    JSONObject.writeValue(writer, next, indentFactor, newIndent);
                } catch (Exception exception) {
                    throw new JSONException("Unable to write JSONArray value: " + next, exception);
                }
                addCommas = true;
            }
            if (indentFactor > 0) {
                writer.write('\n');
            }
            JSONObject.indent(writer, indent);
        }
    }
}
