package nextstep.subway.domain;

import lombok.Getter;
import nextstep.subway.applicaion.exceptions.DataNotFoundException;
import nextstep.subway.applicaion.exceptions.StationDuplicateException;
import nextstep.subway.enums.exception.ErrorCode;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;


@Getter
@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line",orphanRemoval = true, cascade = CascadeType.ALL)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(Line line, Station upStation, Station downStation, Integer distance) {
        Section section = new Section(line, upStation, downStation, distance);
        sections.add(section);
    }

    public void addSection(Section section) {
        validContainAlreadyReqDownStation(section.getDownStation());
        sections.add(section);
    }

    public void deleteSection(Long downStationId) {
        validContainReqDownStation(downStationId);
        sections.remove(getLastSection());
    }

    public List<Station> getStations() {
        return sections.stream()
                       .map(Section::getRelatedStations)
                       .flatMap(Collection::stream).distinct()
                       .collect(Collectors.toList());
    }

    public Station getLastStation() {
        if (sections.isEmpty())
            throw new DataNotFoundException(ErrorCode.NOT_FOUND_SECTION);

        return sections.get(sections.size() - 1).getDownStation();
    }

    public Section getLastSection(){
        if(sections.size() < 1){
            throw new NoSuchElementException("저장 된 section정보가 없습니다.");
        }

        return sections.get(sections.size() - 1);
    }

    private void validContainAlreadyReqDownStation(Station downStation) {
        boolean isAlreadyRegisterStation = getStations().stream()
                                                        .anyMatch(station -> station.getName().equals(downStation.getName()));

        if (isAlreadyRegisterStation)
            throw new StationDuplicateException(ErrorCode.ALREADY_REGISTER_STATION);

   }

   private void validContainReqDownStation(Long downStationId) {
       if (!Objects.equals(getLastStation().getId(), downStationId))
           throw new  DataNotFoundException(ErrorCode.NOT_FOUND_SECTION);
   }
}
