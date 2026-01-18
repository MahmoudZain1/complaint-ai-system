package com.ecommerce.complaints.service;

import com.ecommerce.complaints.config.aspect.annotation.LogClass;
import com.ecommerce.complaints.exception.BusinessException;
import com.ecommerce.complaints.messaging.api.RabbitMQEventPublisher;
import com.ecommerce.complaints.model.dto.ComplaintSearchRequest;
import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.entity.User;
import com.ecommerce.complaints.model.generate.*;
import com.ecommerce.complaints.repository.api.ComplaintRepository;
import com.ecommerce.complaints.repository.api.UserRepository;
import com.ecommerce.complaints.service.api.ComplaintService;
import com.ecommerce.complaints.service.mapper.ComplaintMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ecommerce.complaints.messaging.event.ComplaintEvent.*;
import static com.ecommerce.complaints.model.error.ComplaintErrors.*;
import static com.ecommerce.complaints.model.error.UserErrors.INVALID_CREDENTIALS;


@Service
@RequiredArgsConstructor
@LogClass
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ComplaintMapper complaintMapper;
    private final RabbitMQEventPublisher eventPublisher;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public ComplaintVTO createComplaint(ComplaintCreateDTO complaintCreateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException(INVALID_CREDENTIALS);
        }
        User user =  userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new BusinessException(INVALID_CREDENTIALS));
        validateComplaintCreation(complaintCreateDTO);
        Complaint complaint = complaintMapper.toEntity(complaintCreateDTO);
        complaint.setCustomer(user);
        complaint.setCustomerName(user.getName());
        complaint.setCustomerEmail(user.getEmail());
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
    public ComplaintListVTO listComplaints(ComplaintSearchRequest request) {
        Pageable pageable = request.toPageable();
        Page<Complaint> result = complaintRepository.findAll(
                request.status(),
                request.category(),
                request.priority(),
                pageable
        );
        return complaintMapper.toListVTO(result);
    }


    public void validateComplaintCreation(ComplaintCreateDTO dto) {
        if (dto == null) {
            throw new BusinessException(INVALID_INPUT);
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
    }
}
