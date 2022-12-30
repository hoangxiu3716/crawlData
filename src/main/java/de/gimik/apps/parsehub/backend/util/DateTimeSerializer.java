package de.gimik.apps.parsehub.backend.util;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dang on 30.08.2014.
 */
public class DateTimeSerializer extends JsonSerializer<Date> {
    private static SimpleDateFormat formatter =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {
        if (value == null)
            gen.writeString("");
        gen.writeString(formatter.format(value));
    }
}
