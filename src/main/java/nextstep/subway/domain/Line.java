package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.SectionRequest;

import javax.persistence.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Entity
public class Line extends BaseEntity {
    public static final int MIN_SECTION_COUNT = 2;

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

    public Set<Station> getStations() {
        Set<Station> stations = new HashSet<>();
        sections.forEach(section -> {
                    stations.add(section.getUpStation());
                    stations.add(section.getDownStation());
                });
        return stations;
    }

    public void changeLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void createSection(Section section) {
        this.sections.add(section);
        section.setLine(this);
    }


    public void addSection(Station upStation, Station downStation, SectionRequest request) {
        getLastSection(sections)
                .downStationIsNot(request.getUpStationId());
        downStation.notExistsIn(getIdsIn(sections));
        Section newSection = new Section(upStation, downStation, request.getDistance());
        createSection(newSection);
    }


    public void removeSection(Long stationId) {
        validSectionsSize(sections.size());
        Section lastSection = getLastSection(sections);
        if (Objects.equals(stationId, getDownStationIdIn(lastSection))) {
            sections.remove(lastSection);
        }
    }



    private void validSectionsSize(int size) {
        if (size < MIN_SECTION_COUNT) {
            throw new IllegalArgumentException("삭제할 수 있는 구간이 존재하지 않습니다");
        }
    }

    private Section getLastSection(List<Section> sections) {
        int sectionLastIndex = sections.size() - 1;
        return sections.get(sectionLastIndex);
    }

    private List<Long> getIdsIn(List<Section> sections) {
        return sections.stream()
                .map(Section::getStationIds)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private Long getDownStationIdIn(Section section) {
        return section.getDownStation().getId();
    }

}
