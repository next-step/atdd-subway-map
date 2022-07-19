package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.common.exception.CustomException;
import nextstep.subway.common.exception.ResponseCode;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Sections {
    public static final int FIRST_STATION_IDX = 0;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    public void add(final Section section){
        if(!sections.isEmpty()){
            checkSectionMatch(section);
            checkStationDuplicate(section);
        }
        sections.add(section);
    }

    private void checkSectionMatch(final Section section) {
        if(!getDownEndStation().equals(section.getUpStation())){
            throw new CustomException(ResponseCode.SECTION_NOT_MATCH);
        }
    }

    private void checkStationDuplicate(final Section section) {
        Set<Station> stations = new HashSet<>(getStations());
        if(stations.contains(section.getDownStation())){
            throw new CustomException(ResponseCode.LINE_STATION_DUPLICATE);
        }
    }

    public List<Station> getStations(){
        return sections.stream()
            .map(Section::getAllStation)
            .flatMap(List::stream)
            .distinct()
            .collect(Collectors.toUnmodifiableList());
    }

    public Station getUpEndStation(){
        return sections.get(FIRST_STATION_IDX).getUpStation();
    }

    public Station getDownEndStation(){
        return sections.get(sections.size() - 1).getDownStation();
    }
}
