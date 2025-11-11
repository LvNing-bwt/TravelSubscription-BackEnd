-- 注册表 --
CREATE TABLE company_account (
    uid BIGINT AUTO_INCREMENT PRIMARY KEY,    -- 👑 核心ID，整个系统唯一标识
    username VARCHAR(64) UNIQUE NOT NULL,     -- 🔑 登录用户名（唯一）
    password_hash VARCHAR(255) NOT NULL,      -- 🔒 加密后的密码
    email VARCHAR(200),                       -- 📧 备用登录方式
    phone VARCHAR(20) UNIQUE,                 -- 📞 备用登录方式

    -- 🆕 新增：角色权限管理
    role ENUM('SUPER_ADMIN', 'ADMIN', 'COMPANY') DEFAULT 'COMPANY',  -- 修复：这里缺了逗号

    -- 🚦 账户状态管理
    status TINYINT DEFAULT 1,                 -- 1-正常 2-禁用（控制登录权限）
    last_login_time TIMESTAMP NULL,           -- ⏰ 最后登录时间（用于安全审计）
    login_fail_count INT DEFAULT 0,           -- 🛡️ 连续登录失败次数（防暴力破解）

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- 🚀 性能优化索引
    INDEX idx_username (username),            -- 用户名登录索引
    INDEX idx_email (email),                  -- 邮箱登录索引
    INDEX idx_phone (phone),                  -- 手机登录索引
    INDEX idx_status (status),                -- 状态过滤索引（修复：这里缺了逗号）
    INDEX idx_role (role)                     -- 角色索引
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 个人信息表 --
CREATE TABLE company_profile (
    uid BIGINT PRIMARY KEY,                   -- 🔗 关联account表的uid
    username VARCHAR(100) NOT NULL,
    nickname VARCHAR(100),
    phone VARCHAR(20) UNIQUE,
    company_name VARCHAR(200) UNIQUE,         -- 🏢 公司名称（唯一标识）（修复：这里缺了逗号）
    legal_person_gender VARCHAR(10),          -- 👨‍💼 法人性别

    -- 📋 公司展示信息
    avatar_url VARCHAR(255),                  -- 🖼️ 公司Logo链接
    company_address TEXT,                     -- 🏠 公司地址（可能很长）
    company_intro TEXT,                       -- 📝 公司简介（可能很长）

    -- 🔄 刷新天数设置
    refresh_days INT DEFAULT 7,

    -- 📄 认证相关材料
    business_license_url VARCHAR(255),        -- 📑 营业执照
    id_card_front_url VARCHAR(255),           -- 🆔 身份证正面
    id_card_back_url VARCHAR(255),            -- 🆔 身份证反面
    real_name_verified BOOLEAN DEFAULT FALSE, -- ✅ 实名认证状态

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (uid) REFERENCES company_account(uid) ON DELETE CASCADE,
    INDEX idx_company_name (company_name),    -- 按公司名搜索索引
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;