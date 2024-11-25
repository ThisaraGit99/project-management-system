////package com.projectmanagement.config;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import com.projectmanagement.model.User;
//
//import java.io.IOException;
//
//public class RoleDeserializer extends JsonDeserializer<User.Role> {
//    @Override
//    public User.Role deserialize(JsonParser parser, DeserializationContext context) throws IOException {
//        String value = parser.getText().toUpperCase();  // Convert to uppercase
//        return User.Role.valueOf(value);
//    }
//}
