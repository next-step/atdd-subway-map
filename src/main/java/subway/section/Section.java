package subway.section;

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

    @Column(nullable = false)
    private Long lineId;

    public Section() {
    }

    public Section(Long upStationId, Long downStationId, Long distance, Long lineId) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.lineId = lineId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long upStationId;
        private Long downStationId;
        private Long distance;
        private Long lineId;

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

        public Builder lineId(Long lineId) {
            this.lineId = lineId;
            return this;
        }

        public Section build() {
            return new Section(this.upStationId, this.downStationId, this.distance, this.lineId);
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

    public Long getLineId() {
        return lineId;
    }
}
