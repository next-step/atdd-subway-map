package nextstep.subway.domain.step_feature;

import nextstep.subway.domain.Station;

public class StationStepFeature {

    public static final String GANGNAM_STATION_NAME = "강남역";
    public static final String YEOKSAM_STATION_NAME = "역삼역";
    public static final String NONHYEON_STATION_NAME = "논현역";
    public static final String PANGYO_STATION_NAME = "판교역";


    public static Station createGangnamStation() {
        return new Station(1L, GANGNAM_STATION_NAME);
    }

    public static Station createYeoksamStation() {
        return new Station(2L, YEOKSAM_STATION_NAME);
    }

    public static Station createNonhyeonStation() {
        return new Station(3L, NONHYEON_STATION_NAME);
    }

    public static Station createPangyoStation() {
        return new Station(4L, PANGYO_STATION_NAME);
    }

}
