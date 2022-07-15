package nextstep.subway.section.domain;

import nextstep.subway.line.domain.Line;

import javax.persistence.*;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Line line;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    protected Section() {
    }

    private Section(final Long id, final Line line, final Long upStationId, final Long downStationId, final Long distance) {
        this.id = id;
        this.line = line;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
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

    public boolean isDuplicated(final Section section) {
        return downStationId.equals(section.getUpStationId()) || downStationId.equals(section.getDownStationId());
    }

    public void setLine(final Line line) {
        this.line = line;
    }

    public static class SectionBuilder {
        private Line line;
        private Long upStationId;
        private Long downStationId;
        private Long distance;

        public SectionBuilder line(final Line line) {
            this.line = line;
            return this;
        }

        public SectionBuilder upStationId(final Long upStationId) {
            this.upStationId = upStationId;
            return this;
        }

        public SectionBuilder downStationId(final Long downStationId) {
            this.downStationId = downStationId;
            return this;
        }

        public SectionBuilder distance(final Long distance) {
            this.distance = distance;
            return this;
        }

        public Section build() {
            return new Section(null, line, upStationId, downStationId, distance);
        }

    }

    public static SectionBuilder builder() {
        return new SectionBuilder();
    }

}
