package ru.nsu.pashentsev.db.building;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import ru.nsu.pashentsev.db.building.entity.Cinema;
import ru.nsu.pashentsev.db.building.entity.ConcertSquare;
import ru.nsu.pashentsev.db.building.entity.CulturePalace;
import ru.nsu.pashentsev.db.building.entity.Stage;
import ru.nsu.pashentsev.db.building.entity.Theatre;

public enum BuildingType {

    @JsonProperty("theatre")
    THEATRE("theatre", Theatre.class),

    @JsonProperty("stage")
    STAGE("stage", Stage.class),

    @JsonProperty("culturePalace")
    CULTURE_PALACE("culturePalace", CulturePalace.class),

    @JsonProperty("concertSquare")
    CONCERT_SQUARE("concertSquare", ConcertSquare.class),

    @JsonProperty("cinema")
    CINEMA("cinema", Cinema.class);

    @Getter
    private Class<?> clazz;

    BuildingType(String buildingType, Class<?> clazz) {
        this.clazz = clazz;
    }

}
