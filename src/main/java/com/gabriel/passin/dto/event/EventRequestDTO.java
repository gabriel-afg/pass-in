package com.gabriel.passin.dto.event;

public record EventRequestDTO(
        String title,
        String details,
        Integer maximunAttendees
) {
}
