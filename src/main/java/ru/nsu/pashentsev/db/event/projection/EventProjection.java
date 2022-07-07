package ru.nsu.pashentsev.db.event.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class EventProjection {

    private String name;

    private String eventType;

    private String eventPlace;

    private String eventDate;

}
