package subway.section.dto;

import lombok.Builder;
import lombok.Getter;
import subway.section.entity.Section;
import subway.station.entity.Station;
import subway.subwayline.entity.SubwayLine;

@Getter
public class SectionDto {

    private Long id;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    @Builder
    public SectionDto(Long id, Long upStationId, Long downStationId, Integer distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionDto of(Long id, Long upStationId, Long downStationId, Integer distance) {
        return SectionDto.builder()
                .id(id)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build();
    }

    public static SectionDto from(Section section) {
        return SectionDto.builder()
                .id(section.getId())
                .upStationId(section.getUpStation().getId())
                .downStationId(section.getDownStation().getId())
                .distance(section.getDistance())
                .build();
    }

    public Section toEntity(SubwayLine subwayLine, Station upStation, Station downStation, Integer distance) {
        return Section.builder()
                .subwayLine(subwayLine)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }
}
