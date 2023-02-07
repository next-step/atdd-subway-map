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
        if(!isLineDownStationEqualsSectionUpStation(section.getUpStation()))
            throw new DomainException(DomainExceptionType.UPDOWN_STATION_MISS_MATCH);

        if(isContainStation(section.getDownStation()))
            throw new DomainException(DomainExceptionType.DOWN_STATION_EXIST_IN_LINE);


        this.downStationId = section.getDownStation().getId();
        sections.addSections(section);
        lineDistance += section.getSectionDistance();
    }

    public void deleteSection(Station station) {
        if(!isLineDownStation(station))
            throw new DomainException(DomainExceptionType.NOT_DOWN_STATION);

        if(hasOnlyOneSection())
            throw new DomainException(DomainExceptionType.CANT_DELETE_SECTION);

        Section targetSection = sections.getSectionByDownStatoinId(station.getId());
        this.lineDistance -= targetSection.getSectionDistance();
        this.downStationId = targetSection.getUpStation().getId();
        sections.deletionSection(station);
    }

    private boolean isLineDownStationEqualsSectionUpStation(Station newSectionUpStation){
        return this.downStationId == newSectionUpStation.getId();
    }

    private boolean isContainStation(Station station){
        return getStationList().contains(station);
    }

    private boolean hasOnlyOneSection(){
        return sections.getSectionCount() == 1;
    }

    private boolean isLineDownStation(Station station){
        return this.downStationId == station.getId();
    }
}
