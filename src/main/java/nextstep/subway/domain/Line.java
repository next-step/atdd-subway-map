package nextstep.subway.domain;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String color;

    @Convert(converter = SectionConverter.class)
    private List<Section> sections = new ArrayList<>();

    protected Line() {
    }

    public Line(final String name, final String color, final Long upStationId, final Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.addSection(upStationId, downStationId, distance);
    }

    public void update(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Long upStationId, Long downStationId, int distance) {
        if (upStationId.equals(downStationId)) {
            throw new IllegalArgumentException();
        }

        Section section = new Section(upStationId, downStationId, distance);
        if (!sections.isEmpty()) {
            Section lastSection = this.sections.get(sections.size() - 1);

            if (!lastSection.getDownStationId().equals(upStationId)) {
                throw new IllegalArgumentException();
            }

            List<Long> upStationIds = this.sections.stream().map(Section::getUpStationId).collect(Collectors.toList());
            if (upStationIds.contains(downStationId)) {
                throw new IllegalArgumentException();
            }
        }

        this.sections.add(section);
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

    public List<Long> getAllStations() {
        List<Long> upStationIds = sections.stream().map(Section::getUpStationId).collect(Collectors.toList());
        Long lastDownStationId = sections.get(sections.size() - 1).getDownStationId();

        upStationIds.add(lastDownStationId);

        return upStationIds;
    }

    public void deleteSection(final Long stationId) {
        if (this.sections.size() == 1) {
            throw new IllegalArgumentException();
        }

        if (this.sections.get(sections.size() - 1).getDownStationId().equals(stationId)) {
            this.sections.remove(sections.size() - 1);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
