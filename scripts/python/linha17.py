import shapefile

sf = shapefile.Reader("c://dev//linha17.shp")
shapes = sf.shapes()
i = 0
points = shapes[0].points
records = sf.records()

while i < len(shapes):
    print(records[i][0] + "," + str(shapes[i].points[0][0]) + "," +  str(shapes[i].points[0][1]))
    i+=1
