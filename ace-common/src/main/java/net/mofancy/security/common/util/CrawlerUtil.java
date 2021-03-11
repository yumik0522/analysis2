package net.mofancy.security.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zwq
 * @version 1.0
 * @date 2020/1/16 0016 下午 3:05
 */
@Slf4j
public class CrawlerUtil {

    public static String getInfo(String url, String complieStr){
        String content = CrawlerUtil.SendGet(url);
        log.info(content);
        return getInfoDetail(content,complieStr);
    }

    public static String getInfoDetail(String content, String complieStr){
        if (!content.equals("异常")){
            Pattern pattern;
            Matcher matcher;
            pattern = Pattern.compile(complieStr);
            matcher = pattern.matcher(content);
            boolean isFind = matcher.find();
            return isFind ? matcher.group(1) : "";
        }
        return "";
    }

    public static List<String> getListInfoFor(String url, String complieStr){
        String content = CrawlerUtil.SendGet(url);
        return getListInfo(content,complieStr);
    }
    public static List<String> getListInfo(String content,String complieStr){
        List<String> infoList = new ArrayList<>();
        if (!content.equals("异常")){
            Pattern pattern;
            Matcher matcher;
            pattern = Pattern.compile(complieStr);
            matcher = pattern.matcher(content);
            boolean isFind = matcher.find();
            while (isFind){
                infoList.add(matcher.group(1));
                isFind = matcher.find();
            }
        }
        return infoList;
    }

    public static String SendGet(String url) {
        // 定义一个字符串用来存储网页内容
        String result = "";
        // 定义一个缓冲字符输入流
        BufferedReader in = null;
        try {
            // 将string转成url对象
            URL realUrl = new URL(url);
            // 初始化一个链接到那个url的连接
            URLConnection connection = realUrl.openConnection();
            // 开始实际的连接
            connection.connect();
            // 初始化 BufferedReader输入流来读取URL的响应
            System.out.println(connection.getInputStream());
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            // 用来临时存储抓取到的每一行的数据
            String line;
            while ((line = in.readLine()) != null) {
                // 遍历抓取到的每一行并将其存储到result里面
                result += line;
            }
        } catch (Exception e) {
            log.error("发送GET请求出现异常！:", e);
            return "异常";
        }
        // 使用finally来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


    public static String getContentText(String url) {

        String htmlStr = CrawlerUtil.getInfo(url, "<p[^>]*>([^<]*)</p>");

        log.info(htmlStr);

        return null;
    }

    public static void main(String[] arg) {
        getContentText("https://ifeeds.tmall.com/article.html?contentId=9630407&publishTime=1579157331000&pid=mm_723190117_1092200392_109783000367&source=default&union_lens=lensId:m_0b080ad2_0c23_16fad20dfbf_a8a0;traffic_flag:lm");
    }

}

