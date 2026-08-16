package com.loan_org.customer_management.config.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(JacksonProperties.class)
public class JacksonConfig {

    private final JacksonProperties properties;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        setSerialization(objectMapper);
        setDeserialization(objectMapper);
        setInclusion(objectMapper);
        setNaming(objectMapper);
        return objectMapper;
    }

    private void setSerialization(ObjectMapper mapper) {
        var config = properties.getSerialization();
        mapper.configure(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                config.isWriteDatesAsTimestamps());
        mapper.configure(
                SerializationFeature.WRITE_DATES_WITH_ZONE_ID,
                config.isWriteDatesWithZoneId());
        mapper.configure(
                SerializationFeature.INDENT_OUTPUT,
                config.isPrettyPrint());
        mapper.configure(
                SerializationFeature.FAIL_ON_EMPTY_BEANS,
                config.isFailOnEmptyBeans());
    }

    private void setDeserialization(ObjectMapper mapper) {
        var config = properties.getDeserialization();
        mapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                config.isFailOnUnknownProperties());
        mapper.configure(
                DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
                config.isAcceptEmptyStringAsNull());
        mapper.configure(
                DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
                config.isAcceptSingleValueAsArray());
        mapper.configure(
                DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL,
                config.isReadUnknownEnumValuesAsNull());
    }

    private void setInclusion(ObjectMapper mapper) {
        var config = properties.getInclusion();
        JsonInclude.Include valueInclusion = JsonInclude.Include.valueOf(
                config.getValue().toUpperCase());
        JsonInclude.Include contentInclusion = JsonInclude.Include.valueOf(
                config.getContent().toUpperCase());
        mapper.setDefaultPropertyInclusion(
                JsonInclude.Value.construct(
                        valueInclusion,
                        contentInclusion));
        mapper.setDefaultSetterInfo(
                JsonSetter.Value.forValueNulls(
                        Nulls.valueOf(
                                config.getNullValueHandling().toUpperCase())));
    }

    private void setNaming(ObjectMapper mapper) {
        String strategy = properties.getNaming().getStrategy();
        switch (strategy.toLowerCase()) {
            case "snake_case" ->
                mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            case "lower_camel_case" ->
                mapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
            case "lower_case" ->
                mapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CASE);
            case "kebab_case" ->
                mapper.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
            default ->
                throw new IllegalArgumentException(
                        "Unsupported Jackson naming strategy: "
                                + strategy);
        }
    }

}