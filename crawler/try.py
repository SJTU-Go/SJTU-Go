import requests
import json
cookies = {
    # 'ride_language': 'zh',
    # 'gr_user_id': 'dfb0a5a7-c6e1-4cff-971c-e5b5973906cb',
}

headers = {
    'Host': 'app2.mobike.com',
    # 'siua': 'AWC+OCWXgOoBbtmWOqVbgqko4kKM5D3yQVqYP29UeDyRhZjIjP+UXr4+R6SHEUZrO6hAW+Bf7rH5uhshvXPl9cigaVO6JuW6gJAqaE3+bB867CqBsOK7BZw0jGKRZDH/ZZck+hYMV8AZqKIcU3rZJEkvtS+Y3auWdiOkcpMA+5Sep9jVXg0g3cWvTbRJUeOraZw+0u/Yn7Q8PJbxhmnlF9E2uvnP/VHKhyUIBi6CGuvLAWQQsd1tvUBGieFNLsvtTUyj7f1uqvnNh6pHX9eUD/3mXBnmeycKEu6+EA7gfXvKLFAI8KZ2zQYIvUiWIPTdQ2mQBQB+xVenJ+1bcf90soSYCyzfUmUfISB+FkJzTKSyVxD5WgbwgIAbN7LmiSnftHu1xfpWD/JJF3Off7+FlESIoRTaDEHLxWQJbhfYW1lJS+EOR21o8CqUNSxGohanBDfCnBBZ+3WQbZPOvTyFRnZZKJk5MWmtD7i+HbRskSh/pqeh7ZVifvOIcnoC2ATKVFAI48flehB/JEdg/cIucsbvCOKSqEpOoWbiMxWMioaARH3XQs81Tj95ze9no9fv06pd8+karWuSHgrNENZJW2e1PRA3lDwD9mijA8zAYbYMDlNOuwANRbgF4gH2bVQ1k9QaEDMQf/XJvtItv0nXAO8rd7mHLIf1CxRvRcsKM3Eg8rHzxpeqgaxv11yqmv9iYRpmAV/zUvjdYhPGbxZdjw==',
    # 'time': '1592023444013',
    'User-Agent': 'com.mobike.bike/189 (unknown, iOS 13.3, iPhone, Scale/2.000000)',
    # 'deviceresolution': '375X812',
    # 'lang': 'zh',
    # 'SAKModelSDKVersionKey': '3.10.1',
    # 'country': '0',
    # 'locationTime': '1592023433821',
    # 'pragma-os': 'MApi 1.1 (mtscope 8.30.0 appstore; iPhone 13.3 iPhone11,8; a0d0)',
    # 'X-SAKHTTPCache-IgnoreQueryKey': '__reqTraceID',
    # 'latitude': '31.1563232421875',
    # 'version': '8.30.0',
    # 'map': 'AMAP',
    # 'os': '13.3',
    # 'versionCode': '189',
    # 'platform': '0',
    # 'X-B3-traceId': '5e64f9f4d99db26c',
    # 'longitude': '121.3348334418403',
    # 'Accept-Language': 'zh-Hans-CN, en-CN, ru-RU, fr-FR, ja-JP, zh-Hant-CN, en-us;q=0.8',
    # 'utctime': '1592023444',
    # 'eption': '64ff8',
    # 'mkunionid': '00000000000004D4F7C8F9F9A47278351E769A39781B90000000000001991673',
    # 'Accept': 'application/json',
    # 'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8',
    # 'citycode': '010',
    # 'bikeSessionId': '2F18F51A-8C3D-431D-812C-056C598103271592023435900157',
    # 'locationProvider': 'TENCENT',
}

