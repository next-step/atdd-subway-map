package subway.jpa;

import javax.persistence.*;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Column(name = "up_station_id", nullable = false)
    private Long upStationId;

    @Column(name = "down_station_id", nullable = false)
    private Long downStationId;

    @Column(nullable = false)
    private int distance;
}
