package com.ecommerce.complaints.service.mapper;

import com.ecommerce.complaints.messaging.event.ComplaintAiAnalysiEvent;
import com.ecommerce.complaints.messaging.event.ComplaintEventData;
import com.ecommerce.complaints.model.entity.Complaint;

import com.ecommerce.complaints.model.entity.ComplaintResponse;
import com.ecommerce.complaints.model.generate.*;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(
        componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ComplaintMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", constant = "OTHER")
    @Mapping(target = "status", constant = "NEW")
    @Mapping(target = "priority", constant = "MEDIUM")
    @Mapping(target = "sentiment", constant = "NEUTRAL")
    public abstract  Complaint toEntity(ComplaintCreateDTO dto);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "complaint", ignore = true)
    public abstract ComplaintResponse toEntity(ComplaintResponseVTO vto);

    @Mapping(target = "complaintId", source = "complaint.id")
    public abstract ComplaintResponseVTO toVTO(ComplaintResponse entity);

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = "customerEmail", source = "customer.email")
    public abstract ComplaintVTO toVTO(Complaint entity);

    @Mapping(target = "complaintId", source = "id")
    public abstract ComplaintEventData toCreatedEvent(Complaint entity);

    public abstract ComplaintEventData toUpdatedEvent(Complaint entity);

    public abstract ComplaintListVTO toListVTO(Page<Complaint> page);

    public ResponseGenerationRequestDTO toResponseGenerationRequestDTO(ComplaintAiAnalysiEvent event) {
        ResponseGenerationRequestDTO dto =  ResponseGenerationRequestDTO.builder()
        .tone(ResponseGenerationRequestDTO.ToneEnum.PROFESSIONAL)
        .includeCompensation(false)
        .customInstructions(null).build();
        return dto;
    }}
