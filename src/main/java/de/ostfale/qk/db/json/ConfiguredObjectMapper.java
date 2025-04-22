package de.ostfale.qk.db.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Map;
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

    public <T> Optional<List<T>> mapToList(String jsonString, Class<T> elementType) {
        try {
            var collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, elementType);
            List<T> list = objectMapper.readValue(jsonString, collectionType);
            return Optional.of(list);
        } catch (JsonProcessingException e) {
            log.errorf("Could not map json string to list of %s", elementType.getName(), e);
            return Optional.empty();
        }
    }

    public <T> Optional<Map<String, T>> mapToStringMap(String jsonString, Class<T> valueType) {
        try {
            var mapType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, valueType);
            Map<String, T> map = objectMapper.readValue(jsonString, mapType);
            return Optional.of(map);
        } catch (JsonProcessingException e) {
            log.errorf("Could not map json string to map with value type %s", valueType.getName(), e);
            return Optional.empty();
        }
    }


    public Object getObjectMapper() {
        return objectMapper;
    }
}
