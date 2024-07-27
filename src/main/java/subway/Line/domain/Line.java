package subway.Line.domain;

import static subway.global.exception.ExceptionCode.INVALID_DELETE_DOWNSTATION;
import static subway.global.exception.ExceptionCode.INVALID_DOWNSTATION_NOT_NEW_EQUAL_DOWNSTATION;
import static subway.global.exception.ExceptionCode.INVALID_DOWNSTATION_TO_BE_NEW_UPSTATION;
import static subway.global.exception.ExceptionCode.INVALID_SECTION_MIN;
import static subway.global.exception.ExceptionCode.NOT_FOUND_STATION;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import subway.global.exception.BadRequestException;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    @OneToMany(
            mappedBy = "line",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Section> sections = new ArrayList<>();

    public Line() {}

    public Line(String name, String color, Long upStationId, Long downStationId, int distance) {
        this(null, name, color, upStationId, downStationId, distance);
    }

    public Line(Long id, String name, String color, Long upStationId, Long downStationId, int distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.addSection(upStationId, downStationId, distance);
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

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public List<Long> getStationsIds() {
        return List.of(upStationId, downStationId);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateColor(String color) {
        this.color = color;
    }

    public void addSection(Long upStationId, Long downStationId, int distance) {
        if (!this.sections.isEmpty() &&
                !this.downStationId.equals(upStationId)) {
            throw new BadRequestException(INVALID_DOWNSTATION_TO_BE_NEW_UPSTATION);
        }

        if (!this.sections.isEmpty() &&
                this.downStationId.equals(downStationId)) {
            throw new BadRequestException(INVALID_DOWNSTATION_NOT_NEW_EQUAL_DOWNSTATION);
        }
        this.sections.add(new Section(upStationId, downStationId, distance, this));
        this.distance = calculateDistance();
        this.downStationId = downStationId;
    }

    public void deleteSection(Long stationId) {
        Section deleteSection = sections.stream().filter(section -> section.isDownStationId(stationId)).findAny()
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_STATION));

        if (!this.downStationId.equals(deleteSection.getDownStationId())) {
            throw new BadRequestException(INVALID_DELETE_DOWNSTATION);
        }

        if (this.sections.size() < 2) {
            throw new BadRequestException(INVALID_SECTION_MIN);
        }

        this.downStationId = deleteSection.getUpStationId();
        deleteSection.remove();
        this.sections.remove(deleteSection);
    }

    private int calculateDistance() {
        return this.sections.stream()
                .mapToInt(Section::getDistance).sum();
    }
}
