package subway.line.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import subway.section.domain.Section;
import subway.section.exception.InvalidSectionCreateException;
import subway.section.exception.InvalidSectionDeleteException;
import subway.station.domain.Station;
import subway.support.ErrorCode;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(length = 10, nullable = false)
    private String color;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

//    @OneToMany(mappedBy = "line", cascade = { CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
//    private List<Section> sections = new ArrayList<>();

    @Embedded
    private Sections sections = new Sections();

    public static Line firstLine(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(null, name, color, upStation, downStation, distance);
    }

    protected Line(Long id, String name, String color, Station upStation, Station downStation, int distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;

        this.sections = new Sections(List.of(Section.firstSection(this, upStation, downStation, distance)));
    }

    public void changeNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public boolean isLastStation(Long stationId) {
        if (!sections.isLastSection()) {
            return false;
        }

        return sections.isLastStation(stationId);
    }

    // TODO:
    public boolean isLastDownStation(Long stationId) {
        return sections.isLastDownStation(stationId);
    }

    public void addSection(Section section) {
        if (!section.isUpstation(downStation.getId())) {
            throw new InvalidSectionCreateException(ErrorCode.SECTION_CREATE_FAIL_BY_UPSTATION);
        }

        if (section.isDownstation(downStation.getId()) || section.isDownstation(upStation.getId())) {
            throw new InvalidSectionCreateException(ErrorCode.SECTION_CREATE_FAIL_BY_DOWNSTATION);
        }

        section.attachToLine(this);
        sections.appendSection(section);
    }

    public void deleteSection(Long stationId) {
        if (isLastStation(stationId)) {

            throw new InvalidSectionDeleteException(ErrorCode.SECTION_DELETE_FAIL_BY_LAST_STATION_REMOVED);
        }

        if (!isLastDownStation(stationId)) {
            throw new InvalidSectionDeleteException(ErrorCode.SECTION_DELETE_FAIL_BY_NOT_ALLOWED_STATION);
        }
    }

    public Section getSection(Long downStationId, Long upStationId) {
        return sections.get(downStationId, upStationId);
    }
}
