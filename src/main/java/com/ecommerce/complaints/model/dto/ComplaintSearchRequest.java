package com.ecommerce.complaints.model.dto;

import com.ecommerce.complaints.model.enums.ComplaintCategory;
import com.ecommerce.complaints.model.enums.ComplaintStatus;
import com.ecommerce.complaints.model.enums.Priority;
import com.ecommerce.complaints.model.enums.Sentiment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Set;

public record ComplaintSearchRequest(
        ComplaintStatus status,
        ComplaintCategory category,
        Priority priority,
        Integer page,
        Integer size,
        String sortBy,
        String sortDirection
) {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final String DEFAULT_SORT_BY = "createdAt";
    private static final Sort.Direction DEFAULT_DIRECTION = Sort.Direction.DESC;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("id", "createdAt", "status", "priority", "category");

    public Pageable toPageable() {
        int validatedPage = (page != null && page >= 0) ? page : DEFAULT_PAGE;
        int validatedSize = (size != null && size > 0 && size <= 200) ? size : DEFAULT_SIZE;

        String validatedSortBy = (sortBy != null && ALLOWED_SORT_FIELDS.contains(sortBy))
                ? sortBy : DEFAULT_SORT_BY;

        Sort.Direction validatedDirection = "asc".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.ASC : DEFAULT_DIRECTION;

        return PageRequest.of(validatedPage, validatedSize, Sort.by(validatedDirection, validatedSortBy));
    }
}
