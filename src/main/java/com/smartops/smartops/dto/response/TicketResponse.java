package com.smartops.smartops.dto.response;

import java.time.LocalDateTime;

/**
 * Common response DTO for Ticket entities.
 */
public record TicketResponse(
        Long id,
        String title,
        String description,
        String ticketType,
        String status,
        String priority,
        String createdByName,
        String assignedToName,
        String departmentName,
        String rejectionReason,
        String attachmentUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime resolvedAt
) {}
