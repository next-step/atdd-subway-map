package subway.line.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.exception.SubwayBadRequestException;
import subway.line.constant.LineMessage;
import subway.station.model.Station;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Line {

    private static final long MINIMAL_SECTION_SIZE = 2L;

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
    @Embedded
    private LineSection lineSection = new LineSection();

    public void updateLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        this.lineSection.add(section, this);
        this.downStation = section.getDownStation();
    }

    public List<Station> getStationsInSections() {
        List<Station> stations = new ArrayList<>();
        stations.add(this.upStation);
        stations.addAll(lineSection.getDownStations());
        return stations;
    }

    public void deleteSectionByStation(Station station) {
        Section lastSection = lineSection.deleteSectionByStation(station, this.downStation);
        this.downStation = lastSection.getUpStation();
    }
}
