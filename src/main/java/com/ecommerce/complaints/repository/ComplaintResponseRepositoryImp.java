package com.ecommerce.complaints.repository;

import com.ecommerce.complaints.model.entity.ComplaintResponse;
import com.ecommerce.complaints.model.enums.ResponseStatus;
import com.ecommerce.complaints.repository.api.ComplaintResponseRepository;
import com.ecommerce.complaints.repository.jpa.ComplaintResponseJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public Optional<ComplaintResponse> findById(Long id) {return repository.findById(id);}

    @Override
    public boolean existsByComplaintId(Long complaintId) {
        return repository.existsByComplaintId(complaintId);
    }



    @Override
    public List<ComplaintResponse> findPendingApprovals() {
        return repository.findPendingWithComplaint(ResponseStatus.PENDING_APPROVAL);
    }

    @Override
    public long countByStatus(ResponseStatus status) {
        return repository.countByStatus(status);
    }

    @Override
    public long count() {return repository.count();}

    @Override
    public List<ComplaintResponse> findAll() {return repository.findAll();}
}
