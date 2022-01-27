package nextstep.subway.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Sections {
    private List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    private Section getFirstSection() {
        return sections.stream()
                .filter(us ->
                        sections.stream().noneMatch(ds ->
                                ds.getDownStation().equals(us.getUpStation())))
                .collect(Collectors.toList())
                .get(0);
    }

    public List<Section> sorted() {
        Section firstSection = getFirstSection();
        sections.remove(firstSection);
        sections.add(0, firstSection);

        for (int i = 0; i < sections.size() - 1; i++) {
            Station downStation = sections.get(i).getDownStation();
            for (int j = i + 1; j < sections.size(); j++) {
                Station upStation = sections.get(j).getUpStation();
                if (downStation.equals(upStation)) {
                    Collections.swap(sections, i + 1, j);
                    break;
                }
            }
        }

        return sections;
    }

    public List<Section> getSections() {
        return sections;
    }
}
