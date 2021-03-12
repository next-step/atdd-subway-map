package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.exception.CanNotMatchUpStationException;
import nextstep.subway.exception.CanNotRemoveSectionException;
import nextstep.subway.exception.ExistDownStationException;
import nextstep.subway.section.Section;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.*;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(Section section) {
        if(isEmptySections()) {
            setSection(section);
            return;
        }
        checkValidStation(section);
        setSection(section);
    }

    private boolean isEmptySections() {
        return sections.size() == 0;
    }

    private void setSection(Section section) {
        sections.add(section);
        section.addLine(this);
    }

    private void checkValidStation(Section section) {
        checkValidDownStation(section);
        checkValidUpStation(section);
    }

    private void checkValidUpStation(Section section) {
        Section lastSection = getLastSection();
        if(!lastSection.isEqual(section.getUpStation())) {
            throw new CanNotMatchUpStationException();
        }
    }

    private Section getLastSection() {
        return sections.get(sections.size() - 1);
    }

    private void checkValidDownStation(Section section) {
        boolean isExist = isExistDownStation(section.getDownStation());
        if(isExist) {
            throw new ExistDownStationException();
        }
    }

    private boolean isExistDownStation(Station downStation) {
        return sections.stream()
                .anyMatch(section -> isExistDownStation(section, downStation));
    }

    private boolean isExistDownStation(Section section, Station downStation) {
        return section.isExistStation(downStation);
    }

    public void remove() {
        checkValidSection();
        removeSection();
    }

    private void removeSection() {
        sections.remove(sections.size() - 1);
    }

    private void checkValidSection() {
        if(sections.size() == 1) {
            throw new CanNotRemoveSectionException();
        }
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
        return Collections.unmodifiableList(sections);
    }

    public List<Station> getStations() {
        return getStationsFromSections();
    }

    private List<Station> getStationsFromSections() {
        List<Station> stations = sections.stream()
                .map(Section::getUpStation)
                .collect(toList());

        stations.add(getLastDownStation());
        return stations;
    }

    private Station getLastDownStation() {
        return getLastSection().getDownStation();
    }

}
