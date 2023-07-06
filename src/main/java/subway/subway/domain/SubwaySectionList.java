package subway.subway.domain;

import java.util.Arrays;
import java.util.List;

public class SubwaySectionList {

    private final List<SubwaySection> subwaySections;

    public SubwaySectionList(SubwaySection... subwaySections) {
        this.subwaySections = Arrays.asList(subwaySections);
    }

    public SubwaySectionList(List<SubwaySection> subwaySectionList) {
        this.subwaySections = subwaySectionList;
    }

    public void add(SubwaySection station) {
        subwaySections.add(station);
    }

    public List<SubwaySection> getSections() {
        return subwaySections;
    }
}
