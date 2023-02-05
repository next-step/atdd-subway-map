package subway.domain.line;

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
        if (name == null) {
            throw new IllegalArgumentException("노선 이름이 존재하지 않습니다.");
        }
        if (name.length() == 0 || name.length() > 20) {
            throw new IllegalArgumentException("노선 이름 길이는 1이상 20이하 이어야 합니다.");
        }
        this.name = name;
    }

}
