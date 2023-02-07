package subway.domain;

import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.common.DomainException;
import subway.common.DomainExceptionType;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LINE")
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LINE_ID")
    private Long id;

    @Column(name = "LINE_NAME", length = 20, nullable = false)
    private String name;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "UP_STATION_ID")
    private Long upStationId;

    @Column(name = "DOWN_STATION_ID")
    private Long downStationId;

    @Column(name = "LINE_DISTANCE")
    private Integer lineDistance;

    @Embedded private Sections sections = new Sections();

    public Line(String name, String color, Long downStationId, Long upStationId, Integer distance) {
        this.name = name;
        this.color = color;
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.lineDistance = distance;
    }

    public void updateNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public List<Station> getStationList() {
        return sections.getStations();
    }

    public void addSection(Section section) {
        if(!isEqualsLineDownStationAndSectionUpStation(section.getUpStation()))
            throw new DomainException(DomainExceptionType.UPDOWN_STATION_MISS_MATCH);

        if(isContainStation(section.getDownStation()))
            throw new DomainException(DomainExceptionType.DOWN_STATION_EXIST_IN_LINE);


        this.downStationId = section.getDownStation().getId();
        sections.addSections(section);
        lineDistance += section.getSectionDistance();
    }

    public Integer getSectionCount() {
        return sections.getSectionCount();
    }

    public void deleteSection(Station station) {
        this.downStationId =
                sections.getSectionByDownStatoinId(station.getId()).getUpStation().getId();
        sections.deletionSection(station);
    }

    private boolean isEqualsLineDownStationAndSectionUpStation(Station newSectionUpStation){
        return this.getDownStationId() == newSectionUpStation.getId();
    }

    private boolean isContainStation(Station station){
        return getStationList().contains(station);
    }
}
