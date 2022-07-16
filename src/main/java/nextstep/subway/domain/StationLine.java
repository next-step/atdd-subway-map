package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationRequest;

import javax.persistence.*;
import java.util.List;
import nextstep.subway.constant.Message;
import nextstep.subway.exception.IllegalUpdatingStateException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StationLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    private Long upStationId;

    private Long downStationId;

    private Integer distance;

    @Embedded
    private Sections sections = new Sections();

    @Builder
    public StationLine(String name, String color, Long upStationId, Long downStationId, Integer distance, Sections sections) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.sections = sections;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Section addSection(Station upStation, Station downStation, Integer distance) {
        Section section = Section.builder()
            .upStation(upStation)
            .downStation(downStation)
            .distance(distance)
            .build();

        updateDownStation(downStation);
        plusDistance(distance);
        section.updateStationLine(this);

        return sections.add(section);
    }

    public void updateDownStation(Station downStation) {
        this.downStationId = downStation.getId();
    }

    public void plusDistance(Integer distance) {
        this.distance += distance;
    }

    public void minusDistance(Integer distance) {
        this.distance -= distance;
    }

    public Section removeSection(Long stationId) {
        validateDownStation(stationId);

        Section section = sections.remove(stationId);
        Section lastSection = sections.getLastSection();

        updateDownStation(lastSection.getDownStation());
        minusDistance(section.getDistance());

        return section;
    }

    public void validateDownStation(Long stationId) {
        if(!downStationId.equals(stationId)) {
            throw new IllegalUpdatingStateException(Message.ONLY_DELETE_DOWNSTAION.getValue());
        }
    }

}
