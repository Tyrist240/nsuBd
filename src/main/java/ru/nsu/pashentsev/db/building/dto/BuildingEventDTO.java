package ru.nsu.pashentsev.db.building.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.nsu.pashentsev.db.building.BuildingType;
import ru.nsu.pashentsev.db.building.projections.BuildingEventProjection;

import java.util.List;

@AllArgsConstructor(onConstructor=@__(@JsonCreator))
@Getter
public class BuildingEventDTO {

    private String buildingName;

    private Integer buildingCapacity;

    private Integer buildingDiagonal;

    private String buildingAddress;

    private BuildingType buildingType;

    List<BuildingEventProjection> buildingEventProjectionList;

}
