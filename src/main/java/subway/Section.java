package subway;

import javax.persistence.*;

@Entity
@Table(name = "section")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "line_id")
    private Line line;

    @Column(name = "downStationId")
    private Long downStationId;

    @Column(name = "upStationId")
    private Long upStationId;

    @Column(name = "distance")
    private int distance;

    public Section() {

    }

    public Section(Line line, Long downStationId, Long upStationId, int distance) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
        this.line = line;
    }

    public long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
