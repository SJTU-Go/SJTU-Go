 #-*-coding:utf-8-*-

import requests
import json
import sys
import pymysql
import time
from calculate_cluster import calculate_cluster

def print_ts(message):
    print("[%s] %s"%(time.strftime("%Y-%m-%d %H:%M:%S", time.localtime()), message))
  
def hello_nearby(lat,lon):
    url = 'https://bike.hellobike.com/api?user.ride.nearBikes'
    headers = {
          "version":"5.33.1",
          "action":"user.ride.nearBikes",
          "lat":lat,
          "lng":lon,
          "cityCode":"021",
          "currentLng":"121.334864",
          "currentLat":"31.156389",
          "adCode": "310112",
          "token": "4ed339e80dcc413483805901397df25d"
          }
    req = requests.post(url,json=headers,verify=False)
    data = json.loads(req.text)['data']
    return data


def write_db_hello(bike):
    conn = pymysql.connect( host='ltzhou.com',
                    port=3306,
                    user='pguser',
                    passwd='pguser',
                    db = 'playground',
                    charset = 'utf8')
    cursor = conn.cursor()
    lat = bike['lat']
    lon = bike['lng']
    cluster_point = calculate_cluster(lat,lon)
    cursor.execute("REPLACE INTO hello_bike_info (bike_id,latitude,longitude,bike_type,bike_color,cluster_point) \
                    VALUES (%s, %s, %s, %s, %s, %s)",(bike['bikeNo'],lat,lon,bike['bikeType'],bike['bikeColor'],cluster_point))
    conn.commit()
    return 1

def crawler_hello():
    bl_lat = 31.0163088100
    bl_lon = 121.4278078100
    br_lat = 31.045899000
    br_lon = 121.4675410000
    lat_block = 8
    lon_block = 15
    x_lat = (br_lat - bl_lat) / lat_block
    x_lon = (br_lon - bl_lon) / lon_block
    cnt = 0
    for i in range(lat_block):
        for j in range(lon_block):
            data = hello_nearby(bl_lat + i*x_lat, bl_lon + j*x_lon)
            cnt += len(data)
            for bike in data:
                write_db_hello(bike)
    print_ts(cnt)

if __name__ == '__main__':
    conn = pymysql.connect( host='ltzhou.com',
                        port=3306,
                        user='pguser',
                        passwd='pguser',
                        db = 'playground',
                        charset = 'utf8')
    cursor = conn.cursor()
    bl_lat = 31.0163088100
    bl_lon = 121.4278078100
    br_lat = 31.045899000
    br_lon = 121.4675410000
    lat_block = 8
    lon_block = 15
    x_lat = (br_lat - bl_lat) / lat_block
    x_lon = (br_lon - bl_lon) / lon_block

    for i in range(lat_block):
        for j in range(lon_block):
            data = hello_nearby(bl_lat + i*x_lat, bl_lon + j*x_lon)
            for bike in data:
                write_db_hello(bike)

    cluster_sql = 'SELECT cluster_point,count(*) num FROM hello_bike_info GROUP BY cluster_point'
    cursor.execute(cluster_sql)
    result = cursor.fetchall()
    print(result)
