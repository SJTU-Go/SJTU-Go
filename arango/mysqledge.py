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
for i in c2.fetchAll():
    count += 1


    print('No.', count)
    if count > 4000:
        id = ((i['_key']))
        fromv=c1[i['_from'][7:]]#['location']
        tov=c1[i['_to'][7:]]#['location']
        length=int(i['distance'])
        type=2
        if i['type']=='residential':
            type-=2
        elif i['type']=='service':
            type-=1
        locat1 = ['1','12']
        locat1[0],locat1[1]=str(fromv['location'][1]),str(fromv['location'][0])
        locat2 = ['1', '12']
        locat2[0], locat2[1] = str(tov['location'][1]), str(tov['location'][0])
        locat1=tuple(locat1)
        locat2=tuple(locat2)
        locat=[]
        locat.append(locat1)
        locat.append(locat2)
        loc = tuple(locat)



        name=i['name']
        cur.execute("INSERT INTO map_edge_info(edgeid,edgelength,edgeline,edgetype)  VALUES ('%s','%s', ST_GeomFromText('LINESTRING(%s %s,%s %s)'),'%s');" % (id, length, locat1[0],locat1[1],locat2[0],locat2[1],type))
        con.commit()


cur.close()
