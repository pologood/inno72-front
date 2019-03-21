package com.inno72.monitor;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

/**
 * 根据订单的经纬度归属所在的商业区域
 * @author lee
 * 2017年2月6日 下午2:12:02
 */
@Component
public class MapsUtil {

	private static Map<String, Map<String, Double>> mapYS = new HashMap<>() ;
	@PostConstruct
	public void init(){
		b();
	}


	//73.113659
	//135.340222
	static boolean isInPolygon(double p_x, double p_y, int v){

		if (p_x < 73.113659d || p_x > 135.340222d){
			return false;
		}


		String sss = "75.978344,40.136884;75.978344,36.22902;80.415276,44.111248;80.415276,30.807977;86.104134,27.999259;86.104134,48.119987;97.098229,27.784192;97.098229,42.638057;95.402172,44.111248;95.402172,28.02328;98.189094,28.038054;98.189094,42.503687;98.801136,23.602698;98.801136,42.612682;104.192479,22.636958;104.192479,41.792682;111.572397,21.406484;111.572397,43.612682;115.106456,22.078709;115.106456,45.612682;118.737609,24.380125;118.737609,46.612682;118.815729,49.934274;118.815729,23.961597;122.53184,37.02554;122.53184,53.112682;127.488391,50.221241;127.488391,41.462682;131.133831,44.111248;131.133831,47.782418;120.996712,28.02328;120.996712,53.262754;124.493969,53.087421;124.493969,40.136884;109.518852,18.17283;109.518852,42.158189";

		// 用其经度差计算最近的点位
		Map<String, Double> map ;
		// 相同经度下的纬度集合

		List<Map<String, Double>> list = new ArrayList<>();
		for(String ss : sss.split(";")){
			String[] split = ss.split(",");
			double polygonPoint_x=Double.parseDouble(split[0]);
			double polygonPoint_y=Double.parseDouble(split[1]);
			map = new HashMap<>();
			double abs = Math.abs(polygonPoint_x - p_x);
			map.put("abs", abs);
			map.put("x", polygonPoint_x);
			map.put("y", polygonPoint_y);
			list.add(map);
		}

		list.sort(Comparator.comparing(o -> o.get("abs")));

		Map<String, Double> stringDoubleMap = list.get(0);
		Double x = stringDoubleMap.get("x");

		Map<String, Double> doubleMap = mapYS.get(x + "");
		Double a = doubleMap.get("a");
		Double b = doubleMap.get("b");

		boolean t = false;

		if (b <= p_y && p_y <= a){
			t = true;
		}
		return t;
	}
	
	 /**
     * 判断当前位置是否在多边形区域内 
     */
    public static boolean isInPolygon(double p_x, double p_y){

		String sss = "75.978344,40.136884;75.978344,36.22902;80.415276,44.111248;80.415276,30.807977;95.402172,44.111248;95.402172,28.02328;98.189094,28.038054;98.189094,42.503687;86.104134,27.999259;86.104134,48.119987;131.133831,44.111248;131.133831,47.782418;120.996712,28.02328;120.996712,53.262754;124.493969,53.087421;124.493969,40.136884;109.518852,18.17283;109.518852,42.158189";
		Map<String, Double> s1;
		StringBuilder partitionLocationA = new StringBuilder();
		for(String ss : sss.split(";")){
			String[] split = ss.split(",");
			s1 = new HashMap<>();
			partitionLocationA.append(split[1]).append("_").append(split[0]).append(",");
		}
		String partitionLocation = partitionLocationA.toString().substring(0, partitionLocationA.toString().length() -1);
        Point2D.Double point = new Point2D.Double(p_x, p_y);
 
        List<Point2D.Double> pointList= new ArrayList<Point2D.Double>();  
        String[] strList = partitionLocation.split(",");
        
        for (String str : strList){
        	String[] points = str.split("_");
            double polygonPoint_x=Double.parseDouble(points[1]);  
            double polygonPoint_y=Double.parseDouble(points[0]);  
            Point2D.Double polygonPoint = new Point2D.Double(polygonPoint_x,polygonPoint_y);  
            pointList.add(polygonPoint);  
        }  
        return IsPtInPoly(point,pointList);  
    }  
    /** 
     * 返回一个点是否在一个多边形区域内， 如果点位于多边形的顶点或边上，不算做点在多边形内，返回false
     * @param point 参数
     * @param polygon 经纬度边点集合
     * @return  是否在内
     */  
	public static boolean checkWithJdkGeneralPath(Point2D.Double point, List<Point2D.Double> polygon) {  
		java.awt.geom.GeneralPath p = new java.awt.geom.GeneralPath();  
		Point2D.Double first = polygon.get(0);  
        p.moveTo(first.x, first.y);  
        polygon.remove(0);  
        for (Point2D.Double d : polygon) {  
           p.lineTo(d.x, d.y);  
        }  
        p.lineTo(first.x, first.y);  
        p.closePath();  
        return p.contains(point);  
   }  
   
