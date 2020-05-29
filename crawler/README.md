# Server

本目录中的文件和代码将会部署在服务器

## 目录说明
```
|- crawler_script.py 
|- calculate_cluster.py 计算附近停车点
|- crawler_e100.py e100爬虫
|- crawler_hello.py 哈罗单车爬虫
```

## MySQL Script
```
CREATE TABLE `hellobike`.`0326alltogether` (
  `bikeID` VARCHAR(15) NOT NULL,
  `lat` DECIMAL(18,15) NOT NULL,
  `lng` DECIMAL(18,15) NOT NULL,
  `price` VARCHAR(15) NOT NULL,
  `time` VARCHAR(45) NOT NULL,
  `bikeType` INT NOT NULL,
  `bikeColor` INT NOT NULL,
  `record` TIMESTAMP NOT NULL,
  PRIMARY KEY (`bikeID`));
```

## 取点说明
```
   lat           long
TR 31.0349908800,121.4459288100
BL 31.0163088100,121.4278078100
BR 31.0237195600,121.4505744000

从Bottom-Left出发,定义不大不小的x,y一步
right step 31.0170352000,121.4293634900
up step 31.0177248000,121.4273250100

经度上分格: (121.4505744000 - 121.4278078100)/(121.4293634900 - 121.4278078100) = 14.634494240495009 15格
纬度上分格: (31.0349908800 - 31.0237195600)/(31.0177248000 - 31.0163088100) = 7.960027966308737 8格
```

全部爬取一次约3分钟.