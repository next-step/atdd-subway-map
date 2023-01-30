package subway;

import javax.persistence.*;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "upStationId")
    private Long upStationId;

    @Column(name = "downStationId")
    private Long downStationId;
}
