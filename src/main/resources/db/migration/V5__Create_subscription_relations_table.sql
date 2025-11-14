CREATE TABLE subscription_relations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    subscriber_company_id BIGINT NOT NULL,        -- 订阅方公司ID
    target_node_id BIGINT NOT NULL,               -- 被订阅的目标节点ID
    subscription_time DATETIME NOT NULL,          -- 订阅时间

    -- 双写状态管理
    backend_status ENUM('ACTIVE', 'CANCELLED') DEFAULT 'ACTIVE',
    openfire_status ENUM('ACTIVE', 'CANCELLED') DEFAULT 'ACTIVE',

    -- 同步相关信息
    last_sync_time DATETIME,                      -- 最后同步到OpenFire的时间
    sync_attempts INT DEFAULT 0,                  -- 同步尝试次数
    sync_error_message TEXT,                      -- 同步错误信息

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (subscriber_company_id) REFERENCES companies(company_id),
    FOREIGN KEY (target_node_id) REFERENCES pubsub_nodes(node_id),

    -- 关键业务约束和索引
    UNIQUE KEY uk_company_subscription (subscriber_company_id, target_node_id),
    INDEX idx_subscriber_company (subscriber_company_id),
    INDEX idx_target_node (target_node_id),
    INDEX idx_subscription_time (subscription_time),
    INDEX idx_backend_status (backend_status),
    INDEX idx_sync_status (openfire_status, last_sync_time)
) ENGINE=InnoDB COMMENT = '订阅关系表';