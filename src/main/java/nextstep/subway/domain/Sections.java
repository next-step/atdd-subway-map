package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.StationResponse;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public List<Section> getSections() {
        return sections;
    }

    public Section getLastSection() {
        return sections.stream().reduce((first, second) -> second).orElse(null);
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public List<Station> createStationList() {
        return this.sections.stream()
                .flatMap(s -> Stream.of(s.getUpStation(), s.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
    }

    public List<StationResponse> createStationResponseList() {
        return createStationList().stream()
                .map(station -> new StationResponse(station.getId(), station.getName()
                        , station.getCreatedDate(), station.getModifiedDate()))
                .collect(Collectors.toList());
    }

    public void deleteSection(Long stationId) {
        sections.removeIf(s -> (s.getDownStation().getId() == stationId));
    }

}
