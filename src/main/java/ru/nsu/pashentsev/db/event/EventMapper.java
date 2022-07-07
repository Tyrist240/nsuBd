package ru.nsu.pashentsev.db.event;

import ru.nsu.pashentsev.db.common.utils.DateTimeFormatter;
import ru.nsu.pashentsev.db.event.dto.EventDTO;
import ru.nsu.pashentsev.db.event.dto.EventResponseDTO;

public class EventMapper {

    static EventResponseDTO toEventDto(Event event) {
        return new EventResponseDTO(
            event.getId(),
            event.getName(),
            EventType.MAPPINGS.get(event.getEventType()).name(),
            String.valueOf(event.getEventPlace()),
            DateTimeFormatter.getFormattedDateFromTimestamp(event.getEventDate())
        );
    }

    static Event toEvent(EventDTO eventDto) {
        return new Event(
            eventDto.getId(),
            eventDto.getName(),
            eventDto.getEventType(),
            eventDto.getEventPlace(),
            eventDto.getEventDate()
        );
    }

}
