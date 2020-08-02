 #-*-coding:utf-8-*-

import os
import time

import json
import requests
import pymysql
import config

from crawler_e100 import crawler_e100,write_db_e100
from crawler_hello import crawler_hello



def print_ts(message):
    print("[%s] %s"%(time.strftime("%Y-%m-%d %H:%M:%S", time.localtime()), message))

def update():
    conn = pymysql.connect( host=config.host,
                        port=3306,
                        user=config.user,
                        passwd=config.passwd,
                        db = config.db,
                        charset = 'utf8')
    cursor = conn.cursor()
    cursor.execute("SET SQL_SAFE_UPDATES = 0")
    cursor.execute("UPDATE map_vertex_info SET bike_count = 0")
    cursor.execute("SET SQL_SAFE_UPDATES = 1")
    cursor.execute("UPDATE map_vertex_info \
   INNER JOIN (SELECT cluster_point, count(*) AS bikecnt FROM hello_bike_info \
                WHERE TIMESTAMPDIFF(SECOND,time,current_timestamp) < 300 \
				GROUP BY cluster_point) hello_count \
   ON hello_count.cluster_point = map_vertex_info.vertexid \
SET map_vertex_info.bike_count = hello_count.bikecnt")

def run(interval):
    print_ts("-"*100)
    # print_ts("Command %s"%command)
    print_ts("Starting every %s seconds."%interval)
    print_ts("-"*100)
    while True:
        # sleep for the remaining seconds of interval
        time_remaining = interval-time.time()%interval
        print_ts("Sleeping until %s (%s seconds)..."%((time.ctime(time.time()+time_remaining)), time_remaining))
        time.sleep(time_remaining)
        # execute the command
        print_ts("Updating.")
        try:
            update()
        except:
            print_ts("Update Fails")

        print_ts("-"*100)


if __name__=="__main__":
    requests.packages.urllib3.disable_warnings()
    interval = 30
    run(interval)


