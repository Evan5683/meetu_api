package com.example.meetu_api.controller;

import com.example.meetu_api.model.Event;
import com.example.meetu_api.model.Participant;
import com.example.meetu_api.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(event -> ResponseEntity.ok().body(event))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        return eventRepository.findById(id)
                .map(event -> {
                    event.setName(eventDetails.getName());
                    event.setDescription(eventDetails.getDescription());
                    event.setTime(eventDetails.getTime());
                    event.setLocation(eventDetails.getLocation());
                    Event updatedEvent = eventRepository.save(event);
                    return ResponseEntity.ok().body(updatedEvent);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(event -> {
                    eventRepository.delete(event);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/participants")
    public ResponseEntity<Participant> addParticipant(@PathVariable Long id,
            @RequestBody Participant participantDetails) {
        return eventRepository.findById(id)
                .map(event -> {
                    Participant participant = new Participant();
                    participant.setName(participantDetails.getName());
                    participant.setUserId(participantDetails.getUserId());
                    participant.setEvent(event);
                    event.getParticipants().add(participant);
                    eventRepository.save(event);
                    return ResponseEntity.ok().body(participant);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/participants/{participantId}")
    public ResponseEntity<?> removeParticipant(@PathVariable Long id, @PathVariable Long participantId) {
        return eventRepository.findById(id)
                .map(event -> {
                    event.getParticipants().removeIf(participant -> participant.getId().equals(participantId));
                    eventRepository.save(event);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
