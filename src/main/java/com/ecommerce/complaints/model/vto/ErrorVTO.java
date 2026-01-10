package com.ecommerce.complaints.model.vto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorVTO {

    private String code;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    private Integer status;
    private List<String> details;
}