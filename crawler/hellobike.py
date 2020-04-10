# -*- coding: utf-8 -*-

import requests
import json
import sys
import pymysql
import time

token = "b035c3c414764f09ac9f687d288ce812",

conn = pymysql.connect( host='127.0.0.1',
                        port=3306,
                        user='root',
                        passwd='',
                        db = 'hellobike',
                        charset = 'utf8')
crusor = conn.cursor()

session = requests.session()

def hello_nearby(lat,lng):
    url = 'https://bike.hellobike.com/api?user.ride.nearBikes'
    headers = {
      "version":"5.33.1",
      "action":"user.ride.nearBikes",
      "lat":lat,
      "lng":lng,
      "cityCode":"021",
      "currentLng":"121.334864",
      "currentLat":"31.156389",
      "adCode": "310112",
      "token": "b035c3c414764f09ac9f687d288ce812", # provided manually
      }
    req = requests.post(url,json=headers)
    data = json.loads(req.text)['data']
    return data

def write_db(bike):
    values = [bike['bikeNo'], bike['lat'], bike['lng'], bike['price'][:-1],
              bike['time'][:-2],  bike['bikeType'], bike['bikeColor']]
    command = 'INSERT INTO 0326alltogether\
               VALUES (%s, %s, %s, %s, %s, %s, %s, CURRENT_TIMESTAMP)'
    crusor.execute(command, values)
    conn.commit()
    return 1


if __name__ == '__main__':
    tr_lat = 31.0349908800
    tr_lng = 121.4459288100
    bl_lat = 31.0163088100
    bl_lng = 121.4278078100
    br_lat = 31.0237195600
    br_lng = 121.4505744000
    x_block = 15
    y_block = 8
    
    x_lat = (br_lat - bl_lat) / x_block
    x_lng = (br_lng - bl_lng) / x_block
    y_lat = (tr_lat - br_lat) / y_block
    y_lng = (tr_lng - br_lng) / y_block


    for i in range(x_block):
        for j in range(y_block):
            fetch = hello_nearby(bl_lat + i*x_lat + j*y_lat , bl_lng + i*x_lng + j*y_lng)
            for item in fetch:
                try:
                    write_db(item)
                except:
                    print ('Error in',end="")
                    print (item)
            print ("finish ",(i,j)," block")
