from coord_convert.transform import wgs2gcj
def transform(v):
	gcj_lat,gcj_lon= wgs2gcj(float(v[0]), float(v[1]))
	return (str(gcj_lon),str(gcj_lat))

print(transform((31.027848877672053,121.453133241292946)))