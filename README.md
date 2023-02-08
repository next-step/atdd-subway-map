# NextStep 지하철 노선도 미션
> [ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 미션 피드백 링크
- Step 1 : [https://github.com/next-step/atdd-subway-map/pull/604](https://github.com/next-step/atdd-subway-map/pull/604)
- Step 2 : [https://github.com/next-step/atdd-subway-map/pull/685](https://github.com/next-step/atdd-subway-map/pull/685)
- Step 3 : [https://github.com/next-step/atdd-subway-map/pull/710](https://github.com/next-step/atdd-subway-map/pull/710)

## 미션 관련 ERD
- ERD Cloud - https://www.erdcloud.com/d/tquYyrSeEHaqpZThG

<img src="images/db-erd.png">


## 미션 내용

### STEP 1
- [x] 지하철역 목록 조회 인수 테스트 작성하기
- [x] 지하철역 삭제 인수 테스트 작성하기

### STEP 2
- [x] 지하철 노선 생성하기
  - [x] 노선 생성 시 들어온 역 정보들을 상행종점역과 하행종점역으로 등록하기
- [x] 지하철 노선 목록 조회하기
- [x] 지하철 노선 조회하기
- [x] 지하철 노선 수정하기
- [x] 지하철 노선 삭제하기
- [x] 인수 테스트 격리하기

### STEP 3
- [x] 지하철 구간 등록하기
    - [x] 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
    - [x] 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
- [x] 구간을 제거한다.
    - [x] 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
    - [x] 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
