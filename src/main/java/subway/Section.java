package subway;

import javax.persistence.*;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Long upStationId;

    @Column(nullable = false)
    Long downStationId;

    @Column(nullable = false)
    Long distance;

    @Column(nullable = false)
    Long lineId;
}
