package com.example.watermark.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.script.ScriptUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author laihongfeng
 * @ClassName: BogusUtils
 * @Description:
 * @date 2023年09月05日 15:19:30
 */
public class BogusUtils {

    /**
     * 获取x-bogus
     * @param url
     * @param userAgent
     * @return
     * @throws IOException
     */
    public static String getBogus(String url,String userAgent) throws IOException {
        FileReader fileReader = new FileReader(BogusUtils.class.getClassLoader().getResource("x-bogus/X-Bogus.js").getFile());
        String sign = (String)ScriptUtil.invoke(fileReader.readString(), "sign",url,userAgent);
        String urls = url + "&X-Bogus=" + sign;
        return urls;
    }

    /**
     * 获取ttwid
     * @return
     * @throws IOException
     */
    public static String getTtwid() throws IOException {
        HttpResponse ttwidUrl = HttpRequest.post("https://ttwid.bytedance.com/ttwid/union/register/").body("{\"region\":\"cn\",\"aid\":1768,\"needFid\":false,\"service\":\"www.ixigua.com\",\"migrate_info\":{\"ticket\":\"\",\"source\":\"node\"},\"cbUrlProtocol\":\"https\",\"union\":true}").execute();
        return commGetVideoId("ttwid=([^;]+)", ttwidUrl.header("Set-Cookie"));
    }

