import json
timejs = open('crawler/WriteBus/busTime.json','r',encoding='utf-8')
timetable = json.load(timejs)
timejs.close()
timetable = timetable['startTime']
# print (timetable)



import pymysql

conn = pymysql.connect( host='ltzhou.com',
                        port=3306,
                        user='pguser',
                        passwd='pguser',
                        db = 'playground',
                        charset = 'utf8')
crusor = conn.cursor()

# def writeTime(timelis,conn,tablename,is_clock):
# 	crusor = conn.cursor()
# 	for i in range(len(timelis)//2):
# 		crusor.execute('INSERT INTO {0} (busid, bus_time) VALUES ({1}, MAKETIME({2},{3},0))'.format(tablename, ( -1 if is_clock else 1) * (i+1), timelis[2*i], timelis[2*i+1], 0))
# 	conn.commit()
# 	return



crusor.execute('DROP TABLE IF EXISTS `bus_time_weekday`')
crusor.execute('DROP TABLE IF EXISTS `bus_time_vacation`')
crusor.execute('DROP TABLE IF EXISTS `bus_time`')
crusor.execute('CREATE TABLE `bus_time`( \
               `busid` INT(11) NOT NULL,\
			         `bus_time` TIME NOT NULL, \
               `bus_type` INT(11) NOT NULL, \
               PRIMARY KEY (`busid`))\
               ENGINE = InnoDB,\
               DEFAULT CHARACTER SET = utf8mb4')
conn.commit()

idx_cnt = 1

counterclock_time = timetable['direct1']
for i in range(len(counterclock_time)//2):
	crusor.execute('INSERT INTO `bus_time` (busid, bus_time, bus_type) VALUES ({0}, MAKETIME({1},{2},0), {3})'.format(idx_cnt, counterclock_time[2*i], counterclock_time[2*i+1], 1))
	idx_cnt += 1
conn.commit()


clock_time = timetable['direct2']
for i in range(len(clock_time)//2):
	crusor.execute('INSERT INTO `bus_time` (busid, bus_time, bus_type) VALUES ({0}, MAKETIME({1},{2},0), {3})'.format(idx_cnt, clock_time[2*i], clock_time[2*i+1], -1))
	idx_cnt += 1
conn.commit()


counterclock_time = timetable['festival_direct1']
for i in range(len(counterclock_time)//2):
	crusor.execute('INSERT INTO `bus_time` (busid, bus_time, bus_type) VALUES ({0}, MAKETIME({1},{2},0), {3})'.format(idx_cnt, counterclock_time[2*i], counterclock_time[2*i+1], 2))
	idx_cnt += 1
conn.commit()

clock_time = timetable['festival_direct2']
for i in range(len(clock_time)//2):
	crusor.execute('INSERT INTO `bus_time` (busid, bus_time, bus_type) VALUES ({0}, MAKETIME({1},{2},0), {3})'.format(idx_cnt, clock_time[2*i], clock_time[2*i+1], -2))
	idx_cnt += 1
conn.commit()
