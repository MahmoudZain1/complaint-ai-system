package com.ecommerce.complaints.service.mapper;

import com.ecommerce.complaints.model.entity.User;
import com.ecommerce.complaints.model.generate.RegisterRequest;
import com.ecommerce.complaints.model.vto.UserVTO;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "complaints", ignore = true)
    public abstract User toEntity(RegisterRequest request);

    public abstract UserVTO toVTO(User entity);

}
