CREATE TABLE pubsub_nodes (
    node_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    node_name VARCHAR(100) NOT NULL,              -- 节点显示名称
    node_type ENUM('PROVINCE', 'CITY', 'COMPANY') NOT NULL,
    location_id VARCHAR(10),                      -- 仅PROVINCE/CITY节点有值
    parent_node_id BIGINT,                        -- 父节点ID，省级为NULL
    company_id BIGINT,                            -- 仅COMPANY节点有值
    is_active BOOLEAN DEFAULT FALSE,               -- 是否活跃
    display_order INT DEFAULT 0,                  -- 同级节点显示顺序

    -- OpenFire集成字段
    openfire_node VARCHAR(255),                   -- OpenFire PubSub节点ID
    openfire_service VARCHAR(100),                -- OpenFire服务名

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (location_id) REFERENCES locations(location_id),
    FOREIGN KEY (parent_node_id) REFERENCES pubsub_nodes(node_id),
    FOREIGN KEY (company_id) REFERENCES companies(company_id),

    -- 关键索引
    INDEX idx_node_type (node_type),
    INDEX idx_parent_node (parent_node_id),
    INDEX idx_company (company_id),
    INDEX idx_location (location_id),
    INDEX idx_active_order (is_active, display_order),
    UNIQUE KEY uk_openfire_node (openfire_node)   -- OpenFire节点唯一
) ENGINE=InnoDB COMMENT = '发布订阅节点树';