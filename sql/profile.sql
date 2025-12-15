## 数据库表结构设计

### 1. 用户健康档案主表 `health_profiles`

CREATE TABLE health_profiles
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id        BIGINT                            NOT NULL COMMENT '用户ID，关联users表',
    gender         ENUM ('male', 'female')           NOT NULL DEFAULT 'male' COMMENT '性别',
    age            TINYINT UNSIGNED                  NOT NULL COMMENT '年龄',
    height         DECIMAL(5, 2)                     NOT NULL COMMENT '身高(cm)',
    weight         DECIMAL(5, 2)                     NOT NULL COMMENT '体重(kg)',
    activity_level DECIMAL(4, 3)                     NOT NULL DEFAULT 1.2 COMMENT '活动系数',
    goal           ENUM ('lose', 'maintain', 'gain') NOT NULL DEFAULT 'maintain' COMMENT '健身目标',
    is_active      BOOLEAN                                    DEFAULT TRUE COMMENT '是否为当前使用的档案',
    created_at     TIMESTAMP                                  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at     TIMESTAMP                                  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_user_id (user_id),
    INDEX idx_user_active (user_id, is_active),

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) COMMENT '用户健康档案表';

### 2. 身体指标历史记录表 `health_metrics`

CREATE TABLE health_metrics
(
    id                       BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id                  BIGINT        NOT NULL COMMENT '用户ID',
    profile_id               BIGINT        NOT NULL COMMENT '健康档案ID',
    weight                   DECIMAL(5, 2) NOT NULL COMMENT '体重(kg)',
    body_fat_rate            DECIMAL(5, 2) COMMENT '体脂率(%)',
    muscle_mass              DECIMAL(5, 2) COMMENT '肌肉量(kg)',
    waistline                DECIMAL(5, 2) COMMENT '腰围(cm)',
    hipline                  DECIMAL(5, 2) COMMENT '臀围(cm)',
    chest_circumference      DECIMAL(5, 2) COMMENT '胸围(cm)',
    blood_pressure_systolic  SMALLINT COMMENT '收缩压(mmHg)',
    blood_pressure_diastolic SMALLINT COMMENT '舒张压(mmHg)',
    heart_rate               SMALLINT COMMENT '心率(次/分钟)',
    record_date              DATE          NOT NULL COMMENT '记录日期',
    notes                    TEXT COMMENT '备注信息',
    created_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    INDEX idx_user_date (user_id, record_date),
    INDEX idx_profile (profile_id),

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (profile_id) REFERENCES health_profiles (id) ON DELETE CASCADE
) COMMENT '身体指标历史记录表';

### 3. 营养目标表 `nutrition_goals`

CREATE TABLE nutrition_goals
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id         BIGINT        NOT NULL COMMENT '用户ID',
    profile_id      BIGINT        NOT NULL COMMENT '健康档案ID',
    bmr             DECIMAL(7, 2) NOT NULL COMMENT '基础代谢率(kcal)',
    tdee            DECIMAL(7, 2) NOT NULL COMMENT '每日总能量消耗(kcal)',
    target_calories DECIMAL(7, 2) NOT NULL COMMENT '目标摄入热量(kcal)',
    protein_grams   DECIMAL(6, 2) NOT NULL COMMENT '蛋白质推荐量(g)',
    carbs_grams     DECIMAL(6, 2) NOT NULL COMMENT '碳水化合物推荐量(g)',
    fat_grams       DECIMAL(6, 2) NOT NULL COMMENT '脂肪推荐量(g)',
    fiber_grams     DECIMAL(6, 2) DEFAULT 25 COMMENT '膳食纤维推荐量(g)',
    water_ml        DECIMAL(7, 2) DEFAULT 2000 COMMENT '饮水量推荐(ml)',
    effective_date  DATE          NOT NULL COMMENT '生效日期',
    is_active       BOOLEAN       DEFAULT TRUE COMMENT '是否当前使用',
    created_at      TIMESTAMP     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      TIMESTAMP     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_user_profile (user_id, profile_id),
    INDEX idx_active (user_id, is_active),
    INDEX idx_effective_date (effective_date),

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (profile_id) REFERENCES health_profiles (id) ON DELETE CASCADE
) COMMENT '营养目标表';

### 4. 健康目标设置表 `health_objectives`

CREATE TABLE health_objectives
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id        BIGINT                                                                                  NOT NULL COMMENT '用户ID',
    profile_id     BIGINT                                                                                  NOT NULL COMMENT '健康档案ID',
    objective_type ENUM ('weight_loss', 'weight_gain', 'muscle_gain', 'fat_loss', 'endurance', 'strength') NOT NULL COMMENT '目标类型',
    target_value   DECIMAL(6, 2) COMMENT '目标数值',
    current_value  DECIMAL(6, 2) COMMENT '当前数值',
    unit           VARCHAR(20) COMMENT '单位(kg, %, cm等)',
    target_date    DATE COMMENT '目标完成日期',
    priority       TINYINT   DEFAULT 1 COMMENT '优先级(1-5)',
    is_achieved    BOOLEAN   DEFAULT FALSE COMMENT '是否已达成',
    is_active      BOOLEAN   DEFAULT TRUE COMMENT '是否激活',
    notes          TEXT COMMENT '备注',
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_user_profile (user_id, profile_id),
    INDEX idx_active_priority (is_active, priority),

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (profile_id) REFERENCES health_profiles (id) ON DELETE CASCADE
) COMMENT '健康目标设置表';

### 5. 用户健康问卷表 `health_assessments`

CREATE TABLE health_assessments
(
    id                   BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id              BIGINT                                    NOT NULL COMMENT '用户ID',
    assessment_type      ENUM ('initial', 'follow_up', 'periodic') NOT NULL COMMENT '评估类型',

    -- 生活习惯问卷
    exercise_frequency   TINYINT COMMENT '每周运动次数',
    exercise_duration    SMALLINT COMMENT '每次运动时长(分钟)',
    exercise_intensity   ENUM ('low', 'medium', 'high') COMMENT '运动强度',
    sleep_hours          TINYINT COMMENT '每日睡眠时长(小时)',
    sleep_quality        ENUM ('poor', 'fair', 'good', 'excellent') COMMENT '睡眠质量',
    stress_level         ENUM ('low', 'medium', 'high') COMMENT '压力水平',

    -- 饮食习惯
    meal_pattern         ENUM ('3_meals', '5_6_meals', 'irregular') COMMENT '进餐模式',
    dietary_restrictions JSON COMMENT '饮食限制',
    food_allergies       JSON COMMENT '食物过敏',
    water_intake_ml      DECIMAL(5, 2) COMMENT '每日饮水量(ml)',

    -- 健康状况
    chronic_diseases     JSON COMMENT '慢性疾病',
    medications          JSON COMMENT '正在服用的药物',
    supplements          JSON COMMENT '营养补充剂',

    assessment_date      DATE                                      NOT NULL COMMENT '评估日期',
    next_assessment_date DATE COMMENT '下次评估日期',
    notes                TEXT COMMENT '备注',
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    INDEX idx_user_date (user_id, assessment_date),
    INDEX idx_type (assessment_type),

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) COMMENT '用户健康评估表';
