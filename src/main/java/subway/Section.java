package subway;

import javax.persistence.*;

@Entity
@Table(name = "section")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "downStationId")
    private Long downStationId;

    @Column(name = "upStationId")
    private Long upStationId;

    @Column(name = "distance")
    private int distance;

}
