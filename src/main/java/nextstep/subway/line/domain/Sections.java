package nextstep.subway.line.domain;

import nextstep.subway.line.domain.exception.InvalidStationException;
import nextstep.subway.line.domain.exception.UnableToDeleteException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.*;

@Embeddable
public class Sections {
    
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        if(getStationList().size()==0){
            sections.add(section);
            return;
        }
        if(getStationList().get(getStationList().size()-1)!=section.getUpStation()){
            throw new InvalidStationException("추가되는 구간의 상행역이 이 노선의 종점역이 아닙니다.");
        }
        if(getStationList().contains(section.getDownStation())){
            throw new InvalidStationException("추가되는 구간의 하행역이 이 노선에 있는 역입니다.");
        }
        sections.add(section);
    }

    public void delete(Station station){
        if(sections.size()==1){
            throw new UnableToDeleteException("구간이 1개 남아 역을 삭제할 수 없습니다.");
        }
        if(station!=getStationList().get(getStationList().size()-1)){
            throw new UnableToDeleteException("마지막 역이 아니라 삭제할 수 없습니다.");
        }
        sections.remove(sections.size()-1);
    }

    public List<Station> getStationList() {
        List<Station> stationList = new ArrayList<>();
        findFirstSection().ifPresent(section -> {
            stationList.add(section.getUpStation());
            while (true){
                stationList.add(section.getDownStation());
                Optional<Section> optNextSection = findNextSection(section);
                if(!optNextSection.isPresent()){
                    break;
                }
                section = optNextSection.get();
            }
        });
        return stationList;
    }

    private Optional<Section> findFirstSection() {
        Set<Station> set = new HashSet<>();
        sections.stream().forEach(section -> {
            set.add(section.getDownStation());
        });
        return sections.stream()
                .filter(section -> set.add(section.getUpStation())
                ).findFirst();
    }

    private Optional<Section> findNextSection(Section prevSection) {
        return sections.stream()
                .filter(section -> prevSection.getDownStation() == section.getUpStation())
                .findFirst();
    }
}
