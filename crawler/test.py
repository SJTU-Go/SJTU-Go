import pymysql

conn = pymysql.connect( host='ltzhou.com',
                        port=3306,
                        user='pguser',
                        passwd='pguser',
                        db = 'playground',
                        charset = 'utf8')

cursor = conn.cursor()

m = 113.
n = 22.175

sql_2 = "SELECT count(*) FROM motor_forbid_area where ST_Contains(shape,ST_POLYGONFROMTEXT('Point(113.547 22.186)')) > 0 LIMIT 1"

cursor.execute(sql_2)

a = cursor.fetchone()
print(a[0])

conn.commit()