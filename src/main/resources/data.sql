-- 1. 필수 컬럼(nickname, password, phone)에 값을 모두 채워서 유저 삽입
INSERT INTO users (username, nickname, password, phone)
VALUES ('user1', '닉네임1', 'password123', '010-1234-5678');

INSERT INTO users (username, nickname, password, phone)
VALUES ('user2', '2번유저', 'pass123', '010-5678-5678');

-- 2. 카테고리 삽입
INSERT INTO categories (name) VALUES ('캠핑용품');

INSERT INTO categories (name) VALUES ('전자제품');

-- 3. 아이템 삽입
-- INSERT INTO items (title, description, price_per_day, is_available, owner_id, category_id, created_at)
-- VALUES ('텐트', '물품 설명', 10000, TRUE, 1, 1, CURRENT_TIMESTAMP());