data = {
    'bikenum':100,
    'biketype':0,
    'citycode':'021',
    'client_id':'ios',
    'filterMode':0,
    # 'fingerprint':'i2HKpOmsirDPavelVfQBZJhhywTWwxvc7fmJdIvi7C4vyoT3LiTQHJ%2Foqd3EB7KUCxgDVNqy3OIcDViqbGnSUTBzAm2goeT0P9CGZuV4%2BdZbTubyvIs9Qt7QHve6MpYpjXI4JWNp%2FP9hjKtJYgLNKH%2BkwfaILm8g4Ag1m3jvvAJ8kg87r8IylGbE9SVX9NI4RZn1KgyQrHl0tYzDo5ODfuVQGYITH5cixRVfEiKycXp%2BICl%2FqHJZW82r2PY7kYAL2OD%2FEtveSS6zeC5tgzw2kq8rdYfKyfrBtuXGpEvC0VcxgnLn5tOf2UNhjo46HUn7MawAtpzRYmt1CGZbOoOsNJYEbjfDj8rUcXBsvD%2FlPV%2FYcVEZe9n2pTbhCtq17KuMzGIkbWqg3Goxck8fWy8tlSmf8d7Q3kJQkP69YATiIfWDeDxtC0iYviJ0nfpMEz6k0cc52JNPSDH%2B7Z4M0oQuRXCtKWJuaxkVRCqXoMUOz2MZU8Rb5XCD5tTJlRwsXOl5tad%2FWUmt4SYEIcNVkmEIo5dDxpoljWEuqk%2BvWsfd7HIS5lBcEaQpdKWzrhaj5XELYDF7qmveT1WngItblbv%2BDxq5MFOrzpzgu0Bm30GCupBG%2FkI%2BsP1a%2FG%2BFkwaS2%2FX64KiDsJ4ZnoXVtG6XTNg3R3f5kqRWdus5dKiLzo0OHsHVzQ9QpgqFCC%2FL3IfGsyYkSJbtqru49ex3meNe%2BiCFSPjKMONFLeJrEKXi16tdAlJPNxFT5QWso%2BYUWS%2B3JuW%2FpUAbxtZcjikYSK%2FHeN9aXwN9l0tQ8q%2BIiu%2FukZIzO8ixDZsswWTkIwM%2B%2B%2FOEv%2F6Uzsb3EnvnLkpNbpmHKXDvL8KNo0fSmL3M6Vdst0t5RU%2FfomhDQbMFGhkSF%2F7Nk3tG7D754ntijYSQalT2%2FKcNinBQt2hDkup1y3K%2FCzIkecDNFBuNIOQdtUF3OOX5s75tL3D5hcECrwl0dLqjMC2sgsDMHBGY9op%2BuNDs6LhRPpY2OvjPE6dEtGzoeqc3sZxxmjQGQUjpKUw0YU7P71D5LDZwbognb3qVgxjSl82XUGeJH0e21lgk94NkHeJ5BWmN%2FQNiBwOnEKQnRqa0L%2B11OtayJ3zFFZF4K0Ft%2FukUl6czU1rxiWecHqPkPYDyHQ9d%2FeTqZKCnbYDYnLb6HZqHZqMqzMyRir9TNvcTgxOKyBOMYo8X3RFoTu13nTjv0W%2BHJgl62Akm%2Bz7oi9TUaVuVFnoiE0OlmFDF62HHyH%2BfyqCzA%2FLgSxWfryK6%2B52LSGte4FjYrwDLPSJ4%2F9nOtDgL7iHa7GTvnFK%2B3YLvJfrMgPmTpAt498HEIGo%2BeyD%2Fa7KM',
    'latitude':31.15266418975167,
    # 'locationProvider':'TENCENT',
    'longitude':121.3386276468093,
    # 'map':'AMAP',
    # 'mplmode':0,
    'scope':500,
    # 'showmode':1,
}

response = requests.post('https://app2.mobike.com/api/nearby/v4/nearbyBikeInfo', headers=headers, data=data)

#NB. Original query string below. It seems impossible to parse and
#reproduce query strings 100% accurately so the one below is given
#in case the reproduced version is not "correct".
# response = requests.post('https://app2.mobike.com/api/nearby/v4/nearbyBikeInfo?__skts=1592023444.018817&__skua=f8448fb967c93741ca891d7f4b9a4d34&__skcy=svUjOn5yPbfeex0%2FMqAQI3r7j7c%3D&__skck=8f5973b085446090f224af74e30e0181&__skno=3E8BAC8A-2559-4482-8B99-E7D6AD78F0DF&__skvs=1.1&sign=1a3b82b3adbe3d4dc182febf37d1914e', headers=headers, cookies=cookies, data=data)

result = json.loads(response.content)['bike']
print (result[0])