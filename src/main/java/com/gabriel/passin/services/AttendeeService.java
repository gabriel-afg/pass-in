package com.gabriel.passin.services;

import com.gabriel.passin.domain.attendee.Attendee;
import com.gabriel.passin.domain.attendee.exception.AttendeeAlreadyRegisteredException;
import com.gabriel.passin.domain.attendee.exception.AttendeeNotFoundException;
import com.gabriel.passin.domain.checkin.CheckIn;
import com.gabriel.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.gabriel.passin.dto.attendee.AttendeeDetails;
import com.gabriel.passin.dto.attendee.AttendeesListResponseDTO;
import com.gabriel.passin.dto.attendee.AttendeeBadgeDTO;
import com.gabriel.passin.repositories.AttendeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AttendeeService {

    @Autowired
    private AttendeeRepository attendeeRepository;

    @Autowired
    private CheckInService checkInService;

    public List<Attendee> getAttendeesFromEvent(String eventId) {
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventsAttendee(String eventId) {
        return new AttendeesListResponseDTO(
                this.getAttendeesFromEvent(eventId).stream().map(attendee -> {
                    LocalDateTime checkedInAt = this.checkInService.getCheckIn(attendee.getId())
                            .map(CheckIn::getCreatedAt).orElse(null);
                    return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
                }).toList()
        );
    }

    public void registerAttendee(Attendee newAttendee) {
        this.attendeeRepository.save(newAttendee);
    }

    public void verifyAttendeeSubscription(String email, String eventId) {
        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);

        if (isAttendeeRegistered.isPresent())
            throw new AttendeeAlreadyRegisteredException("Attendee is already registered");

    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder) { // Retornar crachá
        Attendee attendee = getAttendee(attendeeId);
        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();
        AttendeeBadgeDTO response = new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId());

        return new AttendeeBadgeResponseDTO(response);
    }

    public void checkInAttendee(String attendeeId) {
        Attendee attendee = this.getAttendee(attendeeId);

        this.checkInService.registerCheckIn(attendee);
    }

    private Attendee getAttendee(String attendeeId) {
        return this.attendeeRepository.findById(attendeeId).orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with id" + attendeeId));
    }
}
