package subway;

import javax.persistence.*;

@Entity
public class LineStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Station station;

    public Long getId() {
        return id;
    }


}
