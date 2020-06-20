# SJTU-Go
EI333 Group Project 交大校园共享出行导航服务端

## 项目环境

IntelliJ IDEA 2020.1
AdoptOpenJDK 11(LTS)
相关依赖包可通过pom.xml文件导入

## 目录说明
服务器端代码位于SJTU-Go文件夹中，其中
- arango文件夹中的脚本运行在本地设备，用于初始化配置图数据库的数据。
- crawler文件夹中的脚本中，部分脚本运行在本地设备，用于初始化配置关系型数据库，另一部分脚本需要在服务器后台持续运行，见start-crawler.sh脚本中的说明。
- src文件夹中包含所有服务端API服务的源代码。开发环境为AdoptOpenJDK 11(LTS)，采用SpringBoot框架，相关包可以通过Maven工具根据pom.xml文件导入。建议在本地编译成jar包后部署到服务器运行。
```
SJTU-Go
│  pom.xml   // 包管理配置文件
│
├─arango  // ArangoDB和MySQL数据库中地图信息的导入脚本
│
├─crawler  // 交通信息导入
│
├─src
│  ├─main
│  │  ├─java
│  │  │  └─org
│  │  │      └─sjtugo
│  │  │          └─api
│  │  │              │  ApiApplication.java
│  │  │              │
│  │  │              ├─config            // 配置类
│  │  │              │
│  │  │              ├─controller    // 处理HTTP请求的控制类
│  │  │              │  │
│  │  │              │  ├─RequestEntity
│  │  │              │  │
│  │  │              │  └─ResponseEntity
│  │  │              │
│  │  │              ├─DAO          // 数据服务接口
│  │  │              │  │
│  │  │              │  ├─Entity
│  │  │              │  │
│  │  │              │  └─Exception
│  │  │              │
│  │  │              ├─entity   // 实体类
│  │  │              │
│  │  │              └─service   // 负责业务逻辑的控制类
│  │  │                  │
│  │  │                  ├─Exception
│  │  │                  │
│  │  │                  └─NavigateService  // 导航服务的控制类
│  │  │
│  │  └─resources
│  │      │  application.properties  // 配置文件
│  │      │
│  │      ├─static
│  │      └─templates
│  └─test
│      └─java
│          └─org
│              └─sjtugo
│                  └─api   // 单元测试代码
│
└─testcase   // 单元测试需用到的数据
```