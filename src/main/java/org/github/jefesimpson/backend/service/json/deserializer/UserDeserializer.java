package org.github.jefesimpson.backend.service.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.github.jefesimpson.backend.service.model.User;
import org.github.jefesimpson.backend.service.model.UserRole;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.time.LocalDate;

public class UserDeserializer extends StdDeserializer<User> {
    public UserDeserializer() {
        super(User.class);
    }

    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        String login = root.get("login").asText();
        String password = root.get("password").asText();
        UserRole userRole = UserRole.valueOf(root.get("userRole").asText().toUpperCase());
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        return new User(0, login, hashed, LocalDate.now(), userRole);
    }
}
