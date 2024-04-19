--
-- 表的结构 `order`
--

CREATE TABLE `order`
(
    `id`          varchar(32)    NOT NULL COMMENT '订单号',
    `user_id`     varchar(32)    NOT NULL COMMENT '购买人',
    `seller_id`   varchar(32)    NOT NULL COMMENT '卖家id',
    `sku_id`      varchar(32)    NOT NULL COMMENT 'SkuId',
    `amount`      int(11)        NOT NULL COMMENT '购买数量',
    `money`       decimal(10, 2) NOT NULL COMMENT '购买金额',
    `pay_time`    datetime                DEFAULT NULL COMMENT '支付时间',
    `pay_status`  varchar(255)   NOT NULL DEFAULT '0' COMMENT '支付状态',
    `del_flag`    tinyint(1)     NOT NULL DEFAULT 0 COMMENT '删除标志（0代表存在 1代表删除）',
    `create_by`   varchar(32)             DEFAULT NULL COMMENT '创建人',
    `create_time` timestamp      NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
    `update_by`   varchar(32)             DEFAULT NULL COMMENT '修改人',
    `update_time` timestamp      NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '修改时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='订单表';

--
-- 转储表的索引
--

--
-- 表的索引 `order`
--
ALTER TABLE `order`
    ADD PRIMARY KEY (`id`),
    ADD KEY `idx_user_id` (`user_id`),
    ADD KEY `idx_seller_id` (`seller_id`);
COMMIT;