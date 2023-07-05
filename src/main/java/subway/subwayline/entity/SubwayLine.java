package subway.subwayline.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.exception.ErrorCode;
import subway.exception.SubwayException;
import subway.section.entity.Section;
import subway.station.entity.Station;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubwayLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStationId;

    private Integer distance;

    @OneToMany(mappedBy = "subwayLine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    @Builder
    public SubwayLine(String name, String color, Station upStationId, Station downStationId, Integer distance, Section section) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.addSection(section);
    }

    public static SubwayLine of(String name, String color, Station upStationId, Station downStationId, Integer distance) {
        return SubwayLine.builder()
                .name(name)
                .color(color)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build();
    }

    public void modifySubwayLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        this.sections.add(section);
        section.addSection(this);
    }

    public boolean isExistsDownStation(Station upStation) {
        return this.downStationId.equals(upStation);
    }

    public boolean isExistsStations(Station station) {
        return Arrays.asList(upStationId, downStationId).contains(station);
    }

    public void removeSection(Station downStation) {
        if (isSectionOne()) {
            throw new SubwayException(ErrorCode.SECTION_IS_ONE);
        }
        if (!isLastSection(downStation)) {
            throw new SubwayException(ErrorCode.NOT_DOWN_STATION);
        }
        this.sections.removeIf(section -> section.isDownStation(downStation));
    }

    private boolean isSectionOne() {
        return this.sections.size() == 1;
    }

    private boolean isLastSection(Station downStation) {
        return getLastSection()
                .map(section -> section.isDownStation(downStation))
                .orElse(false);
    }

    private Optional<Section> getLastSection() {
        if (sections.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(sections.get(sections.size() - 1));
        }
    }
}
