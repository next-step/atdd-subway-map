package subway.infrastructor.repository;

import javax.persistence.*;

@Entity
public class StationJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    public StationJpaEntity() {
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
