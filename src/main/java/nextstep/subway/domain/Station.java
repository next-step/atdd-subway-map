package nextstep.subway.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "지하철역 이름은 필수 값 입니다.")
    @Length(min = 1, max = 255, message = "지하철 이름의 길이를 확인해주세요")
    private String name;

    protected Station() {
    }

    @Builder
    public Station(String name) {
        this.name = name;
    }
}
