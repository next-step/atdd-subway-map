package subway.subwayline;

import subway.station.Station;
import subway.station.StationDto;

import java.util.Set;
import java.util.stream.Stream;

public class SubwayLineDto {

    private final Long id;
    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final Integer distance;
    private final Set<StationDto> stationDtos;

    public SubwayLineDto(Long id, String name, String color, Long upStationId, Long downStationId, Integer distance, Set<StationDto> stationDtos) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.stationDtos = stationDtos;
    }

    public static SubwayLineDto of(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        return new SubwayLineDto(
                null,
                name,
                color,
                upStationId,
                downStationId,
                distance,
                null
        );
    }

    public static SubwayLineDto of(SubwayLine subwayLine) {
        return new SubwayLineDto(
                subwayLine.getId(),
                subwayLine.getName(),
                subwayLine.getColor(),
                subwayLine.getUpStationId().getId(),
                subwayLine.getDownStationId().getId(),
                subwayLine.getDistance(),
                Stream.of(
                        StationDto.from(subwayLine.getUpStationId()),
                        StationDto.from(subwayLine.getDownStationId())
                ).collect(java.util.stream.Collectors.toSet())
        );
    }

    public SubwayLine toEntity(Station upStation, Station downStation) {
        return SubwayLine.of(
                name,
                color,
                upStation,
                downStation,
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

    public Set<StationDto> getStationDto() {
        return stationDtos;
    }
}
