from coordTransform.coordTransform_utils import wgs84_to_gcj02


def transform(lon,lat):
	gcj_lon, gcj_lat = wgs84_to_gcj02(lon,lat)
	return (gcj_lon-121.449652+121.444577,gcj_lat-31.025940+31.024322)


print (transform(121.4388497002,31.03167870077))