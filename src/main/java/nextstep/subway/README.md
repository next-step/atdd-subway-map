
- [X] Scenario: 지하철 노선 생성 <br>
    When 지하철 노선 생성을 요청 하면 <br>
    Then 지하철 노선 생성이 성공한다. <br>

- [X] Scenario: 지하철 노선 목록 조회 <br>
Given 지하철 노선 생성을 요청 하고 <br>
Given 새로운 지하철 노선 생성을 요청 하고 <br>
When 지하철 노선 목록 조회를 요청 하면 <br>
Then 두 노선이 포함된 지하철 노선 목록을 응답받는다 <br>

- [X] Scenario: 지하철 노선 조회 <br>
Given 지하철 노선 생성을 요청 하고 <br>
When 생성한 지하철 노선 조회를 요청 하면 <br>
Then 생성한 지하철 노선을 응답받는다 <br>

- [X] Scenario: 지하철 노선 수정 <br>
Given 지하철 노선 생성을 요청 하고 <br>
When 지하철 노선의 정보 수정을 요청 하면 <br>
Then 지하철 노선의 정보 수정은 성공한다. <br>

- [X] Scenario: 지하철 노선 삭제 <br>
Given 지하철 노선 생성을 요청 하고 <br>
When 생성한 지하철 노선 삭제를 요청 하면 <br>
Then 생성한 지하철 노선 삭제가 성공한다. <br>

- [ ] Feature: 지하철역 관리 기능 <br>

Scenario: 중복이름으로 지하철역 생성 <br>
Given 지하철역 생성을 요청 하고 <br>
When 같은 이름으로 지하철역 생성을 요청 하면 <br>
Then 지하철역 생성이 실패한다. <br>

- [ ] Feature: 지하철 노선 관리 기능 <br>

Scenario: 중복이름으로 지하철 노선 생성 <br>
Given 지하철 노선 생성을 요청 하고 <br>
When 같은 이름으로 지하철 노선 생성을 요청 하면 <br>
Then 지하철 노선 생성이 실패한다. <br>
    