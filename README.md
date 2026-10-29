# InventoryManagement

Java Swing과 MySQL을 이용한 POS 재고관리 기능입니다.
기존 POS 팀 프로젝트의 재고관리 부분을 포트폴리오용으로 분리하여 정리했습니다.

## 담당 범위

재고관리 화면 및 JDBC 기반 DB 연동을 중심으로 정리한 저장소입니다.
로그인, 직원관리, 매출관리 및 POS 결제 화면은 포함하지 않습니다.

## 사용 기술

- Java, Swing, JDBC
- MySQL
- Eclipse IDE

원래 개발 당시 JDK·MySQL·Eclipse 버전과 개발 기간은 아직 확인하지 않았습니다.

## 주요 구현 기능

| 기능 | 구현 내용 |
| --- | --- |
| 재고 조회 | stock 테이블 조회 결과를 JTable에 표시 |
| 재고 정렬 | 조정후개수 기준 오름차순·내림차순 정렬 |
| 메뉴 관리 | 메뉴 추가, 가격·재고량 변경, 삭제 |
| 검색 | 메뉴명·카테고리명 부분 검색 |
| 카테고리 관리 | 카테고리 추가, 해당 메뉴와 함께 삭제 |
| 옵션 관리 | 옵션 조회·추가·삭제 |
| 입력 검사 | 숫자 입력, 음수 재고, 중복 메뉴 등의 검사 |

## 실행 방법

1. Eclipse에서 File → New → Java Project로 프로젝트를 만듭니다.
2. module-info.java는 생성하지 않습니다.
3. 이 저장소의 src/Inventory.java를 프로젝트의 src 폴더에 복사합니다.
4. Inventory.java 우클릭 → Run As → Java Application으로 실행합니다.
5. DB 기능을 사용하려면 아래 드라이버·환경변수·테이블을 준비합니다.

기본 UI는 DB 연결 실패를 처리한 뒤 표시될 수 있지만, DB가 없으면 목록 조회와 저장 기능은 사용할 수 없습니다.

### DB 복원

MySQL 8.0 이상에서 `sql/inventory_sample.sql`을 실행합니다. 원래 `posdb`와 별개인 `inventory_portfolio` DB를 사용합니다. 샘플 SQL은 새 DB에 한 번 실행하는 용도이며 반복 실행하면 테이블 또는 기본키 중복 오류가 날 수 있습니다.

### MySQL 연결

MySQL Connector/J의 JAR를 준비하고 프로젝트 Properties → Java Build Path → Libraries → Classpath → Add External JARs에서 추가합니다.

Eclipse의 Run → Run Configurations → Java Application → Inventory → Environment에서 다음 변수를 추가합니다.

| 변수 | 값 예시 |
| --- | --- |
| INVENTORY_DB_URL | jdbc:mysql://localhost:3306/inventory_portfolio?serverTimezone=UTC |
| INVENTORY_DB_USER | 자신의 DB 사용자명 |
| INVENTORY_DB_PASSWORD | 자신의 DB 비밀번호 |

실제 비밀번호는 소스나 README에 기록하지 않습니다. 환경변수를 설정하는 대신 .env 파일만 만드는 것으로는 동작하지 않습니다.

## DB 및 검증 상태

- 원본 덤프에서 재고관리용 테이블 4개의 구조를 추출한 `sql/inventory_sample.sql`을 포함합니다. 데이터는 원본에서 가져오지 않고 새로 작성한 샘플입니다. 직원 및 매출 테이블은 제외했습니다.
- 필요한 테이블과 컬럼은 [DB 안내](docs/database.md)를 참고하세요.
- 이 정리본은 컴파일 및 GUI·DB 통합 실행을 아직 검증하지 않았습니다.
- 메뉴 변경은 menu 테이블에, 메인 목록 조회는 stock 테이블에 접근합니다. 제공된 덤프에는 두 테이블을 동기화하는 트리거가 없습니다. 현재 코드에도 자동 동기화가 없어 메뉴 변경이 메인 재고 목록에 자동 반영되지 않는 한계가 있습니다.

## 포트폴리오 정리 과정의 변경

- 원본 Inventory.java에서 DB 접속 계정·비밀번호를 제거하고 환경변수로 분리했습니다.
- URL 예시에 serverTimezone 철자를 사용했습니다.
- 그 외 기존 UI 및 SQL 처리 로직은 유지했습니다.

## 보완할 부분

- 문자열 연결로 작성한 SQL을 PreparedStatement로 변경
- try-with-resources로 DB 자원 정리
- 여러 DELETE 작업의 트랜잭션 처리
- DB 작업과 UI 코드 분리 및 입력 검증 개선

위 항목들은 향후 개선 계획이며, 완료된 기능으로 표현하지 않습니다.

## 실행 화면 및 시연

실행을 확인한 뒤 docs 폴더에 화면 이미지를 추가하고 이 위치에 연결할 예정입니다.
현재 시연 영상과 화면 이미지는 포함되어 있지 않습니다.
