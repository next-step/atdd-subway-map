package nextstep.subway.line.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Line() {
    }

    public Line(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public void setUpdate(final Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(final Section section) {
        validateSection(section);

        sections.add(section);
    }

    public void addSection(final Long upStationId, final Long downStationId, final Long distance) {
        addSection(new Section(this, upStationId, downStationId, distance));
    }

    private void validateSection(final Section section) {
        if (sections.isEmpty()) {
            return;
        }

        final Section lastSection = getLastSection();
        if (!Objects.equals(section.getUpStationId(), lastSection.getDownStationId())) {
            throw new IllegalArgumentException(
                    "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다."
            );
        }

        final List<Long> stationIds = getStationIds();
        for (final Long stationId : stationIds) {
            if (Objects.equals(section.getDownStationId(), stationId)) {
                throw new IllegalArgumentException("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.");
            }
        }
    }

    public List<Long> getStationIds() {
        final List<Long> stationIds = new ArrayList<>();
        for (final Section section : sections) {
            if (sections.size() == 1 || isNotLastSection(section)) {
                stationIds.add(section.getUpStationId());
            }
            stationIds.add(section.getDownStationId());
        }
        return stationIds;
    }

    private boolean isNotLastSection(final Section section) {
        return section != getLastSection();
    }

    public void deleteStation(final Long stationId) {
        validateStationDeletion(stationId);

        sections.remove(getLastSectionIndex());
    }

    private void validateStationDeletion(final Long stationId) {
        if (sections.size() == 1) {
            throw new IllegalArgumentException(
                    "지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다."
            );
        }

        final Section lastSection = getLastSection();
        if (!Objects.equals(stationId, lastSection.getDownStationId())) {
            throw new IllegalArgumentException(
                    "지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다."
            );
        }
    }

    private Section getLastSection() {
        return sections.get(getLastSectionIndex());
    }

    private int getLastSectionIndex() {
        return sections.size() - 1;
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
}
