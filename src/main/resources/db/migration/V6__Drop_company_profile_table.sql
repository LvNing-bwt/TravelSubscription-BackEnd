-- 删除旧的company_profile表
DROP TABLE IF EXISTS company_profile;

ALTER TABLE company_account
ADD COLUMN company_id BIGINT NOT NULL
COMMENT '关联公司ID';

ALTER TABLE company_account
ADD CONSTRAINT fk_company_account_company
FOREIGN KEY (company_id) REFERENCES companies(company_id);

CREATE INDEX idx_company_account_company_id ON company_account(company_id);