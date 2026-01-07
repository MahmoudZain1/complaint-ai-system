package com.ecommerce.complaints.repository;

import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.enums.ComplaintCategory;
import com.ecommerce.complaints.model.enums.ComplaintStatus;
import com.ecommerce.complaints.model.enums.Priority;
import com.ecommerce.complaints.model.enums.Sentiment;
import com.ecommerce.complaints.repository.api.ComplaintRepository;
import com.ecommerce.complaints.repository.jpa.ComplaintJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ComplaintRepositoryImpl implements ComplaintRepository {

    private final ComplaintJpaRepository jpaRepository;


    @Override
    public Complaint save(Complaint complaint) {
        complaint.setCreatedAt(LocalDateTime.now());
        complaint.setUpdatedAt(LocalDateTime.now());
        complaint.setStatus(ComplaintStatus.NEW);
        return jpaRepository.save(complaint);
    }

    @Override
    public Complaint update(Complaint complaint) {
        complaint.setStatus(ComplaintStatus.IN_PROGRESS);
        complaint.setUpdatedAt(LocalDateTime.now());
        return jpaRepository.save(complaint);
    }

    @Override
    public Optional<Complaint> findById(Long id) {return jpaRepository.findById(id);}

    @Override
    public void delete(Long id) {jpaRepository.deleteById(id);}


    @Override
    public List<Complaint> findByStatus(ComplaintStatus status, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return jpaRepository.findByStatusOrderByCreatedAtAsc(status, pageable);}

    @Override
    public long count() {return jpaRepository.count();}

    @Override
    public long countByStatus(ComplaintStatus status) {return jpaRepository.countByStatus(status);}

    @Override
    public Page<Complaint> findAll(ComplaintStatus status, ComplaintCategory category,
                                   Priority priority, Sentiment sentiment, Pageable pageable) {
        Specification<Complaint> spec = ComplaintSpecs.filtered(status, category, priority, sentiment);
        return jpaRepository.findAll(spec , pageable);
    }
}
