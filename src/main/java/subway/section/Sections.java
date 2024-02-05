package subway.section;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sectionList = new ArrayList<>();


    public void addSection(Section section) {
        validSection(section);
        sectionList.add(section);
    }

    private void validSection(Section section) {
        if(sectionList.size() < 1) {
            return;
        }
        if(!Objects.equals(section.getUpStationId(), getLastDownStationId())) {
            throw new IllegalArgumentException("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다");
        }
        //이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없다.
        if(isdupleCate(section)) {
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
        return getDownStationId(sectionList.size() - 1);
    }

    private Long getUpStationId(int index) {
        return sectionList.get(index).getUpStationId();
    }

    private Long getDownStationId(int index) {
        return sectionList.get(index).getDownStationId();
    }

    public boolean isdupleCate(Section section) {
        return sectionList.stream()
            .anyMatch(section::isSame);
    }

}
