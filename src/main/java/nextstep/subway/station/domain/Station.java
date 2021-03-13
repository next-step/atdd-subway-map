package nextstep.subway.station.domain;

import nextstep.subway.common.BaseEntity;

import javax.persistence.*;

@Entity
public class Station extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    // 기본생성자
    protected Station() {
    }

    // 생성메서드
    public static Station createStation(String name){
        Station station = new Station();
        station.setName(name);
        return station;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private void setId(Long id) {
        this.id = id;
    }

    private void setName(String name) {
        this.name = name;
    }
}
