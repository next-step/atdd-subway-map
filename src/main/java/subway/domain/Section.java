package subway.domain;

import subway.common.BaseEntity;

import javax.persistence.*;

@Entity
public class Section extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Column(nullable = false)
    @ManyToOne(targetEntity = Station.class, fetch = FetchType.LAZY)
    @JoinColumn(name="up_station_id")
    private Long upStationId;

    @Column(nullable = false)
    @ManyToOne(targetEntity = Station.class, fetch = FetchType.LAZY)
    @JoinColumn(name="down_station_id")
    private Long downStationId;

    @Column(nullable = false)
    private Long distance;

}
