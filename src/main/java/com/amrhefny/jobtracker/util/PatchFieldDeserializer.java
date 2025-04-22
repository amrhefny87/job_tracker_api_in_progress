package com.amrhefny.jobtracker.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.io.IOException;

public class PatchFieldDeserializer
        extends JsonDeserializer<PatchField<?>>
        implements ContextualDeserializer {

    /** this will hold the JavaType of T (e.g. Long, String) */
    private final JavaType valueType;

    public PatchFieldDeserializer() {
        this.valueType = null; // will be initialized in createContextual
    }

    private PatchFieldDeserializer(JavaType valueType) {
        this.valueType = valueType;
    }

    @Override
    public JsonDeserializer<?> createContextual(
            DeserializationContext ctxt,
            BeanProperty property) throws JsonMappingException {

        // property.getType() is PatchField<T>
        JavaType wrapperType = property.getType();
        // extract T
        JavaType inner = wrapperType.containedType(0);
        return new PatchFieldDeserializer(inner);
    }

    @Override
    public PatchField<?> deserialize(
            JsonParser p,
            DeserializationContext ctxt) throws IOException {

        JsonNode node = p.getCodec().readTree(p);

        // if you want explicit null → provided with null
        if (node.isNull()) {
            return PatchField.of(null);
        }

        // now convert JSON node *into* the target type T
        ObjectCodec codec = p.getCodec();
        Object value = ((ObjectMapper)codec).convertValue(node, valueType);


        return PatchField.of(value);
    }
}
