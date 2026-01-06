package com.ecommerce.complaints.service;

import com.ecommerce.complaints.config.aspect.annotation.LogClass;
import com.ecommerce.complaints.exception.BusinessException;
import com.ecommerce.complaints.messaging.api.RabbitMQEventPublisher;
import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.enums.ComplaintCategory;
import com.ecommerce.complaints.model.enums.ComplaintStatus;
import com.ecommerce.complaints.model.enums.Priority;
import com.ecommerce.complaints.model.enums.Sentiment;
import com.ecommerce.complaints.model.vto.*;
import com.ecommerce.complaints.repoistory.api.ComplaintRepository;
import com.ecommerce.complaints.service.api.ComplaintService;
import com.ecommerce.complaints.service.mapper.ComplaintMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.ecommerce.complaints.messaging.event.ComplaintEvent.*;
import static com.ecommerce.complaints.model.enums.ComplaintErrors.*;


@Service
@RequiredArgsConstructor
@LogClass
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ComplaintMapper complaintMapper;
    private final RabbitMQEventPublisher eventPublisher;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("id", "createdAt", "updatedAt", "status", "priority", "category", "sentiment", "subject", "customerEmail");


    @Override
    @Transactional
    public ComplaintVTO createComplaint(ComplaintCreateDTO complaintCreateDTO) {
        validateComplaintCreation(complaintCreateDTO);
        Complaint complaint = complaintMapper.toEntity(complaintCreateDTO);
        Complaint savedComplaint = complaintRepository.save(complaint);

        eventPublisher.publishEvent(COMPLAINT_CREATED, complaintMapper.toCreatedEvent(savedComplaint));
        return complaintMapper.toVTO(savedComplaint);
    }

    @Override
    public ComplaintVTO getComplaintById(Long id) {
        return complaintRepository.findById(id).map(complaintMapper::toVTO)
                .orElseThrow(() -> new BusinessException(COMPLAINT_NOT_FOUND, id));
    }


    @Override
    @Transactional
    public ComplaintVTO updateComplaint(Long id, ComplaintUpdateDTO dto) {
        Complaint existing = complaintRepository.findById(id)
                .orElseThrow(() -> new BusinessException(COMPLAINT_NOT_FOUND, id));

        if (dto.getSubject() != null && !dto.getSubject().trim().isEmpty()) {
            existing.setSubject(dto.getSubject().trim());
        }
        if (dto.getDescription() != null && !dto.getDescription().trim().isEmpty()) {
            existing.setDescription(dto.getDescription().trim());
        }
        Complaint updated = complaintRepository.update(existing);

        eventPublisher.publishEvent(COMPLAINT_UPDATED, complaintMapper.toUpdatedEvent(updated));
        return complaintMapper.toVTO(updated);
    }
    @Override
    @Transactional
    public void deleteComplaint(Long id) {
        complaintRepository.findById(id).orElseThrow(() -> new BusinessException(COMPLAINT_NOT_FOUND, id));
        complaintRepository.delete(id);
        eventPublisher.publishEvent(COMPLAINT_DELETED, id);
    }



    @Override
    public ComplaintListVTO listComplaints(ComplaintStatus status,
                                           ComplaintCategory category,
                                           Priority priority,
                                           Sentiment sentiment,
                                           Integer page,
                                           Integer size,
                                           String sortBy,
                                           String sortDirection) {

        String safeSortBy = (sortBy != null && ALLOWED_SORT_FIELDS.contains(sortBy))
                ? sortBy : "createdAt";

        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, safeSortBy);

        int pageNum = (page != null && page >= 0) ? page : 0;
        int pageSize = (size != null && size > 0 && size <= 200) ? size : 20;

        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<Complaint> result = complaintRepository.findAll(status, category, priority, sentiment, pageable);

        return complaintMapper.toListVTO(result);
    }



    public void validateComplaintCreation(ComplaintCreateDTO dto) {
        if (dto == null) {
            throw new BusinessException(INVALID_INPUT);
        }
        if (dto.getCustomerEmail() == null || dto.getCustomerEmail().trim().isEmpty()) {
            throw new BusinessException(MISSING_REQUIRED_FIELD);
        }
        if (dto.getSubject() == null || dto.getSubject().trim().isEmpty()) {
            throw new BusinessException(MISSING_REQUIRED_FIELD);
        }
        if (dto.getDescription() == null || dto.getDescription().trim().isEmpty()) {
            throw new BusinessException(MISSING_REQUIRED_FIELD);
        }
        if (dto.getDescription().length() > 5000) {
            throw new BusinessException(VALIDATION_ERROR);
        }
        if (!dto.getCustomerEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BusinessException(INVALID_EMAIL);
        }
    }
}
