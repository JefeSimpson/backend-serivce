package org.github.jefesimpson.backend.service.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.github.jefesimpson.backend.service.model.User;

import java.io.IOException;

public class UserSerializer extends StdSerializer<User> {

    public UserSerializer() {
        super(User.class);
    }

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", user.getId());
        jsonGenerator.writeStringField("login", user.getLogin());
        jsonGenerator.writeStringField("userRole", user.getUserRole().name());
        jsonGenerator.writeObjectField("createDate", user.getCreateDate());
        jsonGenerator.writeEndObject();
    }
}
