package nextstep.subway.applicaion.object;

import nextstep.subway.exception.NotExistedStationException;

public class Distance {
    int value;

    public Distance(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void checkDistanceLessThanZero() {
        if (this.value < 0) {
            throw new NotExistedStationException("[notValid]:distance");
        }
    }
}
