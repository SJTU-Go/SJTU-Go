 #-*-coding:utf-8-*-

from xml.dom.minidom import parse
import xml.dom.minidom
import pymysql
from coordTransform.coordTransform_utils import wgs84_to_gcj02


def transform(lon,lat):
    gcj_lon, gcj_lat = wgs84_to_gcj02(lon,lat)
    return (gcj_lon-121.449652+121.444577,gcj_lat-31.025940+31.024322)

conn = pymysql.connect( host='ltzhou.com',
                        port=3306,
                        user='pguser',
                        passwd='pguser',
                        db = 'playground',
                        charset = 'utf8')
cursor = conn.cursor()


DOMTree = xml.dom.minidom.parse("highway.osm")
map_data = DOMTree.documentElement
map_data = map_data.getElementsByTagName('node')

for node in map_data:
    node_tags = node.getElementsByTagName('tag')
    if node_tags:
        for tag in node_tags:
            if tag.getAttribute('k') == 'parking_spot':
                PointID = node.getAttribute('id')
                lat = node.getAttribute('lat')
                lon = node.getAttribute('lon')
                lon,lat = transform(float(lon),float(lat))
                Name = tag.getAttribute('v')
                cursor.execute("INSERT INTO parkingSpot (PointID,parkingSpotName,latitude,longitude) \
                        VALUES (%s, %s, %s, %s)",(PointID,Name,lat,lon))
                conn.commit()



