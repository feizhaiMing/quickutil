package com.feizhai.qku.help.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashSet;
import java.util.List;

/**
 * @author wxm
 * @date 2020/6/10.
 */
@Mapper
public interface ColumnsMapper {

    List getAllColumns(@Param("tableName") String tableName,@Param("dbName")String dbName);

    String getTableInfo(@Param("tableName") String tableName,@Param("dbName")String dbName);

    HashSet<String> getAllTables(@Param("dbName")String dbName);
}
