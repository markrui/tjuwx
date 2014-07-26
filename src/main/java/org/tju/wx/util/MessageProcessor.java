package org.tju.wx.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tju.wx.action.WXMsgProcessAction;
import org.tju.wx.msgtype.response.Article;
import org.tju.wx.msgtype.response.NewsMessage;
import org.tju.wx.msgtype.response.TextMessage;
import org.tju.wx.pojo.*;
import org.tju.wx.service.*;
import org.tju.wx.action.*;
import org.tju.wx.service.impl.LetterServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Legend on 2014/7/18.
 */
public class MessageProcessor {
    /**
     * 处理微信发来的请求
     *
     * @param request
     * @return
     */
    public static String processRequest(HttpServletRequest request) {
        String respMessage = "";
        try {
            // 默认返回的文本消息内容
            String respContent = "";
            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");

            // 回复文本消息
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            textMessage.setFuncFlag(0);

            //回复图文消息
            NewsMessage newsMessage = new NewsMessage();
            newsMessage.setToUserName(fromUserName);
            newsMessage.setFromUserName(toUserName);
            newsMessage.setCreateTime(new Date().getTime());
            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);

            // 文本消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                // 消息
                String content = requestMap.get("Content");
                //respContent = "您发送的是文本消息！<a href=\"../register.jsp\">这里</a>\n<a href=\"./register.jsp\">这里</a>\n<a href=\"/register.jsp\">这里</a><a href=\"register.jsp\">这里</a>";

                content = content.trim();
                content = content.replace(" ", "");
                content = content.replace("  ", "");
                content = content.replace("\t", "");
                content = content.replace("\n", "");
                content = content.replace("\r", "");

                System.out.print("Receiver: " + content);
                if (content.indexOf("快递") > -1 || content.indexOf("物流") > -1){
                    content = content.replace("快递", " ");
                    content = content.replace("物流", " ");
                    if (content.equals(" ")){
                        respContent = "快递，快递，你要去哪里啊？对我输入快递名称+单号，如 顺丰快递 966902008817~";
                        textMessage.setContent(respContent);
                        respMessage = MessageUtil.textMessageToXml(textMessage);
                        return respMessage;
                    }
                    //公司的拼音（JSON格式）
                    String text = "{\"AAE\":\"aae\",\"安捷\":\"anjie\",\"安信达\":\"anxinda\",\"Aramex\":\"aramex\",\"CCES\":\"cces\",\"长通\":\"changtong\",\"程光\":\"chengguang\",\"传喜\":\"chuanxi\",\"传志\":\"chuanzhi\",\"CityLink\":\"citylink\",\"东方\":\"coe\",\"城市之星\":\"cszx\",\"大田\":\"datian\",\"德邦\":\"debang\",\"DHL\":\"dhl\",\"递四方\":\"disifang\",\"DPEX\":\"dpex\",\"D速\":\"dsu\",\"百福东方\":\"ees\",\"国际Fedex\":\"fedex\",\"Fedex国内\":\"fedexcn\",\"飞邦\":\"feibang\",\"飞豹\":\"feibao\",\"飞航\":\"feihang\",\"飞远\":\"feiyuan\",\"丰达\":\"fengda\",\"飞康达\":\"fkd\",\"飞快达\":\"fkdex\",\"广东邮政\":\"gdyz\",\"共速达\":\"gongsuda\",\"天地华宇\":\"huayu\",\"华宇\":\"huayu\",\"汇通\":\"huitong\",\"佳吉\":\"jiaji\",\"佳怡\":\"jiayi\",\"加运美\":\"jiayunmei\",\"京广\":\"jingguang\",\"晋越\":\"jinyue\",\"嘉里大通\":\"jldt\",\"快捷\":\"kuaijie\",\"蓝镖\":\"lanbiao\",\"乐捷递\":\"lejiedi\",\"联昊通\":\"lianhaotong\",\"龙邦\":\"longbang\",\"民航\":\"minhang\",\"港中能达\":\"nengda\",\"能达\":\"nengda\",\"OCS\":\"ocs\",\"平安达\":\"pinganda\",\"全晨\":\"quanchen\",\"全峰\":\"quanfeng\",\"全际通\":\"quanjitong\",\"全日通\":\"quanritong\",\"全一\":\"quanyi\",\"RPX\":\"rpx\",\"保时达\":\"rpx\",\"如风达\":\"rufeng\",\"三态\":\"santai\",\"伟邦\":\"scs\",\"盛丰\":\"shengfeng\",\"盛辉\":\"shenghui\",\"申通\":\"shentong\",\"顺丰\":\"shunfeng\",\"速尔\":\"sure\",\"天天\":\"tiantian\",\"TNT\":\"tnt\",\"通成\":\"tongcheng\",\"UPS\":\"ups\",\"USPS\":\"usps\",\"万家\":\"wanjia\",\"新邦\":\"xinbang\",\"鑫飞鸿\":\"xinfeihong\",\"信丰\":\"xinfeng\",\"源安达\":\"yad\",\"亚风\":\"yafeng\",\"一邦\":\"yibang\",\"银捷\":\"yinjie\",\"优速\":\"yousu\",\"一统飞鸿\":\"ytfh\",\"远成\":\"yuancheng\",\"圆通\":\"yuantong\",\"元智捷诚\":\"yuanzhi\",\"越丰\":\"yuefeng\",\"韵达\":\"yunda\",\"运通\":\"yuntong\",\"源伟丰\":\"ywfex\",\"宅急送\":\"zhaijisong\",\"中铁\":\"zhongtie\",\"中通\":\"zhongtong\",\"忠信达\":\"zhongxinda\",\"中邮\":\"zhongyou\",\"EMS\":\"ems\"}";
                    JSONObject jsonObject = JSONObject.fromObject(text);
                    String company = content.substring(0, content.indexOf(" "));

                    //快递公司的拼音格式
                    String com = null;
                    try{
                        com = jsonObject.getString(company);
                    }catch (Exception e){
                        respContent = "找不到您所说的快递公司啊~请重新输入";
                        textMessage.setContent(respContent);
                        respMessage = MessageUtil.textMessageToXml(textMessage);
                        return respMessage;
                    }

                    //快递单号
                    String num = content.substring(content.indexOf(" ") + 1);
                    String url = "http://api.ickd.cn/";
                    String param = "com=" + com + "&nu=" + num + "&id=E077B7C6D3F2DA9C92D13D7BD36CD171&type=json&encode=utf8";
                    //调取api，取得结果
                    String result = HttpRequest.sendGet(url, param);

                    //解析json字符串
                    JSONObject obj = JSONObject.fromObject(result);
                    if (obj.getString("errCode").equals("0")){
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsObj = jsonArray.getJSONObject(i);
                            respContent += jsObj.getString("time") + jsObj.getString("context") + "\n";
                        }
                    }else{
                        respContent += obj.getString("message");
                    }
                    textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }
                else if (content.indexOf("自习") > -1){
                    //天大主楼的json格式
                    String buildings = "{\"10楼\" : \"1049\", \"11楼\" : \"0020\", \"12楼\" : \"0026\", \"14楼\" : \"1047\"," +
                            "\"15楼\" : \"0024\", \"16楼\" : \"1054\", \"17楼\" : \"1071\", \"18楼\" : \"0018\"," +
                            "\"19楼\" : \"0032\", \"20楼\" : \"0021\", \"21楼\" : \"0038\", \"23楼\" : \"0015\"," +
                            "\"24楼\" : \"1042\", \"25楼A\" : \"1089\", \"25楼B\" : \"1050\", \"25楼C\" : \"1090\"," +
                            "\"26楼A\" : \"1084\", \"26楼B\" : \"1085\", \"26楼C\" : \"1086\", \"26楼D\" : \"1087\"," +
                            "\"26楼E\" : \"1088\", \"科学\" : \"1080\", \"船海\" : \"1082\", \"大活\" : \"1060\"," +
                            "\"东阶\" : \"0040\", \"港口\" : \"1083\", \"化工\" : \"1074\", \"科图\" : \"1058\"," +
                            "\"南开\" : \"1092\", \"内燃机\" : \"1081\", \"实习\" : \"1072\", \"水利\" : \"1079\"," +
                            "\"体育\" : \"1078\", \"1075\" : \"土建\", \"1091\" : \"网架\", \"0028\" : \"西阶\"," +
                            "\"1077\" : \"影视\", \"1070\" : \"综合\", \"1073\" : \"1楼\", \"0036\" : \"3楼\", \"0022\" : \"4楼\"," +
                            "\"1048\" : \"5楼\", \"0030\" : \"6楼\", \"0031\" : \"7楼\", \"0045\" : \"8楼\"}";
                    JSONObject jsonObject = JSONObject.fromObject(buildings);
                    content = content.replace("自习", " ");
                    if (content.equals(" ")){
                        respContent = "去哪里自习啊？请输入楼号+自习，如 23楼自习，返回当天所有时段自习室。\n" +
                                "如果想查询隔天自习室，在楼号+自习后加上数字代表星期几，如 23楼自习1，返回23楼星期一的自习室。";
                        textMessage.setContent(respContent);
                        respMessage = MessageUtil.textMessageToXml(textMessage);
                        return respMessage;
                    }
                    String building = content.substring(0, content.indexOf(" "));
                    building = building.toUpperCase();

                    //大楼编号
                    String buildingNum = null;
                    try{
                        buildingNum = jsonObject.getString(building);
                    }catch (Exception e){
                        respContent = "找不到您所说的自习室啊~请重新输入";
                        textMessage.setContent(respContent);
                        respMessage = MessageUtil.textMessageToXml(textMessage);
                        return respMessage;
                    }


                    //当前时间与开学日期差值
                    String startTerm = "2014/02/23";
                    Date date = new Date();
                    //注意format的格式要与日期String的格式相匹配
                    DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    date = sdf.parse(startTerm);

                    Date now = new Date();
                    SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
                    String[] weeks = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};

