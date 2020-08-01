# -*- coding: UTF-8 -*-

import pymysql
import time
import os
from apscheduler.schedulers.blocking import BlockingScheduler
from datetime import datetime


def get_info(db, sql):
    try:
        cursor = db.cursor()
        cursor.execute(sql)
        if sql[0:6].lower() == "select":
            results = cursor.fetchall()
            #            print(results)
            return results
        db.commit()
        db.close()

    except:
        db.ping()
        cursor = db.cursor()
        cursor.execute(sql)
        if sql[0:6].lower() == "select":
            results = cursor.fetchall()
            #            print(results)
            return results
        db.commit()
        db.close()


db = pymysql.connect(
    host="47.94.90.204",
    user="pguser",
    password="pguser",
    database="playground",
    charset="utf8"
)

# 得到4分钟内开始的行程，这个时候对应结束停车点热度要+5
sql1 = "select strategy from playground.trip where depart_time > DATE_SUB(NOW(),INTERVAL 4 MINUTE);"

# 得到4分钟内结束的行程，这个时候开始的停车点热度要+5
sql2 = "select strategy from playground.trip where arrive_time > DATE_SUB(NOW(),INTERVAL 4 MINUTE);"

# 得到四分钟内搜索情况
sql3 = "select search_names from playground.search_history where search_time > DATE_SUB(NOW(),INTERVAL 4 MINUTE);"


def get_popularity():
    strategy_d = get_info(db, sql1)  # 由开始时间得到strategy
    strategy_a = get_info(db, sql2)  # 由结束时间得到strategy
    search_h = get_info(db, sql3)  # 得到search_history

    for s in strategy_d:    # s是tuple类型
        depart = s[0].split(",")[1]
        depart_p = depart[10:-1]
        #print(depart_p)
        
        sql5 = "update playground.map_vertex_info set popularity = popularity+5 where vertex_name = '{}' and vertexid >0".format(depart_p)
        get_info(db, sql5)

    for s in strategy_a:
        arrive = s[0].split(",")[2]
        arrive_p = arrive[10:-1]

        sql5 = "update playground.map_vertex_info set popularity = popularity+5 where vertex_name = '{}' and vertexid >0".format(arrive_p)
        get_info(db, sql5)

    for s in search_h:  # s是搜索开始地点和结束地点的结合，由，隔开
        begin = s[0].split(" ")[0]  # ？？前端传入的不一定是地点名？？
        arrive = s[0].split(" ")[1]

        sql4 = "update playground.map_vertex_info set popularity = popularity+1 where vertex_name = '{}' and vertexid >0".format(begin)
        get_info(db, sql4)
        sql5 = "update playground.map_vertex_info set popularity = popularity+1 where vertex_name = '{}' and vertexid >0".format(arrive)
        get_info(db, sql5)
    print("最近更新时间："+str(datetime.now()))


# 每1分钟执行一次
if __name__ == '__main__':
    scheduler = BlockingScheduler()
    scheduler.add_job(get_popularity, 'interval', minutes=1, next_run_time=datetime.now())
    # 还可以设置开始结束时间(..., start_date='2017-9-8 21:30:00', end_date='2018-06-15 21:30:00')
    print('Press Ctrl+{0} to exit'.format('Break' if os.name == 'nt' else 'C'))  # os.name返回操作系统类型
    try:
        scheduler.start()  # 采用的是阻塞的方式，只有一个线程专职做调度的任务
    #        time.sleep(60)
    except KeyboardInterrupt:
        scheduler.shutdown()
        print("you pressed Ctrl+C")
