package com.ecommerce.complaints.repository.jpa;

import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.enums.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ComplaintJpaRepository extends JpaRepository<Complaint, Long>, JpaSpecificationExecutor<Complaint> {

}
