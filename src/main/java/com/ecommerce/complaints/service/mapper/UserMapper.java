package com.ecommerce.complaints.service.mapper;

import com.ecommerce.complaints.model.entity.User;
import com.ecommerce.complaints.model.enums.UserRole;
import com.ecommerce.complaints.model.generate.RegisterRequest;
import com.ecommerce.complaints.model.vto.UserVTO;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {


    @Mapping(target = "role", ignore = true)
    User toEntity(RegisterRequest request);

     UserVTO toVTO(User entity);


}
