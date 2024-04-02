package com.gabriel.passin.dto.event;

public record EventDetailDTO(
        String id,
        String title,
        String details,
        String slug,
        Integer maximunAttendees,
        Integer attendeesAmount
) {
}
