package subway.line;

import subway.commons.ErrorCode;
import subway.commons.HttpException;
import subway.section.Section;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Column(nullable = false)
    private Long upStationId;

    @Column(nullable = false)
    private Long downStationId;

    @Column(nullable = false)
    private Long distance;

    @OneToMany(mappedBy = "line",fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.PERSIST)
    @OrderColumn(name = "section_order")
    private List<Section>  sections = new ArrayList<>();

    public Line() {
    }

    public Line(Builder builder) {
        this.name = builder.name;
        this.color = builder.color;
        this.upStationId = builder.upStationId;
        this.downStationId = builder.downStationId;
        this.distance = builder.distance;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String color;
        private Long upStationId;
        private Long downStationId;
        private Long distance;



        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder upStationId(Long upStationId) {
            this.upStationId = upStationId;
            return this;
        }

        public Builder downStationId(Long downStationId) {
            this.downStationId = downStationId;
            return this;
        }

        public Builder distance(Long distance) {
            this.distance = distance;
            return this;
        }

        public Line build() {
            return new Line(this);
        }

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public String getColor() {
        return color;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeColor(String color) {
        this.color = color;
    }

    public void addSection(Section section) {
        validateCreateSection(section);
        sections.add(section);
    }

    public void deleteSection(Long stationId) {
        validateDeleteSection(stationId);
        sections.remove(sections.size() - 1);
    }

    private void validateCreateSection(Section creatingSection) {
        // 하행 종점이 같은 것이 존재하면 x
        boolean isExist = sections.stream().anyMatch(section -> section.getDownStationId() == creatingSection.getDownStationId());
        if(isExist) {
            throw new HttpException(ErrorCode.DOWN_STATION_NOT_VALID);
        }
        // 상행 종점이 같은 것이 존재하면 x
        isExist = sections.stream().anyMatch(section -> section.getUpStationId() == creatingSection.getUpStationId());
        if(isExist) {
            throw new HttpException(ErrorCode.UP_STATION_NOT_VALID);
        }
        // 등록하는 상행역이 기존 하행종점과 같지 않으면 x
        Section lastSection = sections.get(sections.size() - 1);
        if(lastSection.getDownStationId() != creatingSection.getUpStationId()) {
            throw new HttpException(ErrorCode.UP_STATION_NOT_VALID);
        }
    }

    private void validateDeleteSection(Long stationId) {
        // 마지막 구간 제거 금지
        if(sections.size() ==1) {
            throw new HttpException(ErrorCode.CANNOT_REMOVE_LAST_SECTION);
        }
        // 제거하는 역이 하행종점이 아니면 x
        Section lastSection = sections.get(sections.size() - 1);
        if(lastSection.getDownStationId() != stationId) {
            throw new HttpException(ErrorCode.IS_NOT_TERMINAL_STATION);
        }

    }
}
