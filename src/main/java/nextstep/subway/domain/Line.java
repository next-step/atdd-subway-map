package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.exception.ExceptionMessages;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.ALL}, orphanRemoval = true)
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

    public void addSection(Section sections) {
        this.sections.add(sections);
    }

    public void changeNameAndColor(LineRequest lineRequest) {
        this.name = lineRequest.getName();
        this.color = lineRequest.getColor();
    }

    public void checkRegisterEndpointId(long upStationId, long downEndpointStationId) {
        if (upStationId != downEndpointStationId) {
            throw new IllegalArgumentException(
                    ExceptionMessages.getNotEndpointInputExceptionMessage(upStationId, downEndpointStationId));
        }
    }

    public void checkRemoveEndPointId(long stationId, long downEndpointStationId) {
        if (downEndpointStationId != stationId) {
            throw new IllegalArgumentException(
                    ExceptionMessages.getNotEndpointInputExceptionMessage(stationId, downEndpointStationId));
        }
    }

    public Station getUpEndpoint() {
        List<Long> upStationIds = getUpStationIds(sections);
        List<Long> downStationIds = getDownStationIds(sections);
        upStationIds.removeAll(downStationIds);
        Long upStationId = upStationIds.get(0);
        return sections.stream()
                .map(Section::getUpStation)
                .filter(upStation -> upStation.getId().equals(upStationId))
                .collect(Collectors.toList())
                .get(0);
    }

    public Station getDownEndpoint() {
        List<Long> upStationIds = getUpStationIds(sections);
        List<Long> downStationIds = getDownStationIds(sections);
        downStationIds.removeAll(upStationIds);
        Long downStationId = downStationIds.get(0);
        return sections.stream()
                .map(Section::getDownStation)
                .filter(downStation -> downStation.getId().equals(downStationId))
                .collect(Collectors.toList())
                .get(0);
    }

    private List<Long> getDownStationIds(List<Section> sections) {
        return sections.stream()
                .map(v -> v.getDownStation().getId())
                .collect(Collectors.toList());
    }

    private List<Long> getUpStationIds(List<Section> sections) {
        return sections.stream()
                .map(v -> v.getUpStation().getId())
                .collect(Collectors.toList());
    }
}
