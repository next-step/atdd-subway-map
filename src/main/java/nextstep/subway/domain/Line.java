package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.SectionRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static nextstep.subway.domain.utils.LineUtils.*;
import static nextstep.subway.domain.utils.SectionUtils.*;
import static nextstep.subway.domain.utils.StationUtils.isSameStation;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void changeLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        this.sections.add(section);
        section.setLine(this);
    }


    public Section newSection(Station upStation, Station downStation,SectionRequest request) {
        getLastSection(sections)
                .downStationIsNot(request.getUpStationId());

        downStation.notEqualsIn(getIdsIn(sections));
        return new Section(upStation,downStation,request.getDistance());
    }


    public void removeSection(Long stationId) {
        validSectionsSize(sections.size());
        Section lastSection = getLastSection(sections);
        if(isSameStation(stationId, getDownStationIdIn(lastSection))){
            sections.remove(lastSection);
        }
    }
}
