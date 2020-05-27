import pymysql

conn = pymysql.connect( host='ltzhou.com',
                        port=3306,
                        user='pguser',
                        passwd='pguser',
                        db = 'playground',
                        charset = 'utf8')

cursor = conn.cursor()

# 创建table: E100Info
# cursor.execute('CREATE TABLE `E100Info`(\
#                 `CarID` VARCHAR(10) NOT NULL,\
#                 `CarPlate` VARCHAR(30) NOT NULL,\
#                 `latitude` FLOAT(9,6) NOT NULL,\
#                 `longitude` FLOAT(9,6) NOT NULL,\
#                 `time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\
#                 `cluster_point` VARCHAR(15) NOT NULL,\
#                 PRIMARY KEY (`CarID`),\
#                 INDEX `ID` USING BTREE (`CarID`))\
#                 ENGINE = InnoDB,\
#                 DEFAULT CHARACTER SET = utf8mb4')

# # 创建table: parkingSpot
# cursor.execute('CREATE TABLE `parkingSpot`(\
#                 `PointID` VARCHAR(15) NOT NULL,\
#                 `parkingSpotName` VARCHAR(40) NULL,\
#                 `latitude` FLOAT(10,7) NULL,\
#                 `longitude` FLOAT(10,7) NULL,\
#                 PRIMARY KEY (`PointID`),\
#                 INDEX `ID` USING BTREE (`PointID`))\
#                 ENGINE = InnoDB,\
#                 DEFAULT CHARACTER SET = utf8mb4')

# # 创建table: helloBikeInfo
# cursor.execute('CREATE TABLE `helloBikeInfo`(\
#                 `BikeID` VARCHAR(15) NOT NULL,\
#                 `latitude` FLOAT(18,15) NOT NULL,\
#                 `longitude` FLOAT(18,15) NOT NULL,\
#                 `bikeType` INT NOT NULL,\
#                 `bikeColor` INT NOT NULL,\
#                 `time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\
#                 `cluster_point` VARCHAR(15) NOT NULL,\
#                 PRIMARY KEY (`BikeID`),\
#                 INDEX `ID` USING BTREE (`BikeID`))\
#                 ENGINE = InnoDB,\
#                 DEFAULT CHARACTER SET = utf8mb4')

# 创建table: helloBikeInfo
# cursor.execute('CREATE TABLE `feedback`(\
#                 `feedbackID` INT NOT NULL,\
#                 `userID` INT NOT NULL,\
#                 `tripID` INT NOT NULL,\
#                 `pickupFB` INT,\
#                 `trafficFB` INT,\
#                 `parkFB` INT,\
#                 `serviceFB` INT,\
#                 `contents` VARCHAR(255),\
#                 `time` DATETIME NOT NULL,\
#                 PRIMARY KEY (`feedbackID`),\
#                 INDEX `ID` USING BTREE (`feedbackID`))\
#                 DEFAULT CHARACTER SET = utf8mb4')

# cursor.execute('ALTER TABLE user drop column preference')

# cursor.execute('ALTER TABLE user add column preference JSON')

cursor.execute('ALTER TABLE user add session_key VARCHAR(100) NOT NULL')



conn.commit()