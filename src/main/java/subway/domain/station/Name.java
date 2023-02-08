package subway.domain.station;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor
@Embeddable
public class Name {
    @Column(length = 20, nullable = false)
    private String name;

    public Name(String name) {
        if (name.length() == 0 || name.length() > 20) {
            throw new IllegalArgumentException("지하철 역 이름의 길이는 1에서 20글자만 허용됩니다.");
        }
        this.name = name;
    }

}
