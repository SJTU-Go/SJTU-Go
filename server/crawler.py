# -*- coding: utf-8 -*-

import requests
import json
import sys

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
      "token": "699fa0191f7041a18d0f0da1f6957d7b", # provided manually
      }
    req = requests.post(url,json=headers)
    data = json.loads(req.text)
    return data

if __name__ == '__main__':
    print (hello_nearby(31.154295066535393,121.3361130759009))
