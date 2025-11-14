DROP TABLE IF EXISTS company_profile;

CREATE TABLE company_profile (
    company_id BIGINT PRIMARY KEY,                    -- 🔗 关联公司ID
    contact_person VARCHAR(100),                   -- 联系人
    company_phone VARCHAR(20),
    avatar_url VARCHAR(255),
    company_address TEXT,                         -- 详细地址
    company_intro TEXT,
    refresh_days INT DEFAULT 7,
    business_license_url VARCHAR(255),
    id_card_front_url VARCHAR(255),
    id_card_back_url VARCHAR(255),
    real_name_verified BOOLEAN DEFAULT FALSE,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (company_id) REFERENCES companies(company_id) ON DELETE CASCADE,
    INDEX idx_company_id (company_id)
);