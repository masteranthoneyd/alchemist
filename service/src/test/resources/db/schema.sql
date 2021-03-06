-- noinspection SqlNoDataSourceInspectionForFile

DROP TABLE IF EXISTS user;

CREATE TABLE user
(
  id      BIGINT (20) NOT NULL COMMENT '主键ID',
  name    VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
  age     INT (11) NULL DEFAULT NULL COMMENT '年龄',
  email   VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
  deleted bit(1) DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS children;

CREATE TABLE children
(
    id      BIGINT (20) NOT NULL COMMENT '主键ID',
    name    VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    user_id BIGINT (20) NULL DEFAULT NULL COMMENT '上级ID',
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS test_user;

CREATE TABLE test_user
(
  id      BIGINT (20) NOT NULL COMMENT '主键ID',
  name    VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
  age     INT (11) NULL DEFAULT NULL COMMENT '年龄',
  email   VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
  deleted bit(1) DEFAULT NULL,
  PRIMARY KEY (id)
);


