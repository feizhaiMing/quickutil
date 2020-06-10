package com.feizhai.qku.help;

import com.feizhai.qku.help.dao.ColumnsMapper;
import com.feizhai.qku.help.entity.Columns;
import com.feizhai.qku.help.util.FileWriter;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * @author wxm
 * @date 2020/6/10.
 */
public class Application {
    private static SqlSessionFactory sqlSessionFactory;

    private static String fileName;

    private static Map<String,  HashSet<String>> params;

    /**
     * 初始化配置
     */
    public static void init() {
        // 配置文件名
        fileName = "db-md-help.md";
        // 配置数据库和表
        params = mapBuild();
        try {
            Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            reader.close();
        } catch (IOException ignore) {
            ignore.printStackTrace();
        }
    }

    /**
     * 数据库和表选择
     * @return
     */
    public static Map<String, HashSet<String>> mapBuild(){
        Map<String,  HashSet<String>> params = new HashMap<>();
        // 设置数据库
        HashSet<String> dbSet = new HashSet<String>();
        dbSet.add("zsts");
        params.put("dbName", dbSet);
        // 设置表, 不设置则加载全部的表
        HashSet<String> tables = new HashSet<>();
        // tables.add("test");
        params.put("tables", tables);
        return params;
    }

    public static void main(String[] args) {
        // 连接数据库
        init();
        FileWriter fileWriter = new FileWriter();
        fileWriter.init(fileName);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        ColumnsMapper columnsMapper = sqlSession.getMapper(ColumnsMapper.class);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        try {
            String dbName = params.get("dbName").iterator().next();
            HashSet<String> tables = params.get("tables");
            // 未设置表名,默认全部
            if(tables.size() == 0){
                tables = columnsMapper.getAllTables(dbName);
            }
            Iterator<String> iterator = tables.iterator();
            while (iterator.hasNext()){
                String tableName = iterator.next();
                String annotation = columnsMapper.getTableInfo(tableName, dbName);
                if(annotation ==null || "".equals(annotation)){
                    annotation = tableName;
                }
                String title = "## " + annotation + "\n" +
                        "\n" +
                        "### 说明\n" +
                        "\n" +
                        "| 说明     | " + annotation+ " |\n" +
                        "| -------- | ---------- |\n" +
                        "| 创建时间 | " + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "  |\n" +
                        "| 索引     |            |\n" +
                        "| 触发器   |            |\n" +
                        "\n" +
                        "### 表结构\n" +
                        "\n" +
                        "| 别名     | 列名                 |   数据类型   | 空值 | 规格 | 注释 |\n" +
                        "| -------- | :------------------- | :----------: | :--: | :--: | :--: |";
                fileWriter.lineWriter(title);

                // 获取字段信息
                List<Columns> columnList = columnsMapper.getAllColumns(tableName, dbName);
                String content = "";
                for(Columns columns: columnList){
                    content = "|" + columns.getColumnComment() + "|" + columns.getColumnName() + "|" +
                            columns.getColumnType() + "|" + columns.getIsNullable().substring(0,1) + "|";
                    if(columns.getColumnKey() != null && "".equals(columns.getColumnKey())) {
                        if("PRI".equals(columns.getColumnKey())) {
                            content += "PK|主键|";
                        }else{
                            content += "  |  |";
                        }
                    }else{
                        content += "  |  |";
                    }
                    fileWriter.lineWriter(content);
                }
                fileWriter.lineWriter("\n");
            }
        } finally {
            sqlSession.close();
            fileWriter.close();
        }
    }

}
