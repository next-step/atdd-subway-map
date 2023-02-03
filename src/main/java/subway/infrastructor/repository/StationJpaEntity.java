package subway.infrastructor.repository;

import javax.persistence.*;

@Entity
@Table(name = "station")
public class StationJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    protected StationJpaEntity() {
    }

    public StationJpaEntity(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
