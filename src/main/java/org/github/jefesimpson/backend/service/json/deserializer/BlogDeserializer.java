package org.github.jefesimpson.backend.service.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.github.jefesimpson.backend.service.model.Blog;

import java.io.IOException;
import java.time.LocalDate;

public class BlogDeserializer extends StdDeserializer<Blog> {

    public BlogDeserializer() {
        super(Blog.class);
    }

    @Override
    public Blog deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        String head = root.get("head").asText();
        String text = root.get("text").asText();
        boolean vipAccess = root.get("vipAccess").asBoolean();
        return new Blog(0, head, text, LocalDate.now(), vipAccess);
    }
}
