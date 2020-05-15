## 写入公交数据

### 数据来源

数据来源：JOSM -> 导出关系的gpx文件 -> Python xml库读取获得路径坐标信息
JOSM -> 导出osm文件 -> Python xml库读取获得站名、站台名
GitHub [SJTU-Bus项目](https://github.com/zry656565/SJTU-Bus) 获得json格式交大校园巴士时刻表

### 文件说明

busStopWrite在项目根目录下运行，可以完成bus_stop表的创建
```
SJTU-Go> python /crawler/WriteBus/busStopWrite.py
```


- id：逆时针为正，顺时针为负，绝对值相等的id对应同一个站台。若`a<b`且`a,b`同号，则表明可以由`b`通过对应的线路到达`a`。特别的，菁菁堂作为始发站id为19和-1，作为终点站id为-19和1.
- location_platform: 站台位置，精确位置在路的两边
- location_stop：站点位置，精确位置在路上
- stop_name
- next_route: Linestring类型，两头端点是当前location_stop和下一站的location_stop，中间是所有转折点坐标。对终点站该字段为NULL。
- diff：表明以菁菁堂始发站时刻表为标准，对应班次时间差。

busTimeWrite在项目根目录下运行，可以完成bus_time_weekday表和bus_time_vacation表的创建
```
SJTU-Go> python /crawler/WriteBus/busTimeWrite.py
```
- busid: 班次id，逆时针为正，顺时针为负
- bus_time: 时间类型，表明菁菁堂站始发时刻


> 注意：运行脚本会自动drop原有对应名称的数据表。