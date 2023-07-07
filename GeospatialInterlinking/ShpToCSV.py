# pip install pyshp
import shapefile  # 使用pyshp
from matplotlib import pyplot as plt
import geopandas as gpd

# gis_road.shp  2555个面

if __name__ == '__main__':
    file = shapefile.Reader("D:\\桌面\\点状线状矢量\\spatialAnalysis_shp\\poi_supermarket.shp")
    # file = shapefile.Reader("D:\\桌面\\点状线状矢量\\渠系\\斗渠.shp")
    # typeDic = {
    #     '草地': 1,
    #     '人工堆掘地': 2,
    #     '荒漠与裸露地表': 3,
    #     '耕地': 4,
    #     '园地': 5,
    #     '房屋建筑': 6,
    #     '林地': 7,
    #     '构筑物': 8
    # }
    typeDic = {
        '干渠': 1,
        '斗渠': 2,
        '支渠': 3,
        '支干渠': 4
    }
    # 获取shp文件中的图形
    ps = file.shapes()

    # for i in range(len(ps)):
    #     typeDic[file.record(i)[1]] = typeDic[file.record(i)[1]] + 1
    # print(typeDic)

    resultFile = open(".\\ningxiaData.csv", "a")
    for i in range(len(ps)):
        # strLine = "\"POLYGON (("
        # strLine = "\"LINESTRING ("
        strLine = "\"POINT ("
        for coo in range(len(ps[i].points)):
            # 拼接坐标
            strLine = strLine + str(ps[i].points[coo][0]) + " " + str(ps[i].points[coo][1]) + ","
        # 收尾
        strLine = strLine[0:len(strLine)-1]
        # 附加信息 点线面 123，id 类型id 类型描述 其他信息（长度面积）
        # gis_dilei_8type.shp 面  其他 长度 面积
        # strLine = strLine + "))\"" + ", 3, " + str(i) + "," + str(typeDic.get(file.record(i)[1])) + "," + file.record(i)[1] + "," + str(file.record(i)[2]) + "," + str(file.record(i)[3])
        # 线 渠道的几个shp  其他 长度
        # strLine = strLine + ")\"" + ", 2, " + str(i) + "," + str(typeDic.get(file.record(i)[8])) + "," + file.record(i)[8] + "," + str(file.record(i)[10])
        # 点  无其他信息
        strLine = strLine + ")\"" + ", 1, " + str(i) + "," + str(8) + "," + "超市"
        resultFile.write(strLine + "\n")
    resultFile.close()



    # 图像
    x, y = zip(*ps[0].points)
    fig, ax = plt.subplots()  # 生成一张图和一张子图
    # plt.plot(x,y,'k-') # x横坐标 y纵坐标 ‘k-’线性为黑色
    plt.plot(x, y, color='#6666ff', label='fungis')  # x横坐标 y纵坐标 ‘k-’线性为黑色
    ax.grid()  # 添加网格线
    ax.axis('equal')
    plt.show()
