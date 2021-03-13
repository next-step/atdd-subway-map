package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(List<Section> sections) {
        sections = sections;
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    /**
     * 다음역
     */
    public Station getNextStation(Long stationId){
        Station nextStation = null;
        if(sections.size() != 0){
            Section section = sections.stream()
                    .filter(it -> it.getUpStation().getId().equals(stationId))
                    .findFirst()
                    .orElse(new Section()); // TODO 널이면 뭐 리턴하지...
            nextStation = section.getDownStation();
        }
        return nextStation;
    }

    /**
     * 해당 역이 존재하는가?
     */
    public boolean isContainsStation(Long stationId){
        boolean isUpStation = sections.stream()
                .anyMatch(it -> it.getUpStation().getId().equals(stationId));
        boolean isDownStation = sections.stream()
                .anyMatch(it -> it.getDownStation().getId().equals(stationId));
        return isUpStation || isDownStation;
    }

    /**
     * 구간 제거
     */
    public void removeLastSection(Long stationId){
        // 벨리데이션
        validateRemoveLastSection(stationId);
        // 삭제
        int idx = getSectionSize() - 1;
        if(idx > 0){
            this.sections.remove(idx);
        }
    }

    /**
     * 구간 제거 - 벨리데이션
     */
    private void validateRemoveLastSection(Long stationId){
        if(getLastStationId() != stationId) {
            throw new IllegalArgumentException("마지막 역(하행 종점역)만 제거할 수 있다.");
        }
        if(getSectionSize() == 1) {
            throw new IllegalArgumentException("구간이 1개인 경우 역을 삭제할 수 없다.");
        }
    }

    public int getSectionSize(){
        return sections == null ? 0 : sections.size();
    }

    public Station getLastStation(){
        return sections.size() == 0 ? null : sections.get(sections.size() -1).getDownStation();
    }
    public Station getFirstStation() { return sections.size() == 0 ? null : sections.get(0).getUpStation(); }

    public Long getLastStationId(){
        return sections.size() == 0 ? null : sections.get(sections.size() -1).getDownStation().getId();
    }
    public Long getFirstStationId() { return sections.size() == 0 ? null : sections.get(0).getUpStation().getId(); }
}

