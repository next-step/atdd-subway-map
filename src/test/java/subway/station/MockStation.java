package subway.station;

import subway.common.Mock;

public enum MockStation implements Mock {
    강남역("강남역"),
    신촌역("신촌역"),
    서초역("서초역");

    private String name;

    MockStation(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