   /** 
    * 判断点是否在多边形内，如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
    * @param point 检测点 
    * @param pts   多边形的顶点 
    * @return      点在多边形内返回true,否则返回false 
    */  
   public static boolean IsPtInPoly(Point2D.Double point, List<Point2D.Double> pts){  
         
       int N = pts.size();  
       boolean boundOrVertex = true; //如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true  
       int intersectCount = 0;//cross points count of x   
       double precision = 2e-10; //浮点类型计算时候与0比较时候的容差  
       Point2D.Double p1, p2;//neighbour bound vertices  
       Point2D.Double p = point; //当前点  
         
       p1 = pts.get(0);//left vertex          
       for(int i = 1; i <= N; ++i){//check all rays              
           if(p.equals(p1)){  
               return boundOrVertex;//p is an vertex  
           }  
             
           p2 = pts.get(i % N);//right vertex              
           if(p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)){//ray is outside of our interests                  
               p1 = p2;   
               continue;//next ray left point  
           }  
             
           if(p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)){//ray is crossing over by the algorithm (common part of)  
               if(p.y <= Math.max(p1.y, p2.y)){//x is before of ray                      
                   if(p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)){//overlies on a horizontal ray  
                       return boundOrVertex;  
                   }  
                     
                   if(p1.y == p2.y){//ray is vertical                          
                       if(p1.y == p.y){//overlies on a vertical ray  
                           return boundOrVertex;  
                       }else{//before ray  
                           ++intersectCount;  
                       }   
                   }else{//cross point on the left side                          
                       double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;//cross point of y                          
                       if(Math.abs(p.y - xinters) < precision){//overlies on a ray  
                           return boundOrVertex;  
                       }  
                         
                       if(p.y < xinters){//before ray  
                           ++intersectCount;  
                       }   
                   }  
               }  
           }else{//special case when ray is crossing through the vertex                  
               if(p.x == p2.x && p.y <= p2.y){//p crossing over p2                      
                   Point2D.Double p3 = pts.get((i+1) % N); //next vertex                      
                   if(p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)){//p.x lies between p1.x & p3.x  
                       ++intersectCount;  
                   }else{  
                       intersectCount += 2;  
                   }  
               }  
           }              
           p1 = p2;//next ray left point  
       }  
         
       if(intersectCount % 2 == 0){//偶数在多边形外  
           return false;  
       } else { //奇数在多边形内  
           return true;  
       }  
   }

   private static void b(){
	   Map<String, Double> m1 = new HashMap<>();
	   m1.put("a", 40.136884);
	   m1.put("b", 36.22902);
	   mapYS.put("75.978344", m1);
	   Map<String, Double> m2 = new HashMap<>();
	   m2.put("a", 44.111248);
	   m2.put("b", 30.807977);
	   mapYS.put("80.415276", m2);
	   m2 = new HashMap<>();
	   m2.put("a", 48.119987);
	   m2.put("b", 27.999259);
	   mapYS.put("86.104134", m2);
	   Map<String, Double> m3 = new HashMap<>();
	   m3.put("a", 44.111248);
	   m3.put("b", 28.02328);
	   mapYS.put("95.402172", m3);
	   Map<String, Double> m4 = new HashMap<>();
	   m4.put("a", 42.638057);
	   m4.put("b", 27.784192);
	   mapYS.put("97.098229", m4);
	   m4 = new HashMap<>();
	   m4.put("a", 42.503687);
	   m4.put("b", 28.038054);
	   mapYS.put("98.189094", m4);
	   m4 = new HashMap<>();
	   m4.put("a", 42.612682);
	   m4.put("b", 23.602698);
	   mapYS.put("98.801136", m4);
	   Map<String, Double> m5 = new HashMap<>();
	   m5.put("a", 41.792682);
	   m5.put("b", 22.636958);
	   mapYS.put("104.192479", m5);
	   m5 = new HashMap<>();
	   m5.put("a", 43.612682);
	   m5.put("b", 21.406484);
	   mapYS.put("111.572397", m5);
	   m5 = new HashMap<>();
	   m5.put("a", 45.612682);
	   m5.put("b", 22.078709);
	   mapYS.put("115.106456", m5);
	   m5 = new HashMap<>();
	   m5.put("a", 46.612682);
	   m5.put("b", 24.380125);
	   mapYS.put("118.737609", m5);
	   m5 = new HashMap<>();
	   m5.put("a", 49.934274);
	   m5.put("b", 23.961597);
	   mapYS.put("118.815729", m5);
	   m5 = new HashMap<>();
	   m5.put("a", 53.112682);
	   m5.put("b", 37.02554);
	   mapYS.put("122.53184", m5);
	   m5 = new HashMap<>();
	   m5.put("a", 50.221241);
	   m5.put("b", 41.462682);
	   mapYS.put("127.488391", m5);
	   Map<String, Double> m6 = new HashMap<>();
	   m6.put("a", 47.782418);
	   m6.put("b", 44.111248);
	   mapYS.put("131.133831", m6);
	   Map<String, Double> m7 = new HashMap<>();
	   m7.put("a", 53.262754);
	   m7.put("b", 28.02328);
	   mapYS.put("120.996712", m7);
	   Map<String, Double> m8 = new HashMap<>();
	   m8.put("a", 53.087421);
	   m8.put("b", 40.136884);
	   mapYS.put("124.493969", m8);
	   Map<String, Double> m9 = new HashMap<>();
	   m9.put("a", 42.158189);
	   m9.put("b", 18.17283);
	   mapYS.put("109.518852", m9);
   }
}