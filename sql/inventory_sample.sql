-- 재고관리 포트폴리오용 SQL: 원본 테이블 구조 4개와 새로 작성한 샘플 데이터

-- MySQL 8.0 이상. 원본 posdb와 구분한 새 DB를 사용합니다.

-- 새 DB에 한 번 실행하세요. 기존 데이터 삭제 명령은 포함하지 않습니다.

CREATE DATABASE IF NOT EXISTS inventory_portfolio CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

USE inventory_portfolio;

CREATE TABLE `category` (
  `카테고리코드` int NOT NULL AUTO_INCREMENT,
  `카테고리명` varchar(10) NOT NULL,
  PRIMARY KEY (`카테고리코드`),
  UNIQUE KEY `카테고리명` (`카테고리명`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `menu` (
  `메뉴코드` int NOT NULL AUTO_INCREMENT,
  `카테고리코드` int NOT NULL,
  `메뉴명` varchar(20) NOT NULL,
  `카테고리명` varchar(10) NOT NULL,
  `재고량` int DEFAULT NULL,
  `가격` int NOT NULL,
  PRIMARY KEY (`메뉴코드`),
  KEY `fk_category` (`카테고리코드`),
  CONSTRAINT `fk_category` FOREIGN KEY (`카테고리코드`) REFERENCES `category` (`카테고리코드`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `options` (
  `옵션코드` int NOT NULL AUTO_INCREMENT,
  `옵션명` varchar(20) NOT NULL,
  `옵션가격` int DEFAULT NULL,
  PRIMARY KEY (`옵션코드`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `stock` (
  `재고코드` int NOT NULL AUTO_INCREMENT,
  `재고명` varchar(20) NOT NULL,
  `보유재고량` int NOT NULL,
  `조정사유` varchar(30) DEFAULT NULL,
  `조정개수` int DEFAULT NULL,
  `조정후개수` int NOT NULL,
  PRIMARY KEY (`재고코드`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO category (카테고리코드, 카테고리명) VALUES (1, '음료'), (2, '디저트');

INSERT INTO menu (메뉴코드, 카테고리코드, 메뉴명, 카테고리명, 재고량, 가격) VALUES (1, 1, '아메리카노', '음료', 20, 3000), (2, 2, '쿠키', '디저트', 10, 2000);

INSERT INTO options (옵션코드, 옵션명, 옵션가격) VALUES (1, '샷 추가', 500);

INSERT INTO stock (재고코드, 재고명, 보유재고량, 조정사유, 조정개수, 조정후개수) VALUES (1, '아메리카노', 20, '샘플 초기 등록', 0, 20), (2, '쿠키', 10, '샘플 초기 등록', 0, 10);
