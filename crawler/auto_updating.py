 #-*-coding:utf-8-*-

import json
import requests
import pymysql
import datetime
#通过datatime计算时间差，将5min内的记入总和；并删去30min前的。
#定义函数如下：
#delete_data：提取30min前的数据段，并删去
#add_data:遍历所有表中的项，全部执行：提取5min内的数据点，将它们的punishment项求和；如果punishment足够大，那么调用modify函数。
#这俩可以合并
#modify：调用api并完成修改。
#SELECT timediff(arrive_time,depart_time)FROM playground.trip;可以实现两个时间段做差
#now()可以直接获得当前时间
#所以运算为：SELECT timediff(now(),time)FROM playground.punishment;
#将上述结果与5/30作比较，决定是否提取/保留对应信息。
#提取方式：直接相加。
#保留方式：将不符合条件的删除
#modify操作，参考api
#最后封装打点
def read(data):
    datetime.datetime.strptime(t_str, "%Y-%m-%d %H:%M")
    conn = pymysql.connect( host='ltzhou.com',
                    port=3306,
                    user='pguser',
                    passwd='pguser',
                    db = 'playground',
                    charset = 'utf8')
    cursor = conn.cursor()
    clusters = dict()



if __name__=="__main__":
    requests.packages.urllib3.disable_warnings()
    conn = pymysql.connect( host='ltzhou.com',
                        port=3306,
                        user='pguser',
                        passwd='pguser',
                        db = 'playground',
                        charset = 'utf8')
    cursor = conn.cursor()
    print(datetime.datetime.now())