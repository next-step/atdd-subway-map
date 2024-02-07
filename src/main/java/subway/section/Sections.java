package subway.section;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sectionList = new ArrayList<>();

    public void addSection(Section section) {
        validSection(section);
        sectionList.add(section);
    }

    private void validSection(Section section) {
        if(sectionList.isEmpty()) {
            return;
        }
        if(!Objects.equals(section.getUpStationId(), getLastDownStationId())) {
            throw new IllegalArgumentException("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다");
        }
        //이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없다.
        if(isDupleCate(section)) {
            throw new IllegalArgumentException("이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없습니다");
        }
    }

    public Long getFirstUpStationId() {
        if(sectionList.isEmpty()) {
            throw new IllegalArgumentException("section is Empty");
        }
        return getUpStationId(0);
    }

    public Long getLastDownStationId() {
        if (sectionList.isEmpty()) {
            throw new IllegalArgumentException("section is Empty");
        }
        return getDownStationId(getLastIndex());
    }

    private Long getUpStationId(int index) {
        return sectionList.get(index).getUpStationId();
    }

    private Long getDownStationId(int index) {
        return sectionList.get(index).getDownStationId();
    }

    public boolean isDupleCate(Section section) {
        return sectionList.stream()
            .anyMatch(section::isSame);
    }

    public void removeLastSection(Long stationId) {
        if(sectionList.isEmpty()) {
            throw new IllegalArgumentException("section is Empty");
        }

        Section section = sectionList.get(getLastIndex());
        if(!isRemovable(stationId)) {
            throw new IllegalStateException("해당 역을 삭제할 수 없습니다.");
        }

        sectionList.remove(getLastIndex());
    }

    private int getLastIndex() {
        return sectionList.size() - 1;
    }

    private boolean isRemovable(Long stationId) {
        return sectionList.size() > 1 && getLastDownStationId().equals(stationId);
    }
}
