package com.xiong.wx.common;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author xionghl
 * @Date 2017/9/15
 */
public class HttpsUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class); // 日志记录

    /**
     * @param url    请求地址
     * @param params 请求参数
     * @param type   请求类型
     * @return
     * @author xionghl
     * @date 2017/9/15
     */
    public static JSONObject post(String url, Map<String, String> params, String type) throws IOException {
        JSONObject result = null;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        /*装填参数*/
        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nameValuePairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }

        /*设置参数到请求对象中*/
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, "utf-8"));

        /*设置header信息*/
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        //获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            String entices = EntityUtils.toString(entity);
            result = JSONObject.fromObject(entices);
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        return result;
    }

    public static JSONObject get(String url) {
        // get请求返回结果
        JSONObject jsonResult = null;
        CloseableHttpClient client = HttpClients.createDefault();
        // 发送get请求
        HttpGet request = new HttpGet(url);
        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(2000).setConnectTimeout(2000).build();
        request.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = client.execute(request);

            //请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //读取服务器返回过来的json字符串数据
                HttpEntity entity = response.getEntity();
                String strResult = EntityUtils.toString(entity, "utf-8");
                //把json字符串转换成json对象
                jsonResult = JSONObject.fromObject(strResult);
            } else {
                logger.error("get请求提交失败:" + url);
            }
        } catch (IOException e) {
            logger.error("get请求提交失败:" + url, e);
        } finally {
            request.releaseConnection();
        }
        return jsonResult;
    }

    public static void main(String[] args) {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+Global.APPID+"&secret="+Global.SECRET;
        JSONObject object = HttpsUtils.get(url);
        System.out.println(object.get("access_token"));
    }
}
