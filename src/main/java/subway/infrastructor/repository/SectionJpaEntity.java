package subway.infrastructor.repository;

import javax.persistence.*;

@Entity
@Table(name = "sections")
public class SectionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "up_station_id"))
    private StationPk upStationId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "down_station_id"))
    private StationPk downStationId;

    @Column
    private Long distance;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "line_id"))
    private LinePk lineId;

    protected SectionJpaEntity() {
    }

    public SectionJpaEntity(Long id, StationPk upStationId, StationPk downStationId, Long distance, LinePk lineId) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.lineId = lineId;
    }

    public SectionJpaEntity(StationPk upStationId, StationPk downStationId, Long distance, LinePk lineId) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.lineId = lineId;
    }

    public StationPk getUpStationId() {
        return upStationId;
    }

    public StationPk getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public LinePk getLineId() {
        return lineId;
    }

    public Long getId() {
        return id;
    }

}
