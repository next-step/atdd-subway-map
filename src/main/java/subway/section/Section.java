package subway.section;

import subway.line.Line;

import javax.persistence.*;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long upStationId;

    @Column(nullable = false)
    private Long downStationId;

    @Column(nullable = false)
    private Long distance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;

    @Column(name = "section_order")
    private Integer sectionOrder;

    public Section() {
    }

    public Section(Long upStationId, Long downStationId, Long distance, Line line) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.line = line;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long upStationId;
        private Long downStationId;
        private Long distance;
        private Line line;

        public Builder() {
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

        public Builder line(Line line) {
            this.line = line;
            return this;
        }

        public Section build() {
            return new Section(this.upStationId, this.downStationId, this.distance, this.line);
        }


    }

    public Long getId() {
        return id;
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

    public Line getLine() {
        return line;
    }

    public Integer getSectionOrder() {
        return sectionOrder;
    }
}
