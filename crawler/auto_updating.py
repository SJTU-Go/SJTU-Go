 #-*-coding:utf-8-*-

import json
import requests
import pymysql
import datetime
import pandas as pd
import numpy as np
import time


def change_to_timestamp(datime):
    return     datime.to_pydatetime().timestamp()

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
        print_ts("Updating.")
        try:
            update()
        except:
            print_ts("Update Fails")

        print_ts("-"*100)


def update():
    vertexlist = []
    time_now = datetime.datetime.now()
    now_stamp=time_now.timestamp()
    conn = pymysql.connect(
                    host='ltzhou.com',
                    port=3306,
                    user='pguser',
                    passwd='pguser',
                    db = 'playground',
                    charset = 'utf8')
    cursor = conn.cursor()
    sqlcom = 'select * from playground.punishment'
    df = pd.read_sql(sqlcom, conn)
    df1 = np.array(df)  # 先使用array()将DataFrame转换一下
    df2 = df1.tolist()  # 再将转换后的数据用tolist()转成列表
    punishment = 0
    for i in df2:
        flag = 1
        #这里表明了需要清除的时间范围 删除30000s之前的数据
        if (now_stamp-change_to_timestamp(i[0]))>30000:
            sqlcom = 'delete from punishment where punishid = %s'%(i[4])
            try:
                # 执行SQL语句
                cursor.execute(sqlcom)
                # 提交到数据库执行
                conn.commit()
            except Exception as e:
                # 发生错误时回滚
                conn.rollback()
        # 这里表明了需要统计的时间范围 300s以内
        if (now_stamp - change_to_timestamp(i[0])) < 300:

            if not len(vertexlist):
                edgesituation = {}
                edgesituation["vertexid1"]= i[1]
                edgesituation["vertexid2"] = i[2]
                edgesituation["puni"] = i[5]
                edgesituation["time"] = change_to_timestamp(i[0])
                vertexlist.append(edgesituation)
            else:
                for p in vertexlist:
                    if p["vertexid1"] == i[1]:
                        if p["vertexid2"] == i[2]:
                            p["puni"] = p["puni"] + i[5]
                            break
                            flag = 0
                if (flag):
                    edgesituation1 = {}
                    edgesituation1["vertexid1"] = i[1]
                    edgesituation1["vertexid2"] = i[2]
                    edgesituation1["puni"] = i[5]
                    edgesituation1["time"] = change_to_timestamp(i[0])
                    vertexlist.append(edgesituation1)
    print(vertexlist)
    url = 'https://api.ltzhou.com/modification/modify/traffic?adminID=0'
    header ={'content-type': 'application/json'}
    for h in vertexlist:
        if h['puni']>=3:
            timeArray = time.localtime(h["time"]+604800)
            timeArray_new = time.localtime(h["time"]+300+604800)
            data = dict()
            data["adminID"]=1
            data["beginDay"]=time.strftime("%Y/%m/%d %H:%M:%S", timeArray)[0:10]
            data["beginTime"]=time.strftime("%Y/%m/%d %H:%M:%S", timeArray)[11:16]
            #print(data["beginTime"])
            data["bikeSpeed"]=5
            data["carSpeed"] = 10
            data["motorSpeed"] = 1
            data["endTime"]=time.strftime("%Y-%m-%d %H:%M:%S", timeArray_new)[11:16]
            data["message"] = "拥堵"
            data["name"] = "拥堵"
            data["relatedVertex"] =  [h["vertexid1"],h["vertexid2"]]
            #            data["relatedVertex"] = "["+ str(h["vertexid1"] )+ "," +str(h["vertexid2"])+"]"
            data["repeatTime"] = 0
            print(data)

            response = requests.post(url, json=data,headers=header)

            # 查看响应内容，response.text 返回的是Unicode格式的数据
            print(response.text)
            print(response.content)
            print(response.url)
            print(response.encoding)
            print(response.status_code)

if __name__ == "__main__":
    requests.packages.urllib3.disable_warnings()
    run(300)
