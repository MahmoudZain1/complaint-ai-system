package com.ecommerce.complaints.model.vto;

import com.ecommerce.complaints.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVTO {

    private Long id;
    private String email;
    private String fullName;
    private UserRole role;
    private boolean active;
    private LocalDateTime createdAt;
}
