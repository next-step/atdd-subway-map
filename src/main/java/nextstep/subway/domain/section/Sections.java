package nextstep.subway.domain.section;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.station.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sectionList;

    public Sections() {
        this.sectionList = new ArrayList<>();
    }

    public List<Station> getAllStations(Station upStation) {
        List<Section> tmpSectionList = new ArrayList<>(sectionList);
        List<Station> stations = new ArrayList<>();

        while (!tmpSectionList.isEmpty()) {
            for (Section tmpSection : tmpSectionList) {
                if (tmpSection.isUpStation(upStation)) {
                    stations.add(upStation);
                    upStation = tmpSection.getDownStation();
                    tmpSectionList.remove(tmpSection);
                    break;
                }
            }
        }

        stations.add(upStation);
        return stations;
    }

    public boolean hasStation(Station downStation) {
        return sectionList.stream().anyMatch(section -> section.hasStation(downStation));
    }

    public List<Section> getSectionList() {
        return sectionList;
    }

    public Section delete(Section section) {
        sectionList.remove(section);
        return section;
    }

    public void add(Line line, Section section) {
        int size = sectionList.size();

        // 기존 상행과 새로운 구간의 상행이 같으면서 하행은 서로 다른경우
        // 새로운 구간이 추가되고
        // 기존 구간이 삭제되며
        // 새로운 하행을 상행으로, 기존의 하행을 하행으로 갖는 구간이 쪼개져서 추가된다.
        sectionList.stream()
                .filter(oriSection -> oriSection.isUpStation(section.getUpStation())
                        && !oriSection.isDownStation(section.getDownStation()))
                .findFirst()
                .ifPresent(oriSection -> {
                    push(line, Section.of(section.getDownStation(), oriSection.getDownStation(),
                            oriSection.getDistance() - section.getDistance()));
                    sectionList.remove(oriSection);

                });

        // 기존 하행과 새로운 구간의 하행이 같으면서 상행은 서로 다른경우
        // 기존 상행을 상행으로, 새로운 상행을 하행으로 갖는 구간이 쪼개져서 추가되고
        // 기존 구간이 삭제되며
        // 새로운 구간이 추가된다.
        sectionList.stream()
                .filter(oriSection -> oriSection.isDownStation(section.getDownStation())
                        && !oriSection.isUpStation(section.getUpStation()))
                .findFirst()
                .ifPresent(oriSection -> {
                    push(line, Section.of(oriSection.getUpStation(), section.getUpStation(),
                            oriSection.getDistance() - section.getDistance()));
                    sectionList.remove(oriSection);

                });

        push(line, section);

    }

    private void push(Line line, Section section) {
        sectionList.add(section);
        section.setLine(line);
    }

    public boolean hasOneSection() {
        return sectionList.size() == 1;
    }
}
