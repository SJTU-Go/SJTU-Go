import pymysql
import requests

conn = pymysql.connect( host='ltzhou.com',
                    port=3306,
                    user='pguser',
                    passwd='pguser',
                    db = 'playground',
                    charset = 'utf8')
cursor = conn.cursor()

sql = "SELECT COUNT(*) FROM jindouyun_info"

cursor.execute(sql)

conn.commit()

result = cursor.fetchall()
print(result)