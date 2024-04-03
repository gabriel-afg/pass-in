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
import com.gabriel.passin.repositories.CheckinRepository;
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
    private CheckinRepository checkinRepository;

    public List<Attendee> getAttendeesFromEvent(String eventId) {
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventsAttendee(String eventId) {
        List<Attendee> attendeeList = this.getAttendeesFromEvent(eventId);

        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkinRepository.findByAttendeeId(attendee.getId());
            LocalDateTime checkedInAt = checkIn.map(CheckIn::getCreatedAt).orElse(null);

            return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailsList);
    }

    public Attendee registerAttendee(Attendee newAttendee) {
        return this.attendeeRepository.save(newAttendee);
    }

    public void verifyAttendeeSubscription(String email, String eventId) {
        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);

        if (isAttendeeRegistered.isPresent())
            throw new AttendeeAlreadyRegisteredException("Attendee is already registered");

    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder) { // Retornar crachá
        Attendee attendee = this.attendeeRepository.findById(attendeeId).orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with id" + attendeeId));
        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();
        AttendeeBadgeDTO response = new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId());

        return new AttendeeBadgeResponseDTO(response);
    }
}