                    //获取要查询的星期数，默认为当天
                    /*int day = 0;
                    for (int i = 0; i < weeks.length; i++) {
                        if (dateFm.format(now).equals(weeks[i])){
                            day = i + 1;
                        }
                    }*/

                    long differTime = (now.getTime() - date.getTime()) / 1000;
                    long differDay = differTime / (24 * 3600);
                    int week = (int)differDay / 7;
                    int weekday = (int)differDay % 7;
                    if (weekday == 0)
                        weekday = 7;
                    if(!content.substring(content.indexOf(" ") + 1).equals(""))
                        weekday = Integer.parseInt(content.substring(content.indexOf(" ") + 1));

                    String param = "todo=displayWeekBuilding&week=" + week + "&building_no=" + buildingNum;
                    String url = "http://e.tju.edu.cn/Education/toModule.do?prefix=/Education&page=/schedule.do?todo=displayWeekBuilding&schekind=6";
                    String result = HttpRequest.sendPost(url, param);

                    //正则表达式匹配空自习室
                    String regEx = "<tr>.*?<td bgcolor=\"#336699\" align=\"center\">.*?<strong><font color=\"White\">(.*?)</font></strong>.*?</td>(.*?)</tr>";
                    Pattern pat = Pattern.compile(regEx);
                    Matcher mat = pat.matcher(result);
                    boolean rs = mat.find();

