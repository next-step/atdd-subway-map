package subway.infrastructor.repository;

import javax.persistence.*;

@Entity
@Table(name = "lines")
class LineJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "up_station_id"))
    private StationPk upStationId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "down_station_id"))
    private StationPk downStationId;

    @Column
    private Long distance;

    protected LineJpaEntity() {
    }

    protected LineJpaEntity(Long id, String name, String color, StationPk upStationId, StationPk downStationId, Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
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

    public StationPk getUpStationId() {
        return upStationId;
    }

    public StationPk getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

}
