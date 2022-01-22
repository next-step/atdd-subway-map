package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.DynamicUpdate;

@Entity
// 쿼리 캐싱이 안되는건 어쩔 수 없지만, 전체를 업데이트하다가 사이드 이펙트가 날 수 있기 때문에 미리 DynamicUpdate를 적용한다.
@DynamicUpdate
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;

    public Line() {}

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void changeLineInformation(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
