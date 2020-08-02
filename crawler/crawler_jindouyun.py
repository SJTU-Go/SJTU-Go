 #-*-coding:utf-8-*-

import json
import requests
import pymysql
import config

from calculate_cluster import calculate_cluster

from coordTransform.coordTransform_utils import bd09_to_gcj02

def print_ts(message):
    print("[%s] %s"%(time.strftime("%Y-%m-%d %H:%M:%S", time.localtime()), message))

# 爬取筋斗云车辆信息
def jindouyun_nearby(lat,lon):
    url = 'https://wx-xcx-3.cangowin.com/app/v1/bike/nearby?bikeType=677fa5b5-b9d2-44bf-8f2d-56af8fb29082&campus=b38b0efc-7d97-4e16-bf65-71d976bc2468&lat={}&lng={}'.format(str(lat),str(lon))

    headers = {
        "client_brand": "6cc88364-4b32-4ba7-96a2-3ce9e84d6358",
        "client_type": "iOS",
        "app_version": "1.0.0",
        "client_app_id": "wx2e5fce1445c706eb",
        "Content-Type": "application/x-www-form-urlencoded",
        "Accept-Language": "zh-Hans-CN;q=1.0",
        "Accept-Encoding": "gzip;q=1.0, compress;q=0.5",
        "Accept": "*/*",
        "Connection": "keep-alive"
        }
    req = requests.get(url,json=headers,verify=False)
    data = json.loads(req.text)['data']
    return data

def write_db_jindouyun(bike):
    conn = pymysql.connect( host=config.host,
                        port=3306,
                        user=config.user,
                        passwd=config.passwd,
                        db = config.db,
                        charset = 'utf8')
    cursor = conn.cursor()
    lat = bike['bikeGpsDO']['lat']
    lon = bike['bikeGpsDO']['lng']
    cluster_point = calculate_cluster(lat,lon)
    bike_id = bike['bikeDO']['bikeCode']
    bike_power = bike['bikePowerDO']['power']
    bike_mile = bike['bikePowerDO']['mileage']
    cursor.execute("REPLACE INTO jindouyun_info (bike_id,latitude,longitude,power,mileage,cluster_point) \
                    VALUES (%s, %s, %s, %s, %s, %s)",(bike_id,lat,lon,bike_power,bike_mile,cluster_point))
    conn.commit()
    return 1

def crawler_jindouyun():
    bl_lat = 31.0163088100
    bl_lon = 121.4278078100
    br_lat = 31.045899000
    br_lon = 121.4675410000
    lat_block = 5
    lon_block = 8
    x_lat = (br_lat - bl_lat) / lat_block
    x_lon = (br_lon - bl_lon) / lon_block
    cnt = 0
    for i in range(lat_block):
        for j in range(lon_block):
            data = jindouyun_nearby(bl_lat + i*x_lat, bl_lon + j*x_lon)
            cnt += len(data)
            for bike in data:
                write_db_jindouyun(bike)
    print_ts(cnt)



if __name__ == '__main__':
    conn = pymysql.connect( host=config.host,
                        port=3306,
                        user=config.user,
                        passwd=config.passwd,
                        db = config.db,
                        charset = 'utf8')
    cursor = conn.cursor()
    bl_lat = 31.0163088100
    bl_lon = 121.4278078100
    br_lat = 31.045899000
    br_lon = 121.4675410000
    lat_block = 5
    lon_block = 7
    x_lat = (br_lat - bl_lat) / lat_block
    x_lon = (br_lon - bl_lon) / lon_block

    for i in range(lat_block):
        for j in range(lon_block):
            data = jindouyun_nearby(bl_lat + i*x_lat, bl_lon + j*x_lon)
            for bike in data:
                write_db_jindouyun(bike)

    cluster_sql = 'SELECT cluster_point,count(*) num FROM jindouyun_info GROUP BY cluster_point'
    cursor.execute(cluster_sql)
    result = cursor.fetchall()
    print(result)
