package fr.marketplace;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.eclipsecollections.EclipseCollectionsModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.zalando.jackson.datatype.money.MoneyModule;

import java.io.IOException;
import java.nio.file.Path;

public class Json {

    private final ObjectMapper delegate;

    public Json() {
        this.delegate = JsonMapper.builder()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .addModule(new JavaTimeModule())
                .addModule(new EclipseCollectionsModule())
                .addModule(new MoneyModule())
                .enable(SerializationFeature.INDENT_OUTPUT)
                .build();
    }

    public String encode(Object value) {
        try {
            return delegate.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T decode(String json, Class<T> typedValue) {
        try {
            return delegate.readValue(json, typedValue);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T decode(String json, TypeReference<T> ref) {
        try {
            return delegate.readValue(json, ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void encodeToFile(Path filePath, Object value) {
        try {
            delegate.writeValue(filePath.toFile(), value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T decodeFromFile(Path filePath, Class<T> typedValue) {
        try {
            return delegate.readValue(filePath.toFile(), typedValue);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T decodeFromFile(Path filePath, TypeReference<T> ref) {
        try {
            return delegate.readValue(filePath.toFile(), ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
