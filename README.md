# quickutil
开发辅助工具

## db-md-help
### 原理
    通过读取information_schema库, 加载出表名,注释,及字段信息;将这些信息拼接成固定字符串.写入文件中.
    
### 使用方法
    1.在application.properties文件中设置号数据库连接信息, 需要有information_schema库的读权限
    2.在Application.java文件中找到init方法,配置初始化参数,包括生成的文件名,需要生成的数据库和表
    3.运行Application.java.