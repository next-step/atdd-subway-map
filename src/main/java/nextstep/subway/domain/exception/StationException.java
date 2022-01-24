package nextstep.subway.domain.exception;

import nextstep.subway.domain.Station;

public class StationException extends RuntimeException {

    private StationException(String message) {
        super(message);
    }

    public static class Duplicated extends StationException {
        public Duplicated(Station station) {
            super("Already exists station; name=" + station.getName());
        }
    }
}
