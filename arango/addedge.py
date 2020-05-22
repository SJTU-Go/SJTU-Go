from pyArango.connection import *
import xml.etree.ElementTree as ET
from math import radians, cos, sin, asin, sqrt
def haversine(lon1, lat1, lon2, lat2):

    lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, lon2, lat2])

    dlon = lon2 - lon1
    dlat = lat2 - lat1
    a = sin(dlat/2)**2 + cos(lat1) * cos(lat2) * sin(dlon/2)**2
    c = 2 * asin(sqrt(a))
    r = 6371
    return c * r * 1000

vbike=6
vmotor=8
vcar=16
conn = Connection(arangoURL='http://47.92.147.237:8529', username="root", password="")
db = conn["_system"]
tree = ET.parse('highway2.xml')
root = tree.findall('way')
c1 = db['vertex']
c2= db['edge']
c3=db['place']
c4=db['park']
for i in range(len(root)):
    data = root[i]
    isbuild=0
    isway=0
    waytype={}
    id = data.attrib['id']
    nd=data.findall('nd')
    pp = data.findall('tag')
    if (len(pp) > 0):
        for ii in range(len(pp)):
            tag = pp[ii]
            att = tag.attrib
            if att['k'] == 'highway':
                isway = True
                waytype['type'] = att['v']
                print(att['v'])
            if att['k'] == 'name' :
                waytype['name'] = att['v']
            if att['k'] == 'building':
                isbuild =True
    if(isway):
        pass
        '''for j in range(len(nd)-1):

            n1 = data[j].attrib['ref']
            n2 = data[j+1].attrib['ref']
            print(n1,n2)
            nn1 = c1[n1[1:]]
            nn2 = c1[n2[1:]]
            a1 = nn1['location']
            a2 = nn2['location']
            lat1, lon1 = float(a1[0]), float(a1[1])
            lat2, lon2 = float(a2[0]), float(a2[1])
            distance= haversine(lon1, lat1, lon2, lat2)
            bikeTime=distance/vbike
            motorTime=distance/vmotor
            carTime=distance/vcar
            edge1 = {
                '_from': 'vertex/'+n1[1:],
                '_to': 'vertex/' + n2[1:],
                'distance': distance,
                '_key': id+'_'+str(j)+'_'+str(j+1),
                'streetID':id,
                'bikeTime':bikeTime,
                'motorTime':motorTime,
                'carTime':carTime,
                'type':waytype['type']
            }
            edge2 = {
                '_from': 'vertex/' + n2[1:],
                '_to': 'vertex/' + n1[1:],
                'distance': distance,
                '_key': id + '_' + str(j+1) + '_' + str(j ),
                'streetID': id,
                'bikeTime': bikeTime,
                'motorTime': motorTime,
                'carTime': carTime,
                'type': waytype['type']
            }
            c2.createDocument(edge1).save()
            c2.createDocument(edge2).save()'''
    if(isbuild):
        la=0
        lo=0
        if len(nd)>1:
            for j in range(len(nd)-1 ):
                n1 = data[j].attrib['ref']
                nn1 = c1[n1[1:]]
                a1 = nn1['location']
                lat1, lon1 = float(a1[0]), float(a1[1])
                la+=lat1
                lo+=lon1
            la/=(len(nd)-1)
            lo/=(len(nd)-1)
        else:
            n1 = data[0].attrib['ref']
            nn1 = c1[n1[1:]]
            a1 = nn1['location']
            la, lo = float(a1[0]), float(a1[1])
        array = []
        close=''
        cid=''
        d=1000000
        for i in c4.fetchAll():
            d1=haversine(lo, la, i['location'][1], i['location'][0])
            if d>d1:
                d=d1
                close=i['name']
        for i in c1.fetchAll():
            if i['name']==close:
                cid=i['_key']
                break

        array.append(float(la))
        array.append(float(lo))

        print(array,waytype['name'])
        nn = {
            '_key': id ,
            'isPlace': True,
            'isPark': False,
            'name': waytype['name'],
            'location': array
        }
        #c1.createDocument(nn).save()
        n1 = {
            '_key': id[1:],
            'name': waytype['name'],
            'location': array,
            'park':cid,
            'parkname':close,
            'distance':d
        }
        c3.createDocument(n1).save()