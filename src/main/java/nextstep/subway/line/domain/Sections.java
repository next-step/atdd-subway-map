package nextstep.subway.line.domain;

import nextstep.subway.exception.NoOtherStationException;
import nextstep.subway.exception.NotEqualsNameException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public Sections() {

    }

    public List<Section> getSections() {
        return sections;
    }

    public int size() {
        return sections.size();
    }

    public Section getFinishSection() {
        return sections.get(size() - 1);
    }

    public Station getDownStation() {
        return sections.get(size() -1).getDownStation();
    }

    private void addSectionValidate(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        if (!upStation.equals(downStation)) {
            throw new NotEqualsNameException();
        }
    }

    private boolean isNotOtherStation() {
        return size() == 1;
    }

    private boolean isTarget(long stationId) {
        return Objects.equals(getDownStation().getId(), stationId);
    }

    private void isValidDeleteSection(long stationId) {
        if (!isTarget(stationId)) {
            throw new IllegalArgumentException("종점역만 삭제가 가능합니다.");
        }

        if (isNotOtherStation()) {
            throw new NoOtherStationException();
        }
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void deleteLastSection(Long stationId) {
        isValidDeleteSection(stationId);
        sections.remove(getFinishSection());
    }

    public List<StationResponse> getAllStation() {
        List<StationResponse> responses = new ArrayList<>();
        responses.add(StationResponse.of(sections.get(0).getUpStation()));

        sections.forEach(section -> responses.add(StationResponse.of(section.getDownStation())));
        return responses;
    }

}
