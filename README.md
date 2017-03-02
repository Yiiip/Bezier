**一阶贝塞尔曲线：**

是一条直线，只有起点和终点，实现方法：

```
canvas.drawLine(float startX, float startY, float stopX, float stopY, @NonNull Paint paint) ;
```

**二阶贝塞尔曲线：**

有起点和终点、一个控制点的曲线，实现方法：
```
 path.moveTo（startX, startY); //移至起点
 path.quadTo(controlX, controlY, endX, endY); //二阶曲线，参数是控制点和终点坐标
```

**三阶贝塞尔曲线：**

有起点和终点、两个控制点的曲线，实现方法：

```
path.moveTo(startX, startY);
path.cubicTo(controlX1, controlY1, controlX2, controlY2, endX, endY); //三阶曲线，参数是控制点1、控制点2和终点坐标
```

展示一张效果图：

![效果图](http://img.blog.csdn.net/20170302205004638?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvU2tpcHBlcktldmlu/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)