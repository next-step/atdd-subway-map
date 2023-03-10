package subway.domain;

import com.sun.source.tree.LineMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.dto.LineRequest;
import subway.mapper.LineMapper;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @OneToOne
    private Station upStation;

    @OneToOne
    private Station downStation;

    public void modify(String name, String color){
        this.name = name;
        this.color = color;
    }
}
