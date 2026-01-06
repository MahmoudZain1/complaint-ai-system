package com.ecommerce.complaints.repoistory.api;

import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.enums.ComplaintCategory;
import com.ecommerce.complaints.model.enums.ComplaintStatus;
import com.ecommerce.complaints.model.enums.Priority;
import com.ecommerce.complaints.model.enums.Sentiment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ComplaintRepository {

    Complaint save(Complaint complaint);

    Complaint update(Complaint complaint);

    Optional<Complaint> findById(Long id);

    void delete (Long id);


    List<Complaint> findByStatus(ComplaintStatus status, int limit);

    long count();

    long countByStatus(ComplaintStatus status);

    Page<Complaint> findAll(ComplaintStatus status, ComplaintCategory category,
                            Priority priority, Sentiment sentiment, Pageable pageable);
}
