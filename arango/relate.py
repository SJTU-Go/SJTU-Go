# coding=utf-8
from pyArango.connection import *
import pyArango
import xml.etree.ElementTree as ET
conn = Connection(arangoURL='http://47.92.147.237:8529', username="root", password="")
db = conn["_system"]
tree = ET.parse('highway2.xml')
root = tree.findall('node')
c1 = db['vertex']
c2 = db['edge']
import time
import datetime
import json
import pymysql
import sys

con = pymysql.connect(host='47.94.90.204', user='pguser', passwd='pguser', db='playground',charset='utf8')
cur = con.cursor()
count=0
for i in c1.fetchAll():
    id = int((i['_key']))
    locat = ['1','12']
    locat[0],locat[1]=str(i['location'][0]),str(i['location'][1])
    loc = tuple(locat)
    #print (i['isPark'])
    count += 1
    print('No.', count,loc)
    if count>4000:
        cur.execute("UPDATE map_vertex_info SET location = POINT%s where vertexid = '%s';" % (loc,id))
        con.commit()
        '''if (i['isPark']):
            name=i['name']
            print (name)
            cur.execute("INSERT INTO map_vertex_info(vertexid,location,vertex_name,bike_count,motor_count)  VALUES ('%s', POINT%s,'%s',0,0) ;" % (id, loc, name))
            con.commit()
        else:
            pass
            cur.execute("INSERT INTO map_vertex_info(vertexid,location,bike_count,motor_count)  VALUES ('%s', POINT%s,0,0);" % (id, loc))
            con.commit()'''

cur.close()
