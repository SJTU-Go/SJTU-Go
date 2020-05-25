# coding=utf-8
from pyArango.connection import *
import pyArango
import xml.etree.ElementTree as ET
conn = Connection(arangoURL='http://47.92.147.237:8529', username="root", password="sjtugo")
db = conn["_system"]
tree = ET.parse('highway2.xml')
root = tree.findall('node')
c1 = db['vertex']
c2 = db['place']
import time
import datetime
import json
import pymysql
import sys

con = pymysql.connect(host='47.94.90.204', user='pguser', passwd='pguser', db='playground',charset='utf8')
cur = con.cursor()
count=0
for i in c2.fetchAll():
    id = int((i['_key']))
    locat = ['1','12']
    locat[0],locat[1]=str(i['location'][0]-0.0046454343),str(i['location'][1]+0.0019236209)
    loc = tuple(locat)
    print (loc)
    count += 1
    name = i['name']
    parkid=int(i['park'])
    v=1.4
    t=int(i['distance']/v)
    print('No.', count)
    if count>0:
        cur.execute("INSERT INTO vertex_destination(placeid,vertexid,reachtime)  VALUES ('%s', '%s', '%s');" % (id, parkid, t))
        cur.execute("UPDATE destination SET location = POINT%s WHERE placeid='%s';" % ( loc, id))
        con.commit()


cur.close()