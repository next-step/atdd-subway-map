package subway.subwayline.dto;

import lombok.Builder;
import lombok.Getter;
import subway.station.entity.Station;
import subway.station.dto.StationDto;
import subway.subwayline.entity.SubwayLine;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class SubwayLineDto {

    private final Long id;
    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final Integer distance;
    private final Set<StationDto> stationDtos;

    @Builder
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
        return SubwayLineDto.builder()
                .name(name)
                .color(color)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build();
    }

    public static SubwayLineDto of(SubwayLine subwayLine) {
        return SubwayLineDto.builder()
                .id(subwayLine.getId())
                .name(subwayLine.getName())
                .color(subwayLine.getColor())
                .upStationId(subwayLine.getUpStationId().getId())
                .downStationId(subwayLine.getDownStationId().getId())
                .distance(subwayLine.getDistance())
                .stationDtos(Stream.of(
                                StationDto.from(subwayLine.getUpStationId()),
                                StationDto.from(subwayLine.getDownStationId())
                            ).collect(Collectors.toSet()))
                .build();
    }

    public SubwayLine toEntity(Station upStation, Station downStation) {
        return SubwayLine.builder()
                .name(name)
                .color(color)
                .upStationId(upStation)
                .downStationId(downStation)
                .distance(distance)
                .build();
    }
}
