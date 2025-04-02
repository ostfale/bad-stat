package de.ostfale.qk.app.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.util.Optional;

@Singleton
public class ConfiguredObjectMapper {
    Logger log = Logger.getLogger(ConfiguredObjectMapper.class);

    private final ObjectMapper objectMapper;

    public ConfiguredObjectMapper() {
        log.debug("Create configured object mapper");
        this.objectMapper = new ObjectMapper();
        configureMapper(this.objectMapper);
    }

    private void configureMapper(ObjectMapper mapper) {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
    }


    public ObjectMapper getMapper() {
        return objectMapper;
    }

    public <T> Optional<T> mapToClass(String jsonString, Class<T> target) {
        try {
            return Optional.of(objectMapper.readValue(jsonString, target));
        } catch (JsonProcessingException e) {
            log.errorf("Could not map json string to class %s", target.getName(), e);
            return Optional.empty();
        }
    }

    public String toPrettyJsonString(Object obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Error serializing object to pretty JSON string", e);
            return "";
        }
    }

}
