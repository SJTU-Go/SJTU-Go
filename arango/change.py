# coding=utf-8
from pyArango.connection import *

from coordTransform_utils import wgs84_to_gcj02

from math import radians, cos, sin, asin, sqrt

def haversine(lon1, lat1, lon2, lat2):

    lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, lon2, lat2])

    dlon = lon2 - lon1
    dlat = lat2 - lat1
    a = sin(dlat/2)**2 + cos(lat1) * cos(lat2) * sin(dlon/2)**2
    c = 2 * asin(sqrt(a))
    r = 6371
    return c * r * 1000
conn = Connection(arangoURL='http://47.92.147.237:8529', username="root", password="")
db = conn["_system"]
c0=db['vertex']
c1 = db['caredge']
c2=db['distanceedge']
def transform(lon,lat):
    gcj_lon, gcj_lat = wgs84_to_gcj02(lon,lat)
    return (gcj_lon-121.449652+121.444577,gcj_lat-31.025940+31.024322)
count=0
for i in c0.fetchAll():
    count+=1
    if count>0:
        s=i['_key']
        location=i['location']
        print(location)
        new = [1.1, 2.2]
        if (location[0]<location[1]):
            doc=c0[s]
            new[0],new[1]=transform(location[1],location[0])
            doc['location'] = new
            doc.save()
            print (s,count,location,new)
'''
vbike=6
vmotor=8
vcar=16
for i in c2.fetchAll():
    count += 1
    if(count>0 and i['type']=='residential'):
        n1 = i['_from'][7:]
        v1=c0[n1]
        n2 = i['_to'][7:]
        v2=c0[n2]

        a1 = v1['location']
        a2 = v2['location']
        lat1, lon1 = float(a1[1]), float(a1[0])
        lat2, lon2 = float(a2[1]), float(a2[0])
        distance = haversine(lon1, lat1, lon2, lat2)
        bikeTime = i['distance'] / vbike
        motorTime = i['distance'] / vmotor
        carTime = i['distance'] / vcar
        edge1 = {
            '_from': i['_from'],
            '_to':  i['_to'],
            'distance': i['distance'],
            #'normalBikeTime': bikeTime,
            #'avoidBikeTime': bikeTime,
            #'normalMotorTime': motorTime,
            #'avoidMotorTime': motorTime
            'normalCarTime': carTime,
            'avoidCarTime': carTime,
            #'type': i['type']
        }
        c1.createDocument(edge1).save()
        print(i['_key'],count)'''


