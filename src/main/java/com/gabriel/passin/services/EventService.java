package com.gabriel.passin.services;

import com.gabriel.passin.domain.attendee.Attendee;
import com.gabriel.passin.domain.event.Event;
import com.gabriel.passin.domain.event.exceptions.EventFullException;
import com.gabriel.passin.domain.event.exceptions.EventNotFoundException;
import com.gabriel.passin.dto.attendee.AttendeeIdDTO;
import com.gabriel.passin.dto.attendee.AttendeeRequestDTO;
import com.gabriel.passin.dto.event.EventIdDTO;
import com.gabriel.passin.dto.event.EventRequestDTO;
import com.gabriel.passin.dto.event.EventResponseDTO;
import com.gabriel.passin.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AttendeeService attendeeService;

    public EventResponseDTO getEventDetail(String eventId){
        Event event = getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAttendeesFromEvent(eventId);

        return new EventResponseDTO(event, attendeeList.size());
    }

    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeRequestDTO){ // Registrar participante
        this.attendeeService.verifyAttendeeSubscription(attendeeRequestDTO.email(),eventId); // verifica se o participante já está no evento
        Event event = getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAttendeesFromEvent(eventId);

        if (event.getMaximun_attendees() <= attendeeList.size()) throw new EventFullException("Event is full");

        Attendee newAttendee = createNewAttendee(attendeeRequestDTO, event);

        this.attendeeService.registerAttendee(newAttendee);

        return new AttendeeIdDTO(newAttendee.getId());
    }

    public EventIdDTO createEvent(EventRequestDTO data){
        Event newEvent = new Event();

        newEvent.setTitle(data.title());
        newEvent.setDetails(data.details());
        newEvent.setMaximun_attendees(data.maximunAttendees());
        newEvent.setSlug(createSlug(data.title()));

        this.eventRepository.save(newEvent);

        return new EventIdDTO(newEvent.getId());
    }

    private Event getEventById(String eventId){
        return this.eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Evento não encontrado (id)"+eventId));
    }

    private Attendee createNewAttendee(AttendeeRequestDTO attendeeRequestDTO, Event event) {
        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendeeRequestDTO.name());
        newAttendee.setEmail(attendeeRequestDTO.email());
        newAttendee.setEvent(event);
        newAttendee.setCreatedAt(LocalDateTime.now());
        return newAttendee;
    }

    private static String createSlug(String title){
        return Normalizer.normalize(title, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }

}
