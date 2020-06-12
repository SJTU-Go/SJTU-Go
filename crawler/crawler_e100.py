 #-*-coding:utf-8-*-

import json
import requests
import pymysql

from calculate_cluster import calculate_cluster, calculate_car_cluster

from coordTransform.coordTransform_utils import bd09_to_gcj02


# 爬取旋风E100车辆信息
def crawler_e100():
    url = 'https://api.ezcarsharing.com/doraemon/cluster/4856712C981340B0A6A10B77861F2411?lon=121.40377&lat=31.080002&geodetic_system=gcj02&rectangle=121.41308566624373,31.05444286048026,121.46480872496845,30.970588273341708'

    Headers = {
        "Host":"api.ezcarsharing.com",
        "Connection":"keep-alive",
        "Accept":"application/json",
        "Origin":"https://www.icicv.sjtu.edu.cn",
        "Authorization":"Bearer 2038f9c3a5204176b7536b6218d41185",
        "User-Agent":"Mozilla/5.0 (Linux; Android 8.1.0; CLT-AL00 Build/HUAWEICLT-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/045131 Mobile Safari/537.36 MMWEBID/4274 MicroMessenger/7.0.14.1660(0x27000E37) Process/tools NetType/WIFI Language/zh_CN ABI/arm64 WeChat/arm64",
        "Referer":"https://www.icicv.sjtu.edu.cn/a3bda4/",
        "Accept-Encoding":"gzip, deflate, br",
        "Accept-Language":"zh-CN,en-US;q=0.9",
        "X-Requested-With":"com.tencent.mm",
    }
    req = requests.get(url=url,headers=Headers,verify=False)
    return json.loads(req.text)

def write_db_e100(data):
    conn = pymysql.connect( host='ltzhou.com',
                    port=3306,
                    user='pguser',
                    passwd='pguser',
                    db = 'playground',
                    charset = 'utf8')
    cursor = conn.cursor()
    clusters = dict()

    for car in data:
        title = car["title"]
        lat = car["vehicles"][0]["lat"]
        lon = car["vehicles"][0]["lon"]
        lon, lat = bd09_to_gcj02(lon,lat)
        lat = lat-31.024782+31.029231
        lon = lon-121.428711+121.439690
        CarID = title[1:9]
        cluster_point = calculate_car_cluster(lat,lon)
        cursor.execute("REPLACE INTO e100_info (car_id,car_plate,latitude,longitude,cluster_point) \
                        VALUES (%s, %s, %s, %s, %s)",(CarID,title,lat,lon,cluster_point))
        conn.commit()
    


if __name__=="__main__":
    requests.packages.urllib3.disable_warnings()

    conn = pymysql.connect( host='ltzhou.com',
                        port=3306,
                        user='pguser',
                        passwd='pguser',
                        db = 'playground',
                        charset = 'utf8')
    cursor = conn.cursor()

    data = crawler_e100()
    write_db_e100(data)

    cluster_sql = 'SELECT cluster_point,count(*) num FROM e100_info GROUP BY cluster_point'
    cursor.execute(cluster_sql)
    result = cursor.fetchall()
    print(result)