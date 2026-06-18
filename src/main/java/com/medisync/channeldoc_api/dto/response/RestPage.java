package com.medisync.channeldoc_api.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * Jackson-friendly wrapper for Spring Data's {@link PageImpl}.
 *
 * <p>{@code PageImpl} lacks a default constructor, causing Jackson deserialization
 * to fail when reading cached pages from Redis. This class provides a
 * {@link JsonCreator} constructor that maps JSON properties to the
 * {@code PageImpl} super constructor.</p>
 *
 * @param <T> the type of page content elements
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable"})
public class RestPage<T> extends PageImpl<T> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestPage(
            @JsonProperty("content") List<T> content,
            @JsonProperty("number") int page,
            @JsonProperty("size") int size,
            @JsonProperty("totalElements") long totalElements) {
        super(content, PageRequest.of(page, size), totalElements);
    }

    /**
     * Convenience constructor to wrap a Spring Data {@link Page}.
     * Used in the service layer to convert query results before caching.
     */
    public RestPage(Page<T> page) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
    }
}
