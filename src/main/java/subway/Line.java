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

    public Line() {

    }

    public Line(String name, Long upStationId, Long downStationId) {
        this.name = name;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }

    public long getId() {
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
}
