package com.ecommerce.complaints.repoistory;

import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.enums.ComplaintCategory;
import com.ecommerce.complaints.model.enums.ComplaintStatus;
import com.ecommerce.complaints.model.enums.Priority;
import com.ecommerce.complaints.model.enums.Sentiment;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ComplaintSpecs {

    public static Specification<Complaint> filtered(ComplaintStatus status, ComplaintCategory category,
                                                    Priority priority, Sentiment sentiment) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (status != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), status));
            }
            if (category != null) {
                predicate = cb.and(predicate, cb.equal(root.get("category"), category));
            }
            if (priority != null) {
                predicate = cb.and(predicate, cb.equal(root.get("priority"), priority));
            }
            if (sentiment != null) {
                predicate = cb.and(predicate, cb.equal(root.get("sentiment"), sentiment));
            }
            return predicate;
        };
    }
}
