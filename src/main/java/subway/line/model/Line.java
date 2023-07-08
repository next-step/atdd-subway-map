package subway.line.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.exception.SubwayBadRequestException;
import subway.line.constant.LineMessage;
import subway.station.model.Station;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Line {

    private final long MINIMAL_SECTION_SIZE = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @ManyToOne
    @JoinColumn
    private Station upStation;

    @ManyToOne
    @JoinColumn
    private Station downStation;

    @Builder.Default
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    public void updateLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        this.sections.add(section);
        this.downStation = section.getDownStation();
        section.setLine(this);
    }

    public List<Station> getStationsInSections() {
        List<Station> stations = new ArrayList<>();
        stations.add(this.upStation);
        for (Section section : this.sections) {
            stations.add(section.getDownStation());
        }
        return stations;
    }

    public Section deleteSectionByStation(Station station) {
        if (this.sections.size() < MINIMAL_SECTION_SIZE) {
            throw new SubwayBadRequestException(LineMessage.DOWN_STATION_MINIMAL_VALID_MESSAGE.getFormatMessage(MINIMAL_SECTION_SIZE));
        }
        if (!this.downStation.equals(station)) {
            throw new SubwayBadRequestException(LineMessage.SECTION_DELETE_LAST_STATION_VALID_MESSAGE.getMessage());
        }

        final int lastElementIndex = this.sections.size() - 1;
        Section lastSection = this.sections.get(lastElementIndex);
        this.downStation = lastSection.getUpStation();
        this.sections.remove(lastSection);
        return lastSection;
    }
}
