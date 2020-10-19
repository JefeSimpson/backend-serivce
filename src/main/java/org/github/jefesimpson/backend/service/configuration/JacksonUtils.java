package org.github.jefesimpson.backend.service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtils {
    private static ObjectMapper mapper = new ObjectMapper();

    private  JacksonUtils(){

    }
    public static ObjectMapper getMapper(){
        return mapper;
    }
}
