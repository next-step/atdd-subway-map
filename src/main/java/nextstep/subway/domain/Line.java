package nextstep.subway.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(Long id, String name, String color, List<Section> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
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

    public List<Section> getSections() {
        return sections;
    }

    public void updateLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        this.sections.add(new Section(this, upStation, downStation, distance));
    }

    public void deleteLastSection() {
        this.sections.remove(getLastSection());
    }

    public Section getLastSection() {
        final int lastIndex = this.sections.size() - 1;
        return this.sections.get(lastIndex);
    }

    public void validateStationInSection(Station downStation, Station upStation) {
        if (!this.sections.isEmpty()) {
            checkUpStation(upStation);
            checkDownStation(downStation);
        }
    }

    private void checkUpStation(Station upStation) {
        final Section lastSection = getLastSection();
        if (!upStation.equals(lastSection.getDownStation())) {
            throw new IllegalArgumentException("등록할 상행종점역은 노선의 하행종점역이어야 합니다.");
        }
    }

    private void checkDownStation(Station downStation) {
        final Section lastSection = getLastSection();
        if (lastSection.getDownStation().equals(downStation) ||
                lastSection.getUpStation().equals(downStation)) {
//        if (this.sections.) // todo section 목록에서 station 목록 전체 가져오기
            throw new IllegalArgumentException("등록할 하행종점역은 노선에 등록되지 않은 역만 가능합니다.");
        }
    }


    public static class Builder {
        private Long id;
        private String name;
        private String color;
        private List<Section> sections = new ArrayList<>();
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder sections(List<Section> sections) {
            this.sections = sections;
            return this;
        }

        public Builder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder modifiedDate(LocalDateTime modifiedDate) {
            this.modifiedDate = modifiedDate;
            return this;
        }

        public Line build() {
            try {
                return new Line(id, name, color, sections);
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("Cannot create Line");
            }
        }
    }
}
