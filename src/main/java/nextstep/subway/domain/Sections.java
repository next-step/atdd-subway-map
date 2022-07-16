package nextstep.subway.domain;

import nextstep.subway.exception.BusinessException;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Embeddable
public class Sections {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stationLine", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private List<Section> sections = new ArrayList<>();

    public void addSection(Section section) {
        validateAddSection(section);
        this.sections.add(section);
    }

    public List<Section> getSections() {
        return this.sections;
    }

    public List<Station> getStationsIncludedInLine() {
        Set<Station> stations = new LinkedHashSet<>();
        for (Section section : this.sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }
        return new ArrayList<>(stations);
    }

    public Section getLastSection() {
        if (this.sections.isEmpty()) {
            return null;
        }

        return this.sections.get(this.sections.size() - 1);
    }

    private void validateAddSection(Section section) {
        if (this.sections.size() > 0) {
            validateNewSectionUpStation(section);
            validateNewSectionDownStation(section);
        }
    }

    private void validateNewSectionUpStation(Section section) {
        Station upStation = section.getUpStation();
        Station lineDownStation = getLastSection().getDownStation();

        if (!lineDownStation.equals(upStation)) {
            throw new BusinessException("새로운 구간의 상행역이 해당 노선에 등록되어있는 하행 종점역이 아닙니다.", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateNewSectionDownStation(Section section) {
        Station downStation = section.getDownStation();
        List<Station> stationsIncludedInLine = getStationsIncludedInLine();

        if (stationsIncludedInLine.contains(downStation)) {
            throw new BusinessException("새로운 구간의 하행역이 해당 노선에 이미 등록되어있습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public void validateDeleteSectionCondition(Station deleteStation) {
        Station targetStation = getLastSection().getDownStation();
        if (!targetStation
                .equals(deleteStation)) {
            throw new BusinessException("삭제하려는 역이 하행 종점역이 아닙니다.", HttpStatus.BAD_REQUEST);
        }

        if (sections.size() == 1) {
            throw new BusinessException("지하철 노선에 상행 종점역과 하행 종점역만 존재합니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
