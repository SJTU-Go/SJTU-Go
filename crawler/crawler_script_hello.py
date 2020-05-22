 #-*-coding:utf-8-*-

import os
import time

import json
import requests
import pymysql

from crawler_e100 import crawler_e100,write_db_e100
from crawler_hello import crawler_hello


def print_ts(message):
    print("[%s] %s"%(time.strftime("%Y-%m-%d %H:%M:%S", time.localtime()), message))
    
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
        print_ts("Starting crawler.")
        try:
            crawler_hello()
        except:
            print_ts("hello crawler error")

        print_ts("-"*100)


if __name__=="__main__":
    conn = pymysql.connect( host='ltzhou.com',
                        port=3306,
                        user='pguser',
                        passwd='pguser',
                        db = 'playground',
                        charset = 'utf8')
    cursor = conn.cursor()

    requests.packages.urllib3.disable_warnings()
    interval = 240
    run(interval)


