package subway.line.dto.response;

public class SectionResponse {

    private final boolean isSuccess = true;

    public SectionResponse() {
    }

    public static SectionResponse of() {
        return new SectionResponse();
    }

    @Override
    public String toString() {
        return "SectionResponse{" +
                "isSuccess=" + isSuccess +
                '}';
    }

}
