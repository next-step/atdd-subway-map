package nextstep.subway.section.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long lineId;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    protected Section() {
    }

    public Section(final Long id, final Long lineId, final Long upStationId, final Long downStationId, final Long distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
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

    public static class SectionBuilder {
        private Long lineId;
        private Long upStationId;
        private Long downStationId;
        private Long distance;

        public SectionBuilder lineId(final Long lineId) {
            this.lineId = lineId;
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
            return new Section(null, lineId, upStationId, downStationId, distance);
        }

    }

    public static SectionBuilder builder() {
        return new SectionBuilder();
    }

}
