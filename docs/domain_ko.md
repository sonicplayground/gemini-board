# 차량 관리 서비스 (Vehicle Management Service)

## 요약 (Summary)
본 서비스는 사용자가 본인의 차량 정보와 정비 이력을 효율적으로 관리할 수 있도록 지원하는 차량 관리 서비스입니다. 사용자는 차량 정보, 정비소, 정비사 정보를 등록하고 관리할 수 있으며, 정비 이력을 기록하고 확인할 수 있습니다.

## 요구사항 목록 (Requirements List)

### 사용자 관련 (User-Related)

1. 사용자의 정보는 이름, 닉네임, 성별, 나이, 주소, 사용자 타입, 프로필사진이 있다.
- 성별과 나이를 제외한 모든 필드는 필수 입력 항목이다.
2. 사용자는 본인의 정보를 수정할 수 있어야 한다.
3. 사용자타입은 서비스관리자, 서비스이용자, 정비소 관리자가 있다.

### 접근 제어 요구사항 (Access Control Requirements)

1. 로그인되지 않은 사용자는 로그인 페이지로 리다이렉트 되어야 한다.
2. 사용자는 아이디와 패스워드로 로그인이 가능하다.
3. 구글, 페이스북 등 외부 인증 서비스를 사용할 수 있다.
4. 사용자 비밀번호는 암호화 되어야 한다.

### 차량 관련 (Vehicle-Related)

1. 서비스 이용자 타입의 사용자는 본인의 차량 정보를 등록/ 수정/ 삭제/ 조회 할 수 있어야 한다.
- (차대번호, 차량 상태, 차량등록증 캡처본)를 제외한 모든 필드는
- 차량 정보는 다음과 같다.
    - 닉네임
    - 차량 사진
    - 차대번호
    - 차량 제조사
    - 차량 상태
      - 주행 거리
      - 타이어 교체 일시
      - 엔진오일 교체 시기
      - 브레이크 페드 교체 일시
    - 모델명
      - 프로젝트 전역으로 코드로 관리된다.
      - 관리되지 못한 모델의 경우를 고려해 직접입력이 가능하다.
    - 구매년도
    - 차량등록증 캡처본
    - 비고
- 차량 삭제 시, 소프트 삭제를 사용해야 한다(삭제 이력은 남기고 데이터는 삭제하지 않습니다).
- 차량 삭제 시, 삭제된 날짜, 삭제한 사용자 등의 정보와 함께 기록되어야 함
- 비고는 차량 특이사항(사고 이력, 보험 정보등)을 기록하는 용도이다.
- 모든 차량은 사용자와 관계가 있어야 합니다.

2. 차량 상태의 소모품에 해당하는 타이어, 엔진오일, 브레이크 패드의 경우 정비 교체 시기를 관리할 수 있습니다.
- 각 소모품별 교체 시기는 다음과 같습니다.
  - 엔진오일: 1년
  - 브레이크 패드: 2년
  - 타이어: 3년

### 정비소 정보 관련(MaintenanceShop)

1. 정비소 관리자 타입의 사용자는 정비소 정보를 등록/ 수정/ 조회 할 수 있어야 한다.
- 모든 필드는 필수 입력 항목이다.
- 정비소 정보는 다음과 같다.
  - 정비소 이름
  - 전화번호
  - 주소
- 정비소에는 여러 정비사가 속한다.
- 정비소 정보 변경에 대한 이력이 관리되어야 한다.
- 정비소 삭제 시, 소프트 삭제를 사용해야 한다.

2. 정비소 관리자 타입의 사용자는 정비사 정보를 등록/수정/삭제/조회 할 수 있어야 한다.
- 모든 필드는 필수 입력항목이다.
- 정비사 정보는 다음과 같다.
  - 이름
  - 소속 정비소
  - 개인 전화번호
- 정비사 정보 변경에 대한 이력이 관리되어야 한다.
- 정비사 삭제 시, 소프트 삭제를 사용해야 한다.

3. 정비소는 주소의 '시' 행정구역에 따라 검색이 가능하다.

### 차량 정비이력 정보 관련 (Vehicle Maintenance History-Related)

1. 사용자는 차량 정비이력 정보를 등록/ 수정/ 조회 할 수 있어야 한다.
- 차량 정비이력 정보는 차량 정보에 종속적이다.
- 모든 필드는 필수 입력 항목이다.
- 차량 정비이력 정보는 다음과 같다.
    - 정비소 정보, 
    - 정비사 정보
    - 정비 항목 (정비 항목은 여러개 일 수 있다.)
      - 정비 항목 이름 
      - 정비 항목별 금액 
        - 정비 금액에 대한 단위와 통화에 대한 명확한 정의가 필요하다.
  - 총계
  - 정비 년월
  - 사용자 확인 여부(Y, N)
- 사용자 확인 여부가 Y 인 경우, 정비이력은 수정, 삭제가 불가하다.
- 정비 이력 수정 시, 수정 이력이 남아야 한다 (데이터를 수정하면 기존 데이터를 별도로 보관해야 합니다.).

2. 정비 이력 검색
- 정비소, 정비 항목, 정비 년월에 따라 조건검색, 필터링 검색이 가능하다.