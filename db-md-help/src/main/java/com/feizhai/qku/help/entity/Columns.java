package com.feizhai.qku.help.entity;

import lombok.Data;

/**
 * @author wxm
 * @date 2020/6/10.
 */
@Data
public class Columns {
    /**
     * 注释
     */
    private String columnComment;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 字段类型
     */
    private String columnType;

    /**
     * 键值类型
     */
    private String columnKey;

    /**
     * 是否允许为空
     */
    private String isNullable;

}