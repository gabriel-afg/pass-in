package com.gabriel.passin.controllers;

import com.gabriel.passin.dto.attendee.AttendeesListResponseDTO;
import com.gabriel.passin.dto.event.EventIdDTO;
import com.gabriel.passin.dto.event.EventRequestDTO;
import com.gabriel.passin.dto.event.EventResponseDTO;
import com.gabriel.passin.services.AttendeeService;
import com.gabriel.passin.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private AttendeeService attendeeService;

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String eventId){
        EventResponseDTO event = this.eventService.getEventDetail(eventId);

        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO data, UriComponentsBuilder uriComponentsBuilder){
        EventIdDTO newEvent = this.eventService.createEvent(data);
        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(newEvent.eventId()).toUri();

        return ResponseEntity.created(uri).body(newEvent);
    }

    @GetMapping("/attendees/{id}")
    public ResponseEntity<AttendeesListResponseDTO> getEventAttendees(@PathVariable String id){
        AttendeesListResponseDTO response = this.attendeeService.getEventsAttendee(id);

        return ResponseEntity.ok(response);
    }
}
