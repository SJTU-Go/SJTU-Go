# SJTU-Go
EI333 Group Project 交大校园共享出行导航服务端

## 项目环境

IntelliJ IDEA 2020.1
AdoptOpenJDK 11(LTS)
相关依赖包可通过pom.xml文件导入

## 目录说明
```
SJTU-Go
│  .gitignore
│  LICENSE
│  mvnw
│  mvnw.cmd
│  pom.xml     // 包管理配置文件
│  README.md
│  SJTU-Go.iml
│
├─arango      // ArangoDB和MySQL数据库中地图信息的导入脚本
│      addedge.py       // 向ArangoDB图数据库导入地图边和建筑物信息
│      addvertex.py     // 向ArangoDB图数据库导入地图节点
│      changeedge.py    // 根据基础图在ArangoDB中建立不同子地图
│      mysqledge.py     // 在MySQL数据库里导入边信息
│      mysqlrelation.py // 在MySQL数据库中导入建筑物信息，并建立建筑和最近停车点的关系表
│      mysqlvertex.py   // 在MySQL数据库里导入节点信息
│
├─crawler    // 交通信息导入
│  │  crawler_script_e100.py   // 持续运行更新共享汽车数据库
│  │  crawler_script_hello.py  // 持续运行更新哈罗单车数据库
│  │
│  │
│  └─WriteBus                  // 写入校园巴士位置、时刻表
│          busStopWrite.py     // MySQL写入校园巴士位置脚本
│          busTimeWrite.py     // MySQL写入校园巴士时刻表脚本
│
└─src
    ├─main.java.org.sjtugo.api
    │  │   │  ApiApplication.java
    │  │   ├─config  // 配置类
    │  │   ├─controller  // 请求控制类
    │  │   ├─DAO      // 数据服务接口
    │  │   ├─entity   // 实体类
    │  │   └─service   // 控制类
    │  └─resources
    │      │  api.jks
    │      └─ application.properties // 配置文件
    └─test.java.org.sjtugo.api               // 测试代码
          ApiApplicationTests.java
```
