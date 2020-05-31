 #-*-coding:utf-8-*-

import pymysql

def calculate_cluster(lat,lon):
    conn = pymysql.connect( host='ltzhou.com',
                        port=3306,
                        user='pguser',
                        passwd='pguser',
                        db = 'playground',
                        charset = 'utf8')

    cursor = conn.cursor()

    sql = 'select * from parkingSpot where latitude > %s-0.001 and latitude < %s+0.001 and longitude > %s-0.001 and longitude < %s+0.001 order \
            by ACOS(SIN((%s*3.1415)/180)*SIN((latitude*3.1415)/180)+COS((%s*3.1415)/180)*COS((latitude*3.1415)/180)*COS((%s*3.1415)/180-(longitude*3.1415)/180))*6380 asc limit 1'

    cursor.execute(sql,(lat,lat,lon,lon,lat,lat,lon))
    result = cursor.fetchall()
    if len(result):
        return result[0][0]
    else:
        return 0

def calculate_car_cluster(lat,lon):
    conn = pymysql.connect( host='ltzhou.com',
                        port=3306,
                        user='pguser',
                        passwd='pguser',
                        db = 'playground',
                        charset = 'utf8')

    cursor = conn.cursor()

    sql = 'select * from (\
            SELECT ST_LENGTH(LINESTRING (location, POINT(%(lng)s, %(lat)s))) \
            AS distance, location, vertexid, bike_count, motor_count, park_info, park_size, vertex_name, is_car_vertex FROM map_vertex_info WHERE \
            MBRContains (ST_GeomFromText (CONCAT( \
              \"LINESTRING(\", %(lng)s - 0.05, \" \", %(lat)s - 0.05, \",\", \
              %(lng)s + 0.05, \" \", %(lat)s + 0.05, \")\" ) \
                ) ,location) AND is_car_vertex = 1) AS vertex_infos \
            ORDER BY vertex_infos.distance LIMIT 1' %{'lng':lon,'lat':lat}

    cursor.execute(sql)
    result = cursor.fetchall()
    if len(result):
        return result[0][2]
    else:
        return 0


if __name__=="__main__":
    lat = 31.022358
    lon = 121.428066
    cluster_point = calculate_car_cluster(lat,lon)
    print(cluster_point)


