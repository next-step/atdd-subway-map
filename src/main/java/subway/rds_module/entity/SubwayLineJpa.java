package subway.rds_module.entity;

import subway.subway.domain.Station;
import subway.subway.domain.SubwayLine;
import subway.subway.domain.SubwaySection;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity
@Table(name = "subway_lines")
public class SubwayLineJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subway_line_id")
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String color;

    @Column(nullable = false)
    private Long startSectionId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="subway_line_id")
    private List<SubwaySectionJpa> subwaySections = new ArrayList<>();

    public SubwayLineJpa() {
    }

    public SubwayLineJpa(Long id, String name, String color, Long startSectionId, List<SubwaySectionJpa> subwaySections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.startSectionId = startSectionId;
        this.subwaySections = subwaySections;
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

    public Long getStartSectionId() {
        return startSectionId;
    }

    public List<SubwaySectionJpa> getSubwaySections() {
        return subwaySections;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public boolean isNew() {
        return id == null;
    }

    public void updateSections(SubwayLine subwayLine) {
        Map<Long, SubwaySectionJpa> subwaySectionMap = subwaySections.stream().collect(Collectors.toMap(SubwaySectionJpa::getId, Function.identity()));

        for (SubwaySection section : subwayLine.getSections()) {
            if (section.isNew()) {
                subwaySections.add(
                        new SubwaySectionJpa(
                                section.getUpStationId().getValue(),
                                section.getUpStationName(),
                                section.getDownStationId().getValue(),
                                section.getDownStationName(),
                                section.getDistance().getValue()));
            } else {
                SubwaySectionJpa subwaySectionJpa = subwaySectionMap.get(section.getId().getValue());
                subwaySectionJpa.update(
                        section.getUpStationId().getValue(),
                        section.getUpStationName(),
                        section.getDownStationId().getValue(),
                        section.getDownStationName(),
                        section.getDistance().getValue());
            }
        }
    }
}
