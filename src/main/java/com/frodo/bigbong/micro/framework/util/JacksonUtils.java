package com.frodo.bigbong.micro.framework.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: frodoking
 * @date: 2019/11/08
 * @description:
 */
public final class JacksonUtils {
    private static final JsonNodeFactory FACTORY = JsonNodeFactory.instance;

    private static final ObjectReader READER;
    private static final ObjectWriter WRITER;

    static {
        final ObjectMapper mapper = newMapper();
        READER = mapper.reader();
        WRITER = mapper.writer();
    }

    private JacksonUtils() {
    }

    /**
     * Return a preconfigured {@link ObjectReader} to read JSON inputs
     *
     * @return the reader
     * @see #newMapper()
     */
    public static ObjectReader getReader() {
        return READER;
    }

    /**
     * Return a preconfigured {@link JsonNodeFactory} to generate JSON data as
     * {@link JsonNode}s
     *
     * @return the factory
     */
    public static JsonNodeFactory nodeFactory() {
        return FACTORY;
    }

    /**
     * Return a map out of an object's members
     *
     * <p>If the node given as an argument is not a map, an empty map is
     * returned.</p>
     *
     * @param node the node
     * @return a map
     */
    public static Map<String, JsonNode> asMap(final JsonNode node) {
        if (!node.isObject())
            return Collections.emptyMap();

        final Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
        final Map<String, JsonNode> ret = new HashMap<>();

        Map.Entry<String, JsonNode> entry;

        while (iterator.hasNext()) {
            entry = iterator.next();
            ret.put(entry.getKey(), entry.getValue());
        }

        return ret;
    }

    /**
     * Pretty print a JSON value
     *
     * @param node the JSON value to print
     * @return the pretty printed value as a string
     * @see #newMapper()
     */
    public static String prettyPrint(final JsonNode node) {
        final StringWriter writer = new StringWriter();

        try {
            WRITER.writeValue(writer, node);
            writer.flush();
        } catch (JsonGenerationException | JsonMappingException e) {
            throw new RuntimeException("How did I get there??", e);
        } catch (IOException ignored) {
            // cannot happen
        }

        return writer.toString();
    }

    /**
     * Return a preconfigured {@link ObjectMapper}
     *
     * <p>The returned mapper will have the following features enabled:</p>
     *
     * <ul>
     *     <li>{@link DeserializationFeature#USE_BIG_DECIMAL_FOR_FLOATS};</li>
     *     <li>{@link SerializationFeature#INDENT_OUTPUT}.</li>
     * </ul>
     *
     * <p>This returns a new instance each time.</p>
     *
     * @return an {@link ObjectMapper}
     */
    public static ObjectMapper newMapper() {
        return new ObjectMapper().setNodeFactory(FACTORY)
                .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
                .enable(com.fasterxml.jackson.core.JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
