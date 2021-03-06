 #-*-coding:utf-8-*-

import os
import time

import json
import requests
import pymysql
import config

from crawler_jindouyun import crawler_jindouyun


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
        # try:
        crawler_jindouyun()
        # except:
            # print_ts("hello crawler error")

        print_ts("-"*100)


if __name__=="__main__":
    conn = pymysql.connect( host=config.host,
                        port=3306,
                        user=config.user,
                        passwd=config.passwd,
                        db = config.db,
                        charset = 'utf8')
    cursor = conn.cursor()

    requests.packages.urllib3.disable_warnings()
    interval = 240
    run(interval)


