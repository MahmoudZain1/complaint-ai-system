package com.ecommerce.complaints.service.mapper;

import com.ecommerce.complaints.model.entity.ComplaintResponse;
import com.ecommerce.complaints.model.generate.ResponseReviewVTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ResponseMapper {

     default ResponseReviewVTO toReviewVTO(ComplaintResponse response) {
        if (response == null) {
            return null;
        }
        return ResponseReviewVTO.builder()
                .id(response.getId())
                .complaintId(response.getComplaint().getId())
                .complaintSubject(response.getComplaint().getSubject())
                .customerName(response.getComplaint().getCustomerName())
                .generatedResponse(response.getGeneratedResponse())
                .tone(response.getTone())
                .confidenceScore(response.getConfidenceScore())
                .generatedAt(response.getGeneratedAt())
                .status(response.getStatus())
                .priority(response.getComplaint().getPriority() != null ?
                        response.getComplaint().getPriority().name() : null)
                .build();
    }
}