                    List<String>[]classroom = new List[6];
                    for (int i = 0; i < 6; i++) {
                        classroom[i] = new ArrayList<String>();
                    }
                    int i = 0;
                    while (mat.find()){
                        String room = mat.group(1).replace(building, "");
                        String reg = "<td align=\"center\" bgcolor=\"([^\"]*)\">.*?<font color=\"([^\"]*)\">●</font>";
                        Pattern pattern = Pattern.compile(reg);
                        Matcher matcher = pattern.matcher(mat.group(2));

                        int j = 0;//j代表第几节课   j=0 代表上午第一节
                        int k = 0;
                        while(matcher.find()){
                            k++;
                            if (k < (weekday - 1) * 6 || k >= weekday * 6){
                                continue;
                            }
                            if (matcher.group(2).equals("#00dd00")){
                                classroom[j].add(room);
                            }
                            j++;
                        }
                        i++;
                    }

                    String periods[] = new String[]{"上午第一节", "上午第二节","下午第一节", "下午第二节", "晚上第一节", "晚上第二节"};
                    List<Article> list = new ArrayList<Article>();
                    Article art = new Article();
                    art.setTitle(weeks[weekday - 1] + " " + building + "的自习室如下：");
                    art.setDescription("");
                    art.setPicUrl("");
                    art.setUrl("");
                    list.add(art);

