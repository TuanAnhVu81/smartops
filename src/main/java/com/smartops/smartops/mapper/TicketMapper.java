package com.smartops.smartops.mapper;

import com.smartops.smartops.dto.response.TicketResponse;
import com.smartops.smartops.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(source = "createdByUser.fullName", target = "createdByName")
    @Mapping(source = "assignedTo.fullName", target = "assignedToName")
    @Mapping(source = "createdByUser.department.name", target = "departmentName")
    TicketResponse toResponse(Ticket ticket);

    List<TicketResponse> toResponseList(List<Ticket> tickets);
}
