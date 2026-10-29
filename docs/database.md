# DB 구성

`sql/inventory_sample.sql`은 사용자가 제공한 Dump20241208.sql에서 category, menu, options, stock의 테이블 생성문을 추출한 파일입니다. MySQL 8.0.27에서 생성된 원본 구조를 바탕으로 하며 기존 AUTO_INCREMENT 다음 번호는 제거했습니다.

- 원본 직원 정보·계정·비밀번호와 매출 데이터는 포함하지 않았습니다.
- 공개용 데이터는 새로 작성한 예시입니다.
- 원본 posdb를 덮어쓰지 않도록 inventory_portfolio DB를 사용합니다.
- SQL 복원 및 Java와의 실제 연동은 아직 검증하지 않았습니다.
- menu와 stock은 별도 테이블이며 제공된 덤프에는 자동 동기화 트리거가 없습니다.

| 테이블 | 역할 |
| --- | --- |
| category | 카테고리 코드·이름 |
| menu | 메뉴·가격·재고량, category 참조 외래키 |
| options | 옵션명·추가가격 |
| stock | 재고 조회·정렬용 수량 및 조정 내역 |
