CREATE TABLE subscription_tiers (
    company_id BIGINT PRIMARY KEY,
    tier_level ENUM('BASIC', 'STANDARD', 'PROFESSIONAL'),  -- BASIC：市级；STANDARD：省级；PROFESSIONAL：全国
    valid_until DATE,
    created_at TIMESTAMP
);
