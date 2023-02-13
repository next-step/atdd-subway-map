package subway.line;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.exception.ErrorCode;
import subway.exception.SubwayException;
import subway.section.Section;
import subway.station.Station;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
        orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    @Column(nullable = false)
    private Long distance;

    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        this.sections.add(section);
        this.downStation = section.getDownStation();
    }

    public void deleteSection(Long stationId) {
        validateMultiSection();
        validateStationOnLine(stationId);
        validateEndStation(stationId);

        int lastIndex = sections.size() - 1;
        Section section = this.sections.get(lastIndex);
        this.downStation = section.getUpStation();
    }

    private void validateEndStation(Long stationId) {
        if (!stationId.equals(downStation.getId())) {
            throw new SubwayException(ErrorCode.NOT_END_STATION);
        }
    }

    private void validateStationOnLine(Long stationId) {
        boolean stationFound = sections.stream().anyMatch(section ->
            stationId.equals(section.getUpStation().getId()) ||
                stationId.equals(section.getDownStation().getId()));

        if (!stationFound) {
            throw new SubwayException(ErrorCode.NOT_FOUND_STATION);
        }
    }

    private void validateMultiSection() {
        if (sections.size() < 2) {
            throw new SubwayException(ErrorCode.INSUFFICIENT_SECTION);
        }
    }
}
