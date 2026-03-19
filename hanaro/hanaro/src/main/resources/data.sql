-- 회원 (비밀번호: 12345678)
INSERT IGNORE INTO member (email, password, nickname, role, deleted, createdAt, updatedAt)
VALUES ('admin@hanaro.com', '$2a$10$dD.hryZyqQFzkrWd4nbduOA/F8EFmbnCAfbuGXcybLwakeBN1jmDq', '관리자', 'ROLE_ADMIN', false, NOW(), NOW());

INSERT IGNORE INTO member (email, password, nickname, role, deleted, createdAt, updatedAt)
VALUES ('user1@hanaro.com', '$2a$10$dD.hryZyqQFzkrWd4nbduOA/F8EFmbnCAfbuGXcybLwakeBN1jmDq', '테스터1', 'ROLE_USER', false, NOW(), NOW());

INSERT IGNORE INTO member (email, password, nickname, role, deleted, createdAt, updatedAt)
VALUES ('user2@hanaro.com', '$2a$10$dD.hryZyqQFzkrWd4nbduOA/F8EFmbnCAfbuGXcybLwakeBN1jmDq', '테스터2', 'ROLE_USER', false, NOW(), NOW());

INSERT IGNORE INTO member (email, password, nickname, role, deleted, createdAt, updatedAt)
VALUES ('user3@hanaro.com', '$2a$10$dD.hryZyqQFzkrWd4nbduOA/F8EFmbnCAfbuGXcybLwakeBN1jmDq', '테스터3', 'ROLE_USER', false, NOW(), NOW());

-- 자유입출금 통장 (회원 가입 시 자동 생성되어야 하지만 seed 데이터로 추가)
INSERT IGNORE INTO account (member_id, accountNumber, accountType, status, balance, interestRate, maturityDate, createdAt, updatedAt)
SELECT id,
       CONCAT(LPAD(id, 3, '0'), '-', LPAD(id*1111, 4, '0'), '-', LPAD(id*2222, 4, '0')),
       'FREE', 'ACTIVE', 0, 0.0, NULL, NOW(), NOW()
FROM member
WHERE email IN ('admin@hanaro.com', 'user1@hanaro.com', 'user2@hanaro.com', 'user3@hanaro.com')
  AND NOT EXISTS (
    SELECT 1 FROM account a WHERE a.member_id = member.id AND a.accountType = 'FREE'
);

-- 상품
INSERT IGNORE INTO product (name, productType, depositAmount, paymentCycle, subscriptionPeriod, maturityRate, earlyTerminationRate, deleted, createdAt, updatedAt)
VALUES ('하나 정기예금 12개월', 'DEPOSIT', 1000000, NULL, 12, 3.5, 1.5, false, NOW(), NOW());

INSERT IGNORE INTO product (name, productType, depositAmount, paymentCycle, subscriptionPeriod, maturityRate, earlyTerminationRate, deleted, createdAt, updatedAt)
VALUES ('하나 정기예금 24개월', 'DEPOSIT', 3000000, NULL, 24, 4.0, 2.0, false, NOW(), NOW());

INSERT IGNORE INTO product (name, productType, depositAmount, paymentCycle, subscriptionPeriod, maturityRate, earlyTerminationRate, deleted, createdAt, updatedAt)
VALUES ('하나 자유적금 월납', 'SAVINGS', 100000, 'MONTHLY', 24, 4.0, 2.0, false, NOW(), NOW());

INSERT IGNORE INTO product (name, productType, depositAmount, paymentCycle, subscriptionPeriod, maturityRate, earlyTerminationRate, deleted, createdAt, updatedAt)
VALUES ('하나 자유적금 주납', 'SAVINGS', 50000, 'WEEKLY', 12, 3.0, 1.5, false, NOW(), NOW());

INSERT IGNORE INTO product (name, productType, depositAmount, paymentCycle, subscriptionPeriod, maturityRate, earlyTerminationRate, deleted, createdAt, updatedAt)
VALUES ('프리미엄 정기예금', 'DEPOSIT', 5000000, NULL, 36, 5.0, 2.5, false, NOW(), NOW());
