CREATE TABLE companies (
    company_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    company_name VARCHAR(200) NOT NULL,
    province_id VARCHAR(10) NOT NULL,
    city_id VARCHAR(10) NOT NULL,
    credit_code VARCHAR(18) UNIQUE,             -- 统一社会信用代码
    legal_person_name VARCHAR(100),             -- 法人姓名
    company_status ENUM('PENDING', 'ACTIVE', 'SUSPENDED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (province_id) REFERENCES locations(location_id),
    FOREIGN KEY (city_id) REFERENCES locations(location_id),

    UNIQUE KEY uk_company_city (company_name, city_id),
    INDEX idx_company_status (company_status),
    INDEX idx_province_city (province_id, city_id),
    INDEX idx_credit_code (credit_code),
    INDEX idx_created_at (created_at)
);