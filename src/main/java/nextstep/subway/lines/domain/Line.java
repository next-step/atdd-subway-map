package nextstep.subway.lines.domain;

import nextstep.subway.stations.domain.Station;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "line", uniqueConstraints = {
        @UniqueConstraint(name = "line_name_unique", columnNames = {"name"})
})
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;

    private String name;

    private String color;

    @Embedded
    private Sections sections;

    protected Line() {

    }

    private Line(final String name, final String color, final Station upStation, final Station downStation, int distance) {
        this.name = name.trim();
        this.color = color.trim();
        this.sections = new Sections();
        this.sections.addSection(upStation, downStation, distance);
    }

    public static Line makeLine(final String name, final String color, final Station upStation, final Station downStation, final int distance) {
        validateName(name);
        validateColor(color);
        return new Line(name, color, upStation, downStation, distance);
    }

    private static void validateName(final String name) {
        if(!StringUtils.hasLength(name.trim())) {
            throw new IllegalArgumentException("지하철 노선명은 null 이거나 빈문자열이 들어올 수 없습니다. [문자열 사이에 공백불가]");
        }
    }

    private static void validateColor(final String color) {
        if(!StringUtils.hasLength(color.trim())) {
            throw new IllegalArgumentException("지하철 노선색은 null 이거나 빈문자열이 들어올 수 없습니다. [문자열 사이에 공백불가]");
        }
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }

    public Sections getSections() {
        return this.sections;
    }

    public void changeName(final String name) {
        if(Objects.nonNull(name)) {
            validateName(name);
            this.name = name.trim();
        }
    }

    public void changeColor(final String color) {
        if(Objects.nonNull(color)) {
            validateColor(color);
            this.color = color.trim();
        }
    }

    private boolean isSectionsEmpty() {
        return this.sections.isEmpty();
    }

    private void validateSectionsEmpty() {
        if(isSectionsEmpty()) {
            throw new IllegalStateException("");
        }
    }

    private Section findUpSection() {

        // 구간이 비어있을경우 예외처리
        validateSectionsEmpty();
        Section upSection = this.sections.findFirst();

        while(upSection != null) {
            boolean isFind = false;
            for(Section section : sections) {
                // 현재구간의 상행역과 다른구간의 하행역이 같은지 체크
                if(upSection.correctUpStationFromOtherSectionDownStation(section)) {
                    upSection = section;
                    isFind = true;
                    break;
                }
            }

            if(!isFind) {
                break;
            }
        }
        return upSection;
    }
    private Section findDownSection() {

        // 구간이 비어있을경우 예외처리
        validateSectionsEmpty();
        Section downSection = this.sections.findFirst();

        while(downSection != null) {
            boolean isFind = false;
            for(Section section : sections) {
                //현재 구간의 하행역과 다른구간의 상행역이 같은지 체크
                if(downSection.correctDownStationFromOtherSectionUpStation(section)) {
                    downSection = section;
                    isFind = true;
                    break;
                }
            }

            if(!isFind) {
                break;
            }
        }
        return downSection;
    }

    public void addSection(Station upStation, Station downStation, int distance) {

        Section upSection = findUpSection();
        Section downSection = findDownSection();

        if(upSection.isUpStation(downStation) || downSection.isDownStation(upStation)) {
            this.sections.addSection(upStation, downStation, distance);
            return;
        }

        throw new IllegalStateException("");
    }

    /**
     * 구간 제거
     * @param station
     */
    public void removeSection(Station station) {

        Section downSection = findDownSection();

        if(downSection.isDownStation(station)) {
           this.sections.removeSection(downSection);
        }
    }
}