                    for (int j = 0; j < classroom.length; j++) {
                        Article article = new Article();

                        String title = periods[j] + "\n";
                        for (int k = 0; k < classroom[j].size(); k++) {
                            title += classroom[j].get(k) + " ";
                        }
                        article.setTitle(title);
                        article.setDescription("");
                        article.setPicUrl("");
                        article.setUrl("");
                        list.add(article);
                    }
                    newsMessage.setArticleCount(list.size());
                    newsMessage.setArticles(list);
                    respMessage = MessageUtil.newsMessageToXml(newsMessage);
                }
                else if(content.indexOf("信") == 0) {
                    if (content.length() < 2) {
                        respContent = "请输入正确格式：信+查询关键字。例如：信 张三";
                    } else {
                        // 获得关键字
                        String key = content.substring(1, content.length());
                        LetterService ls = (LetterService)request.getSession().getAttribute("letterService");
                        if(ls == null){
                            respContent = "服务器正在更新,请稍等...";
                            System.out.println("letterservice is null");
                        } else {

                            List list = ls.findByString(key, 1, Integer.MAX_VALUE);
                            if (list == null || list.size() == 0) {
                                respContent = "没有查到有关“" + key + "”的未取信件信息";
                            } else {
                                for (int i = 0; i < list.size(); i++) {
                                    Letter letter = (Letter) list.get(i);
                                    // 不显示已经被取的
                                    if (letter.getStatus() == Letter.SENDED) {
                                        continue;
                                    }
                                    respContent += "\n-------------------\n收件人：" + letter.getReceiver();
                                    if (!"".equals(letter.getSender())) {
                                        respContent += "\n寄件人：" + letter.getSender();
                                    }
                                    if (!"".equals(letter.getSenderaddr())) {
                                        respContent += "\n寄件地址：" + letter.getSenderaddr();
                                    }
                                    respContent += "\n录入时间：" + letter.getTime().substring(0, 10) + "\n";
                                }

                                if(respContent.length() == 0){
                                    respContent = "没有查到有关“" + key + "”的未取信件信息";
                                } else {
                                    respContent = "查询结果：" + respContent;
                                    respContent += "\n请记录好录入时间，及时到团委查询，祝您万事顺利！";
                                }
                            }
                        }
                    }
                    System.out.print("letter:" + respContent);
                    textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }
            }
            // 图片消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
                respContent = "目前暂不支持图片消息的回复！";
                textMessage.setContent(respContent);
                respMessage = MessageUtil.textMessageToXml(textMessage);
            }
            // 地理位置消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
                respContent = "地理消息回复功能将在近期开放！";
                textMessage.setContent(respContent);
                respMessage = MessageUtil.textMessageToXml(textMessage);
            }
            // 链接消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
                respContent = "目前暂不支持链接消息的回复！";
                textMessage.setContent(respContent);
                respMessage = MessageUtil.textMessageToXml(textMessage);
            }
            // 音频消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
                respContent = "语音消息回复功能将在近期开放！";
                textMessage.setContent(respContent);
                respMessage = MessageUtil.textMessageToXml(textMessage);
            }
            // 事件推送
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = requestMap.get("Event");
                // 订阅
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                    respContent = "感谢您关注天津大学软件学院公众账号[愉快]，\n"
                            + "您可以通过点击<a href=\"../register.jsp\">这里</a> "
                            + "进行身份绑定以获取更多的功能";
                    textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }
                // 取消订阅
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                    // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                }
                // 自定义菜单点击事件
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
                    String eventKey = requestMap.get("EventKey");
                    if(eventKey.equals("31")){
                        respContent = "快递，快递，你要去哪里啊？对我输入快递名称+单号，如 顺丰快递 966902008817~";
                        textMessage.setContent(respContent);
                        respMessage = MessageUtil.textMessageToXml(textMessage);
                    }else if (eventKey.equals("13")){
                        respContent = "去哪里自习啊？请输入楼号+自习，如 23楼自习，返回当天所有时段自习室。\n" +
                                "如果想查询隔天自习室，在楼号+自习后加上数字代表星期几，如 23楼自习1，返回23楼星期一的自习室。";
                        textMessage.setContent(respContent);
                        respMessage = MessageUtil.textMessageToXml(textMessage);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respMessage;
    }
}
