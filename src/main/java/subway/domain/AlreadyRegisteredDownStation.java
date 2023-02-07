package subway.domain;

public class AlreadyRegisteredDownStation extends RuntimeException {

    public AlreadyRegisteredDownStation() {
        super("이미 등록 된 역입니다.");
    }

}
