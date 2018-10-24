CREATE DATABASE IF NOT EXISTS OCR_TEST DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
USE OCR_TEST;
CREATE TABLE IF NOT EXISTS ocr_upload_file_context(
  id         INT AUTO_INCREMENT                                NOT NULL
PRIMARY KEY,
  fileId     VARCHAR(64)                         NULL
COMMENT '文件Id',
  coordinate TEXT                                NULL
COMMENT '图片坐标',
context    TEXT                                NULL
COMMENT '文件中内容',
  createTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
CREATE TABLE IF NOT EXISTS ocr_upload_file_info(
  id             INT AUTO_INCREMENT
    PRIMARY KEY,
  fileName       VARCHAR(128) DEFAULT ''             NOT NULL
  COMMENT '上传文件名称',
  fileId        VARCHAR(64)  DEFAULT ''             NOT NULL
  COMMENT '图片唯一标识Id',
  fileUploadPath VARCHAR(256) DEFAULT ''             NOT NULL
  COMMENT '上传文件路劲',
  fileSize       INT                                 NOT NULL
  COMMENT '上传文件大小',
  fileSaveName   VARCHAR(128) DEFAULT ''             NOT NULL
  COMMENT '上传文件保存名称',
  createTime     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL
  COMMENT '上传文件时间',
  updateTime     TIMESTAMP                           NULL
  COMMENT '修改文件时间',
  deleted        TINYINT(1) DEFAULT '0'              NULL
  COMMENT '0:有效;1:无效'
);
CREATE TABLE IF NOT EXISTS ocr_upload_png_tiff_info(
  id              INT AUTO_INCREMENT
    PRIMARY KEY,
  uploadDirectory VARCHAR(512)                        NULL
  COMMENT '上传路径',
  pngFileName     VARCHAR(256)                        NULL
  COMMENT 'png图片',
  tifFileName     VARCHAR(256)                        NULL
  COMMENT 'tif图片',
  fontFamily      VARCHAR(32)                         NULL
  COMMENT '字体',
  lang            VARCHAR(32)                         NULL
  COMMENT '语言',
  boxName         VARCHAR(256)                        NULL
  COMMENT 'box文件',
  picText         TEXT                                NULL
  COMMENT '图片文字',
  timeStamp       BIGINT                              NULL
  COMMENT '时间戳',
  createTime      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL
  COMMENT '上传时间',
  updateTime      TIMESTAMP                           NULL
  COMMENT '修改时间',
  deleted         INT DEFAULT '0'                     NULL
  COMMENT '删除状态(0:未删除,1:删除)'
);