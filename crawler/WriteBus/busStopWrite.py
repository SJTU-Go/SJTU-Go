from xml.etree import ElementTree as ET

def parseLineString(vlis):
	result = "LINESTRING ("
	for v in vlis:
		result += v[1]
		result += " "
		result += v[0]
		result += ","
	result = result[:-1] + ")"
	return result

def parsePoint(v):
	return "POINT (" + v[1] + " " + v[0]+")"


# 收集逆时针bus路线
tree = ET.parse('crawler/WriteBus/counterbus.gpx')
root = tree.getroot()

counter_placelist = []
pointlist = []
tmplist = []

for node in root.find('trk').find('trkseg').findall('trkpt'):
	pointlist.append((node.attrib['lat'],node.attrib['lon']))
	if (node.find('name') != None):
		if (len(counter_placelist)==0):
			tmplist = pointlist.copy()
			counter_placelist.append([node.find('name').text, [], (node.attrib['lat'],node.attrib['lon'])])
			pointlist = [(node.attrib['lat'],node.attrib['lon'])]
		else:
			counter_placelist[len(counter_placelist)-1][1] = pointlist
			counter_placelist.append([node.find('name').text, [], (node.attrib['lat'],node.attrib['lon'])])
			pointlist = [(node.attrib['lat'],node.attrib['lon'])]
			# print (node.find('name').text)
counter_placelist[len(counter_placelist)-1][1] =pointlist.copy() + tmplist.copy()


# 收集顺时针bus路线
tree = ET.parse('crawler/WriteBus/clockbus.gpx')
root = tree.getroot()

clock_placelist = []
pointlist = []
tmplist = []

for node in root.find('trk').find('trkseg').findall('trkpt'):
	pointlist.append((node.attrib['lat'],node.attrib['lon']))
	if (node.find('name') != None):
		if (len(clock_placelist)==0):
			tmplist = pointlist.copy()
			clock_placelist.append([node.find('name').text, [],(node.attrib['lat'],node.attrib['lon']) ])
			pointlist = [(node.attrib['lat'],node.attrib['lon'])]
		else:
			clock_placelist[len(clock_placelist)-1][1] = pointlist
			clock_placelist.append([node.find('name').text, [], (node.attrib['lat'],node.attrib['lon'])])
			pointlist = [(node.attrib['lat'],node.attrib['lon'])]
			# print (node.find('name').text)
clock_placelist[len(clock_placelist)-1][1] =pointlist.copy() + tmplist.copy()


# 收集逆时针站台坐标
loctree = ET.parse('crawler/WriteBus/bus.osm')
locroot = loctree.getroot()
points = locroot.findall("./node")
points_map = {}
for point in points:
	points_map[point.attrib['id']] = point

platforms = locroot.findall("./relation[@id = '-99754']/member[@role='platform']") # counterclockwise
platform_loc_counter = {}
for platform in platforms:
	platform_info = points_map[platform.attrib['ref']]
	platform_loc_counter[platform_info.find("./tag[@k='name']").attrib['v']] = (platform_info.attrib['lat'], platform_info.attrib['lon'])

# 收集顺时针站台坐标
platforms = locroot.findall("./relation[@id = '-99752']/member[@role='platform']") # clockwise
platform_loc_clock = {}
for platform in platforms:
	platform_info = points_map[platform.attrib['ref']]
	platform_loc_clock[platform_info.find("./tag[@k='name']").attrib['v']] = (platform_info.attrib['lat'], platform_info.attrib['lon'])


import json
timejs = open('crawler/WriteBus/busTime.json','r',encoding='utf-8')
timetable = json.load(timejs)
timejs.close()
timetable = list(timetable['stopTime'].values())

import pymysql

conn = pymysql.connect( host='ltzhou.com',
                        port=3306,
                        user='pguser',
                        passwd='pguser',
                        db = 'playground',
                        charset = 'utf8')
crusor = conn.cursor()

crusor.execute('DROP TABLE IF EXISTS `bus_stop`')
crusor.execute('CREATE TABLE `bus_stop`( \
               `stopid` INT(11) NOT NULL,\
			     `location_platform` POINT NOT NULL, \
			     `location_stop` POINT NOT NULL, \
			     `stop_name` VARCHAR(255) NOT NULL, \
			     `next_route` LINESTRING NULL, \
				 `diff` INT(11) NOT NULL, \
               PRIMARY KEY (`stopid`))\
               ENGINE = InnoDB,\
               DEFAULT CHARACTER SET = utf8mb4')
conn.commit()

# 逆时针：index为正，从19-菁菁堂出发，到达1-菁菁堂
(stop,line,stoploc) = counter_placelist[0]
crusor.execute('INSERT INTO `bus_stop`\
				(stopid, location_platform, \
					location_stop, stop_name, next_route,\
					diff) \
				VALUES (%s, ST_GeomFromText(%s),\
					ST_GeomFromText(%s), %s,\
					NULL, %s)',
				(str(1), parsePoint(platform_loc_counter[stop]),
					parsePoint(stoploc), stop, str(18)))
conn.commit()

index = 19
for (stop,line,stoploc) in counter_placelist:
	# print (parsePoint(platform_loc_counter[stop]))
	# print (parsePoint(stoploc))
	# print (parseLineString(line))
	crusor.execute('INSERT INTO `bus_stop`\
					(stopid, location_platform, \
						location_stop, stop_name, next_route,\
						diff) \
					VALUES (%s, ST_GeomFromText(%s),\
						ST_GeomFromText(%s), %s,\
						ST_GeomFromText(%s), %s)',
		(str(index), parsePoint(platform_loc_counter[stop]),
		  parsePoint(stoploc), stop, parseLineString(line),str(timetable[20-index]["direct1_diff"])))
	conn.commit()
	index -= 1


# 顺时针，index为负，从菁菁堂（-1）出发，到达菁菁堂（-19）
(stop,line,stoploc) = clock_placelist[0]
crusor.execute('INSERT INTO `bus_stop`\
				(stopid, location_platform, \
					location_stop, stop_name, next_route,\
					diff) \
				VALUES (%s, ST_GeomFromText(%s),\
					ST_GeomFromText(%s), %s,\
					NULL, %s)',
				(str(-19), parsePoint(platform_loc_counter[stop]),
					parsePoint(stoploc), stop,str(18)))
conn.commit()

index = -1
for (stop,line,stoploc) in clock_placelist:
	# print (parsePoint(platform_loc_counter[stop]))
	# print (parsePoint(stoploc))
	# print (parseLineString(line))
	crusor.execute('INSERT INTO `bus_stop`\
					(stopid, location_platform, \
						location_stop, stop_name, next_route,\
						diff) \
					VALUES (%s, ST_GeomFromText(%s),\
						ST_GeomFromText(%s), %s,\
						ST_GeomFromText(%s), %s)',
		(str(index), parsePoint(platform_loc_counter[stop]),
		  parsePoint(stoploc), stop, parseLineString(line),str(timetable[18+index]["direct2_diff"])))
	conn.commit()
	index -= 1