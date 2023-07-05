# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 2단계 - 지하철 노선 관리
### 시나리오
1. 지하철 노선 생성
   1. when 지하철 노선을 생성하면
   2. then 지하철 노선이 생성되고,
   3. then 저장한 노선을 조회할 수 있습니다.
2. 지하철 노선 목록 조회
   1. given 지하철 노선을 생성하고
   2. when 지하철 노선 목록을 조회하면
   3. then 전체 지하철 노선 목록이 조회되고
   4. then 생성한 지하철 노선이 포함됩니다.
3. 지하철 노선 조회
   1. given 지하철 노선을 생성하고
   2. when 생성한 지하철 노선을 조회하면
   3. then 생성한 지하철 노선 정보를 응답받습니다.
4. 지하철 노선 수정
   1. given 지하철 노선을 생성하고
   2. and 노선 수정 정보를 가진 상태에서
   3. when 생성한 노선을 수정 정보로 수정 하면
   4. then 생성 노선을 조회했을때 정보가 수정 정보와 일치합니다.
5. 지하철 노선 삭제
   1. given 지하철 노선을 생성하고
   2. when 생성한 노선을 삭제하면
   3. then 해당 지하철 노선을 조회했을때 결과가 존재하지 않습니다.

## 3단계 - 지하철 구간 관리
인수조건 정의 &rarr; 인수 테스트 작성 &rarr; 기능 구현 &rarr; 인수 테스트 격리 &rarr; 리팩터링
### 시나리오
1. 구간 등록_상행역 오류
   1. given 하행역id, 상행역id, 구간거리를 입력하고
   2. and 등록하려는 노선이 있고
   3. and 입력 구간의 상행역이 등록하려는 노선의 하행 종점역이 아니면
   4. when 노선에 입력 구간을 등록하려고 할때
   5. then 등록 불가 합니다.
2. 구간 등록_하행역 오류
   1. given 하행역id, 상행역id, 구간거리를 입력하고
   2. and 등록하려는 노선이 있고
   3. and 입력 구간의 하행역이 등록하려는 노선에 존재하는 역이면
   4. when 노선에 입력 구간을 등록하려고 할때
   5. then 등록 불가 합니다.
3. 구간 등록
   1. given 하행역id, 상행역id, 구간거리를 입력하고
   2. and 등록하려는 노선이 있고
   3. and 입력 구간의 상행역이 등록하려는 노선의 하행 종점역이고
   4. and 입력 구간의 하행역이 등록하려는 노선에 존재하지 않는 역이면
   5. when 노선에 입력 구간을 등록할때
   6. then 정상 등록 됩니다.
4. 구간 제거_미존재 역
   1. given 2개 이상의 구간을 포함하는 노선과 삭제하려는 역ID가 있을때
   2. and 삭제하려는 역이 노선구간에 포함되지 않으면
   3. when 노선에서 구간을 삭제하려 할때
   4. then 삭제 불가합니다.
5. 구간 제거_하행 종착역 아님
   1. given 2개 이상의 구간을 포함하는 노선과 삭제하려는 역ID가 있을때
   2. and 삭제하려는 역이 해당 노선의 하행 종착역이 아니면
   3. when 노선에서 구간을 삭제하려 할때
   4. then 삭제 불가합니다.
6. 구간 제거_구간 1개 존재
   1. given 노선과 삭제하려는 역ID가 있을때
   2. and 역이 노선에 포함되고
   3. and 삭제하려는 노선에 구간이 한개만 있으면
   4. when 노선에서 구간을 삭제하려 할때
   5. then 삭제 불가합니다.
7. 구간 제거
   1. given 삭제하려는 노선과 역 정보가 있을때
   2. and 삭제하려는 역이 노선의 하행 종착역이고
   3. and 노선에 구간이 2개 이상이면
   4. when 노선에서 구간을 삭제하려 할때
   5. then 정상 삭제됩니다.