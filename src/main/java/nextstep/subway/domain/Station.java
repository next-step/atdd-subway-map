package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.Objects;

@Entity
public class Station extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Station() {
    }

    public Station(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(getName(), station.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    public boolean isSameStation(Long stationId) {
        return Objects.equals(id, stationId);
    }

    public void notEqualsIn(List<Long> stationIdsInLine) {
        boolean isEqual = stationIdsInLine.stream()
                .anyMatch(id -> Objects.equals(id, this.id));
        if (isEqual){
            throw new IllegalArgumentException("이미 노선에 존재하는 역 입니다. 추가 하실 수 없습니다.");
        }
    }
}
