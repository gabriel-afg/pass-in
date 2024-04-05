package com.gabriel.passin.services;

import com.gabriel.passin.domain.attendee.Attendee;
import com.gabriel.passin.domain.event.Event;
import com.gabriel.passin.domain.event.exceptions.EventNotFoundException;
import com.gabriel.passin.dto.attendee.AttendeeIdDTO;
import com.gabriel.passin.dto.attendee.AttendeeRequestDTO;
import com.gabriel.passin.dto.event.EventIdDTO;
import com.gabriel.passin.dto.event.EventRequestDTO;
import com.gabriel.passin.dto.event.EventResponseDTO;
import com.gabriel.passin.repositories.EventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class EventServiceTest {

    @Autowired
    private EventService eventService;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private AttendeeService attendeeService;

    @Test
    @DisplayName("Should return event detail when event id exists")
    void shouldReturnEventDetailWhenEventIdExists() {
        Event event = new Event();
        when(eventRepository.findById(anyString())).thenReturn(java.util.Optional.of(event));
        when(attendeeService.getAttendeesFromEvent(anyString())).thenReturn(Collections.emptyList());

        EventResponseDTO eventResponseDTO = eventService.getEventDetail("testEventId");

        verify(eventRepository, times(1)).findById(anyString());
        verify(attendeeService, times(1)).getAttendeesFromEvent(anyString());
        assertNotNull(eventResponseDTO);
    }

    @Test
    @DisplayName("Should throw EventNotFoundException when event id does not exist")
    void shouldThrowEventNotFoundExceptionWhenEventIdDoesNotExist() {
        when(eventRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.getEventDetail("testEventId"));

        verify(eventRepository, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("Should register attendee when event has space")
    void shouldRegisterAttendeeWhenEventHasSpace() {
        AttendeeRequestDTO attendeeRequestDTO = new AttendeeRequestDTO("Test Name", "test@example.com");
        Event event = new Event();
        event.setMaximun_attendees(100);
        when(eventRepository.findById(anyString())).thenReturn(java.util.Optional.of(event));
        when(attendeeService.getAttendeesFromEvent(anyString())).thenReturn(Collections.emptyList());

        AttendeeIdDTO attendeeIdDTO = eventService.registerAttendeeOnEvent("testEventId", attendeeRequestDTO);

        verify(attendeeService, times(1)).verifyAttendeeSubscription(anyString(), anyString());
        verify(eventRepository, times(1)).findById(anyString());
        verify(attendeeService, times(1)).getAttendeesFromEvent(anyString());
        verify(attendeeService, times(1)).registerAttendee(any(Attendee.class));
        assertNotNull(attendeeIdDTO);
    }

    @Test
    @DisplayName("Should create event when valid request is given")
    void shouldCreateEventWhenValidRequestIsGiven() {
        EventRequestDTO eventRequestDTO = new EventRequestDTO("Test Title", "Test Details", 100);
        Event event = new Event();
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventIdDTO eventIdDTO = eventService.createEvent(eventRequestDTO);

        verify(eventRepository, times(1)).save(any(Event.class));
        assertNotNull(eventIdDTO);
    }
}