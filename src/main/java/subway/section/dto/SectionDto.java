package subway.section.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.line.entity.Line;
import subway.section.entity.Section;
import subway.station.entity.Station;

@Getter
@AllArgsConstructor
public class SectionDto {
    private Long id;
    private Long downStationId;
    private Long upStationId;
    private Integer distance;

    public SectionDto(Long downStationId, Long upStationId, Integer distance) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }

    public static SectionDto from(Section section) {
        return new SectionDto(
                section.getId(),
                section.getDownStation().getId(),
                section.getUpStation().getId(),
                section.getDistance()
        );
    }

    public Section toEntity(Line line, Station upStation, Station downStation) {
        return new Section(
                line,
                upStation,
                downStation,
                this.distance
        );
    }
}
