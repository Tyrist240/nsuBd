package ru.nsu.pashentsev.db.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.pashentsev.db.event.dto.EventDTO;
import ru.nsu.pashentsev.db.event.dto.EventResponseDTO;
import ru.nsu.pashentsev.db.event.dto.PeriodOrganizerDTO;
import ru.nsu.pashentsev.db.event.projection.ArtistWithPlaceProjection;
import ru.nsu.pashentsev.db.event.projection.EventProjection;
import ru.nsu.pashentsev.db.event.sortingfilter.EventSearchParams;

import java.util.List;

@RestController
@RequestMapping(value = "/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping(value = "/add",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public EventResponseDTO createEvent(@RequestBody EventDTO eventDTO) {
        if (eventDTO.getId() != null && eventService.isAlreadyExists(eventDTO)) {
            return null;
        }

        return eventService.saveEvent(eventDTO);
    }

    @PostMapping(value = "/fetch/page",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<EventResponseDTO> fetchEventsPage(@RequestBody EventSearchParams eventSearchParams) {
        if (eventSearchParams.getPageNumber() == null || eventSearchParams.getPageSize() == null) {
            return null;
        }

        return eventService.fetchEventsPage(eventSearchParams).getContent();
    }

    @PostMapping(value = "/fetch/list",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<EventResponseDTO> fetchEventsList(@RequestBody EventSearchParams eventSearchParams) {
        return eventService.fetchEventsList(eventSearchParams);
    }

    @PostMapping(value = "/update",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public EventResponseDTO updateEvent(@RequestBody EventDTO eventDTO) {
        if (eventDTO.getId() == null || !eventService.isAlreadyExists(eventDTO)) {
            return null;
        }

        return eventService.saveEvent(eventDTO);
    }

    @PostMapping(value = "/delete/{id}")
    public void deleteEvent(@PathVariable("id") Integer eventId) {
        eventService.deleteEvent(eventId);
    }

    @PostMapping(value = "/getEventsInPeriodOrByOrganizer",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<EventProjection> getEventsInPeriodOrByOrganizer(@RequestBody PeriodOrganizerDTO periodOrganizerDTO) {
        return eventService.getEventsInPeriodOrByOrganizer(
            periodOrganizerDTO.getFrom(),
            periodOrganizerDTO.getTo(),
            periodOrganizerDTO.getId()
        );
    }

    @PostMapping(value = "/getArtistsWithPlaces/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ArtistWithPlaceProjection> getArtistsWithPlaces(@PathVariable("id") Integer eventId) {
        return eventService.getArtistsWithPlaces(eventId);
    }

    @PostMapping(value = "/getEventsInBuilding/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EventProjection> getEventsInBuilding(@PathVariable("id") Integer buildingId) {
        return eventService.getEventsInBuilding(buildingId);
    }

}
