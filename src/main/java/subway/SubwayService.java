package subway;

public interface SubwayService<T, V> {

    V create(T request);

    // SubwayResponse get(SubwayRequest subwayRequest);
    //
    // SubwayResponse update(SubwayRequest subwayRequest);
    //
    // SubwayResponse delete(SubwayRequest subwayRequest);
}