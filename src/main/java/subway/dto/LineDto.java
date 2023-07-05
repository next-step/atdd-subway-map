package subway.dto;

import subway.jpa.Line;

import java.util.List;

public class LineDto {

    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;
    private List<StationDto> stationDtos;

    public LineDto(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public LineDto(Long id, String name, String color, List<StationDto> stationDtos) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationDtos = stationDtos;
    }

    public static LineDto from(Line lineEntity, List<StationDto> stationDtos) {
        return new LineDto(
                lineEntity.getId(),
                lineEntity.getName(),
                lineEntity.getColor(),
                stationDtos
        );
    }

    public Line toEntity() {
        return new Line(
                name,
                color,
                upStationId,
                downStationId,
                distance
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }

    public List<StationDto> getStationDtos() {
        return stationDtos;
    }
}