    /**
     * 获取a_bogus
     * @return
     * @throws IOException
     */
    public static String getABogus(String url,String data,String userAgent) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("url",url);
        jsonObject.set("data",data);
        jsonObject.set("userAgent",userAgent);
        String body = HttpRequest.post("http://47.99.168.109:666/getAbogus").body(jsonObject.toString())
                .header(Header.CONTENT_TYPE, "application/json")
                .execute().body();
        JSONObject bean = JSONUtil.toBean(body, JSONObject.class);
        System.out.println(bean.getStr("Abogus"));
//        return url + "&a-Bogus=" + bean.getStr("Abogus");
        return bean.getStr("Abogus");
    }

    public static void main(String[] args) throws IOException {
//        String url = "https://www.douyin.com/aweme/v1/web/search/item/?keyword=美女禁欲系高级感跳舞&count=15&offset=0&cursor=0&device_platform=webapp&aid=6383&version_name=23.5.0&os_version=10.15.7";
//        String user_agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36";
//        System.out.println(getBogus(url,user_agent));
//        System.out.println(getTtwid());

//        String aBogus = getABogus("https://m.douyin.com/web/api/v2/aweme/iteminfo/?item_ids=7269333711756217655",
//                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
//        System.out.println(aBogus);

//        String body = HttpRequest.get("https://m.douyin.com/web/api/v2/aweme/iteminfo/?item_ids=7269333711756217655&a_bogus=YXMmhcZjMsm1aKSuBwkz9HraMXY0YWRhgZEFYiUB9tqS")
//                .execute().body();
//        System.out.println(body);

        String url = "";
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36";
        String s = randStrNoeq(116);
        getABogus("https://fxg.jinritemai.com/api/order/receiveinfo",
                "come_from=pc&aid=4272&ttwid=7275249392054109755&order_id=6921568623661291087&version=v2&appid=1&__token=6615130c965f853c0875f3ac5e47fea0&_bid=ffa_order&_lid=966514424064&msToken="+s,
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
    }

    /**
     * 通用正则取值
     * @param pattern
     * @param url
     * @return
     */
    public static String commGetVideoId(String pattern,String url) {
        // 定义正则表达式模式
        String id = null;
        // 创建 Pattern 对象
        Pattern regexPattern = Pattern.compile(pattern);
        // 创建 Matcher 对象
        Matcher matcher = regexPattern.matcher(url);
        // 使用 find() 方法查找匹配
        if (matcher.find()) {
            // 提取匹配项
            id = matcher.group(1);
        }
        return id;
    }

    /**
     * 根据时间 和时间格式 校验是否正确
     * @param length 校验的长度
     * @param sDate 校验的日期
     * @param format 校验的格式
     * @return
     */
    public static boolean isLegalDate(int length, String sDate,String format) {
        int legalLen = length;
        if ((sDate == null) || (sDate.length() != legalLen)) {
            return false;
        }
        DateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date = formatter.parse(sDate);
            return sDate.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 通用校验时间格式并返回
     * @param data 时间
     * @return
     */
    public static long dateParse(String data) {
        if (isLegalDate(data.length(),data,"MM-dd HH:mm")) {
            return DateUtil.parse(data,"MM-dd HH:mm").getTime();
        } else if (isLegalDate(data.length(),data,"yyyy-MM-dd HH:mm:ss")){
            return DateUtil.parse(data,"yyyy-MM-dd HH:mm:ss").getTime();
        } else if (isLegalDate(data.length(),data,"HH:mm:ss")) {
            return DateUtil.parse(data,"HH:mm:ss").getTime();
        } else if (isLegalDate(data.length(),data,"yyyy-MM-dd")) {
            return DateUtil.parse(data,"yyyy-MM-dd").getTime();
        } else if (isLegalDate(data.length(),data,"MM-dd")) {
            return DateUtil.parse(data,"MM-dd").getTime();
        } else {
            return System.currentTimeMillis();
        }
    }

    public static String commGetUrl(String text) {
        // 定义正则表达式模式
        String url = null;
        String regex = "https?://\\S+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            url = matcher.group();
        }
        return url;
    }

    public static String extractVideoID(String url) {
        // 提取video/后面的数字号码
        int startIndex = url.indexOf("video/") + 6;
        int endIndex = url.indexOf("/", startIndex);
        String videoID = url.substring(startIndex, endIndex);

        return videoID;
    }

    public static String extractSecUid(String url) {
        int startIndex = url.indexOf("user/") + 5;
        int endIndex = url.indexOf("?", startIndex);
        return url.substring(startIndex, endIndex);
    }

    /**
     * 时长转换
     */
    public static long durationConversion(String time) {
        String iso8601TimeString = "PT" + time.replace(":", "M") + "S";
        Duration duration = Duration.parse(iso8601TimeString);
        return duration.getSeconds();
    }

    /**
     * 获取抖音X-Bogus
     */
    public static String getXBogus(String url, String userAgent) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", url);
        jsonObject.put("user_agent", userAgent);
        String body = HttpRequest.post("https://tiktok.iculture.cc/X-Bogus").body(jsonObject.toStringPretty()).execute().body();
        JSONObject bean = JSONUtil.toBean(body, JSONObject.class);
        return (String) bean.get("param");
    }

    /**
     * 获取User-Agent
     */
    public static String getUserAgent() {
        List<String> list = new ArrayList<>();
        list.add("Mozilla/5.0 (Linux; Android 8.0.0; SM-G955U Build/R16NW) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Mobile Safari/537.36");
        list.add("Mozilla/5.0 (Linux; Android 10; SM-G981B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.162 Mobile Safari/537.36");
        list.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:78.0) Gecko/20100101 Firefox/78.0");
        list.add("Mozilla/5.0 (Windows NT 7.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4389.90 Safari/537.36 Edg/87.0.774.54");
        list.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36 Edg/89.0.774.54");
        return list.get(RandomUtil.randomInt(0, list.size()));
    }

    /**
     * 随机抖音msToken
     * @return
     */
    public static String randStr(int size) {
        String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";

        // 将字符集合转换为列表
        List<Character> characterList = new ArrayList<>();
        for (char c : characters.toCharArray()) {
            characterList.add(c);
        }

        // 打乱列表中的字符顺序
        Collections.shuffle(characterList);

        // 取列表中的前107个字符
        StringBuilder sb = new StringBuilder();
        int endIndex = Math.min(size, characterList.size());
        for (int i = 0; i < endIndex; i++) {
            sb.append(characterList.get(i));
        }

        // 如果生成的字符串长度不足107位，继续从打乱后的列表中取字符直到达到107位
        while (sb.length() < size) {
            Collections.shuffle(characterList);
            sb.append(characterList.get(0));
        }

        return sb.toString()+"==";
    }

    /**
     * 随机抖音msToken不加等于号
     * @return
     */
    public static String randStrNoeq(int size) {
        String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";

        // 将字符集合转换为列表
        List<Character> characterList = new ArrayList<>();
        for (char c : characters.toCharArray()) {
            characterList.add(c);
        }

        // 打乱列表中的字符顺序
        Collections.shuffle(characterList);

        // 取列表中的前107个字符
        StringBuilder sb = new StringBuilder();
        int endIndex = Math.min(size, characterList.size());
        for (int i = 0; i < endIndex; i++) {
            sb.append(characterList.get(i));
        }

        // 如果生成的字符串长度不足107位，继续从打乱后的列表中取字符直到达到107位
        while (sb.length() < size) {
            Collections.shuffle(characterList);
            sb.append(characterList.get(0));
        }

        return sb.toString();
    }

    /**
     * 随机抖音msToken 128位
     * @return
     */
    public static String generateRandomString() {
        String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_-";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(128);
        for (int i = 0; i < 128; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        return sb.toString();
    }

    /**
     * 美拍url地址解析
     * @param url
     * @return
     */
    public static String meipaiUrl(String url) {
        Map<String, String> hex = getHex(url);
        Map<String, String[]> dec = getDec(hex.get("hex_1"));
        String d = subStr(hex.get("str_1"), dec.get("pre") );
        String[] tails = getPos(d, dec.get("tail"));
        String kk = subStr(d,tails);
        return  "https:" + Base64.decodeStr(kk, StandardCharsets.UTF_8);
    }

    private static Map<String,String> getHex(String url) {
        Map<String,String> map = new HashMap<>();
        int length = url.length();
        String hex_1 = url.substring(0, 4);
        String str_1 = url.substring(4, length);
        map.put("hex_1",reverseString(hex_1));
        map.put("str_1",str_1);
        return map;
    }

    private static String reverseString(String str) {
        StringBuilder reversed = new StringBuilder();
        for (int i = str.length() - 1; i >= 0; i--) {
            reversed.append(str.charAt(i));
        }
        return reversed.toString();
    }

    private static Map<String,String[]> getDec(String hex) {
        Map<String,String[]> map = new HashMap<>();
        int b = Integer.parseInt(hex, 16);
        String bStr = Integer.toString(b);
        String[] pre = bStr.substring(0, 2).split("");
        String[] tail = bStr.substring(2).split("");
        map.put("pre",pre);
        map.put("tail",tail);
        return map;
    }

    private static String subStr(String a, String[] b) {
        int length = a.length();
        int k = Integer.parseInt(b[0]);
        String c = a.substring(0, k);
        String d = a.substring(k, k + Integer.parseInt(b[1]));
        String temp = a.substring(k + Integer.parseInt(b[1]), length).replace(d, "");
        return c + temp;
    }

    private static String[] getPos(String a, String[] b) {
        int pos = a.length() - Integer.parseInt(b[0]) - Integer.parseInt(b[1]);
        b[0] = String.valueOf(pos);
        return b;
    }
}
