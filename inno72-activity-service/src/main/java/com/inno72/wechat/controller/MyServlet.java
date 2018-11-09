package com.inno72.wechat.controller;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class MyServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("callBack doGet invoked");
        String TOKEN = "inno72";
        String signature = request.getParameter("signature");
        String echostr = request.getParameter("echostr");
        resp.getWriter().print(echostr);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("-----------doPost----------------");
//        resp.getWriter().print("<h1>Hello MyServlet Response return you this</h1>");

        LOGGER.info("callBack invoked");
        Gson g = new Gson();
        LOGGER.info("callBack param = {}",g.toJson(request.getParameterMap()));
        String html = "<html>\n" +
                "<body>hh</body>\n" +
                "</html>";
        response.sendRedirect("http://api.activity.36solo.com/index.html");
//        LOGGER.info("callBack invoked");
//        System.out.println("-----------doPost----------------");
//        request.setCharacterEncoding("UTF-8");
//
//        response.setCharacterEncoding("UTF-8");
//
//        PrintWriter out = response.getWriter();


//        try {
//
//            Map<String, String> map = MessageUtil.xmlToMap(request);
//
//            String ToUserName = map.get("ToUserName");
//
//            String FromUserName = map.get("FromUserName");
//
//            String CreateTime = map.get("CreateTime");
//
//            String MsgType = map.get("MsgType");
//
//            String Content = map.get("Content");
//
//            String MsgId = map.get("MsgId ");
//
//
//            String message = null;
//
//            if (MsgType.equals(MessageUtil.MESSAGE_TEXT)) {//判断是否为文本消息类型
//
//                if (Content.equals("1")) {
//
//                    message = MessageUtil.initText(ToUserName, FromUserName,
//
//                            "对啊！我也是这么觉得！姜浩帅哭了！");
//
//                } else if (Content.equals("2")) {
//
//                    message = MessageUtil.initText(ToUserName, FromUserName,
//
//                            "好可怜啊！你年级轻轻地就瞎了！");
//
//                } else if (Content.equals("?") || Content.equals("？")) {
//
//                    message = MessageUtil.initText(ToUserName, FromUserName,
//
//                            MessageUtil.menuText());
//
//                } else {
//
//                    message = MessageUtil.initText(ToUserName, FromUserName,
//
//                            "没让你选的就别瞎嘚瑟！！！");
//
//                }
//
//
//            } else if (MsgType.equals(MessageUtil.MESSAGE_EVENT)) {//判断是否为事件类型
//
////从集合中，或许是哪一种事件传入
//
//                String eventType = map.get("Event");
//
////关注事件
//
//                if (eventType.equals(MessageUtil.MESSAGE_SUBSCRIBE)) {
//
//                    message = MessageUtil.initText(ToUserName, FromUserName,
//
//                            MessageUtil.menuText());
//
//                }
//
//            }
//
//            System.out.println(message);
//
//            out.print(message);
//
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//
//        } finally {
//
//            out.close();
//        }
    }
    public static Map<String , String> xmlToMap(HttpServletRequest request) throws Exception {

        Map<String , String> map = new HashMap<String, String>();

//        SAXReader reader = new SAXReader();
//
//        InputStream ins = request.getInputStream();
//
//        Document doc = reader.read(ins);
//
//        Element root = doc.getRootElement();
//
//        List<Element> list = root.elements();
//
//        for (Element element : list) {
//
//            map.put(element.getName(), element.getText());
//
//        }
//
//        ins.close();

        return map;

    }
    @Override
    public void init() throws ServletException {
        super.init();
    }
}