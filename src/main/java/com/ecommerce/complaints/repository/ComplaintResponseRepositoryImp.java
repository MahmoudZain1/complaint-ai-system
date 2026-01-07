package com.ecommerce.complaints.repository;

import com.ecommerce.complaints.model.entity.ComplaintResponse;
import com.ecommerce.complaints.repository.api.ComplaintResponseRepository;
import com.ecommerce.complaints.repository.jpa.ComplaintResponseJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class ComplaintResponseRepositoryImp implements ComplaintResponseRepository {

    private final ComplaintResponseJpaRepository repository;

    @Override
    public ComplaintResponse save(ComplaintResponse complaintResponse) {
        complaintResponse.setGeneratedAt(LocalDateTime.now());
        return repository.save(complaintResponse);
    }

    @Override
    public boolean existsByComplaintId(Long complaintId) {
        return repository.existsByComplaintId(complaintId);
    }
}
