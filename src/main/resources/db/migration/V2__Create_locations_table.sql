CREATE TABLE locations (
    location_id VARCHAR(10) PRIMARY KEY,      -- 国家标准行政区划代码
    location_name VARCHAR(50) NOT NULL,       -- 行政区划名称
    parent_id VARCHAR(10) NOT NULL,           -- 父级ID，省级的父级为'0'
    level TINYINT NOT NULL,                   -- 层级：1-省份, 2-城市
    display_order INT DEFAULT 0,              -- 显示顺序
    is_active BOOLEAN DEFAULT TRUE,           -- 是否启用

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (parent_id) REFERENCES locations(location_id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_level (level),
    INDEX idx_display_order (display_order)
) ENGINE=InnoDB COMMENT = '标准行政区划表';