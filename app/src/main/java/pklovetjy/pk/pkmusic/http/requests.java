package pklovetjy.pk.pkmusic.http;


import android.util.Base64;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import pklovetjy.pk.pkmusic.utils.FileUtils;
import pklovetjy.pk.pkmusic.utils.Song;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class requests {

    private static final String SPLASH_URL = "http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
    private static final String BASE_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting";
    private static final String METHOD_GET_MUSIC_LIST = "baidu.ting.billboard.billList";
    private static final String METHOD_DOWNLOAD_MUSIC = "baidu.ting.song.play";
    private static final String METHOD_ARTIST_INFO = "baidu.ting.artist.getInfo";
    private static final String METHOD_SEARCH_MUSIC = "baidu.ting.search.catalogSug";
    private static final String METHOD_LRC = "baidu.ting.song.lry";
    private static final String PARAM_METHOD = "method";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_SIZE = "size";
    private static final String PARAM_OFFSET = "offset";
    private static final String PARAM_SONG_ID = "songid";
    private static final String PARAM_TING_UID = "tinguid";
    private static final String PARAM_QUERY = "query";


    public static String dedata(String str) {

        byte[] b1 = Base64.decode(str.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        for (int i = 0; i < b1.length; i++) {
            b1[i] ^= 0x77;
        }
        String b_str = new String(b1, StandardCharsets.UTF_8);
        String[] chars1 = b_str.split(",");
        int[] char_int1 = new int[chars1.length];
        for (int i = 0; i < chars1.length; i++) {
            char_int1[i] = Integer.parseInt(chars1[i]);
        }
        StringBuilder sbu3 = new StringBuilder();
        for (int i = 0; i < char_int1.length; i++) {
            if (char_int1[i] % 2 == 0) {
                sbu3.append(char_int1[i]).append(",");
            } else {
                sbu3.append(char_int1[i]).append(",");
                do
                    i++;
                while (char_int1[i] % 2 != 0);
            }
        }
        sbu3.deleteCharAt(sbu3.lastIndexOf(","));
        String[] chars5 = sbu3.toString().split(",");
        int[] char_int5 = new int[chars5.length];
        for (int i = 0; i < chars5.length; i++) {
            int pd = Integer.parseInt(chars5[i]);
            if (pd % 2 == 0) {
                char_int5[i] = pd - 40;
            } else {
                char_int5[i] = pd + 40;
            }
        }
        StringBuffer sbu6 = new StringBuffer();
        for (int i : char_int5) {
            sbu6.append((char) i);
        }

        return String.valueOf(sbu6);
    }

    public static String endata(String key) {
        key = new String(key.getBytes(), StandardCharsets.UTF_8);
        int[] char_int = new int[key.length()];
        char[] chars = key.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char_int[i] = (int) chars[i];
        }
        StringBuilder sbu1 = new StringBuilder();
        for (int i = 0; i < char_int.length; i++) {
            if (char_int[i] % 2 == 0) {
                char_int[i] += 40;
                sbu1.append(char_int[i]).append(",");
            } else {
                char_int[i] -= 40;
                sbu1.append(char_int[i]).append(",");
                Random random = new Random();
                int ranint;
                do {
                    ranint = random.nextInt(1000);
                    sbu1.append(ranint).append(",");
                } while (ranint % 2 != 0);
            }
        }
        sbu1.deleteCharAt(sbu1.lastIndexOf(","));
        byte[] b1 = sbu1.toString().getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < b1.length; i++) {
            b1[i] ^= 0x77;
        }
        return Base64.encodeToString(b1, Base64.DEFAULT);
    }

    private static ArrayList<String> getAllSatisfyStr(String str, String regex) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        ArrayList<String> allSatisfyStr = new ArrayList<>();
        if (regex == null || regex.isEmpty()) {
            allSatisfyStr.add(str);
            return allSatisfyStr;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            allSatisfyStr.add(matcher.group());
        }
        return allSatisfyStr;
    }

    public static class RedirectInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            HttpUrl beforeUrl = request.url();
            Response response = chain.proceed(request);
            HttpUrl afterUrl = response.request().url();

            //1.根据url判断是否是重定向
            if (!beforeUrl.equals(afterUrl)) {
                //处理两种情况 1、跨协议 2、原先不是GET请求。
                if (!beforeUrl.scheme().equals(afterUrl.scheme()) || !request.method().equals("GET")) {
                    //重新请求
                    Request newRequest = request.newBuilder().url(response.request().url()).build();
                    response = chain.proceed(newRequest);
                }
            }
            return response;
        }
    }

    public static Map<String, String> enHeaders(String headerStr) {
        String[] headLines = headerStr.split("\n");
        HashMap<String, String> headers = new HashMap<String, String>();
        for (String line : headLines) {
            int lineint = line.indexOf(":");
            if (lineint == -1 || lineint == 0) {

            } else {
                headers.put(line.substring(0, lineint), line.substring(lineint + 1));
            }
        }
        return headers;
    }

    public static String urlEncode(String url, String parms) {
        if (parms == null || parms == "") {
            return url;
        }
        String encodeName = "";
        String[] parms_s = parms.split("&");
        for (String i : parms_s) {
            try {
                if (i.equals(parms_s[parms_s.length - 1])) {
                    encodeName += i.substring(0, i.indexOf("=") + 1) + URLEncoder.encode(i.substring(i.indexOf("=") + 1, i.length()), "utf-8");
                } else {
                    encodeName += i.substring(0, i.indexOf("=") + 1) + URLEncoder.encode(i.substring(i.indexOf("=") + 1, i.length()), "utf-8") + "&";
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String urlandparm = url + "?" + encodeName;

        return urlandparm;
    }

    public static ResponseBody doGet(String url, String parm, String headerJsonStr) {
        url = requests.urlEncode(url, parm);
        Map<String, String> headers = enHeaders(headerJsonStr);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new HttpInterceptor())
                .build();
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body();
        } catch (IOException p) {
            p.printStackTrace();
            return null;
        }
    }

    public static byte[] doGet(String url, String parm) {
        url = requests.urlEncode(url, parm);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().bytes();
        } catch (IOException p) {
            p.printStackTrace();
            return null;
        }
    }

    public static ResponseBody doPost(String url, String headerjson, String datajson) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new HttpInterceptor())
                .followRedirects(false).addInterceptor(new RedirectInterceptor())
                .build();
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), datajson);
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(enHeaders(headerjson)))
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {

            return response.body();
        } catch (IOException p) {
            p.printStackTrace();
        }
        return null;
    }
    public static String doPost(String url, String datajson) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new HttpInterceptor())
                .followRedirects(false).addInterceptor(new RedirectInterceptor())
                .build();
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), datajson);
        Request request = new Request.Builder()
                .url(url)
//                .headers(Headers.of(enHeaders(headerjson)))
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {

            return response.body().string();
        } catch (IOException p) {
            p.printStackTrace();
        }
        return null;
    }

    public static String getKuWoSongLrc(String Title,String Singer,int Duration) {
        System.out.println("--------------------------------------------------------------------------------------------------->");
        System.out.println(Duration);
        String urlStr = "http://lyrics.kugou.com/search";
        String parm = "ver=1&man=yes&client=pc&keyword=" + Title + "-" +Singer+ "&duration=" + Duration + "&hash=";
        String result = new String(Objects.requireNonNull(requests.doGet(urlStr, parm)));
        JSONArray json = JSON.parseObject(result).getJSONArray("candidates");
        for (int i=0;i<json.size();i++) {
            String singer = String.valueOf(json.getJSONObject(i).get("singer"));
            JSONObject data = json.getJSONObject(i);
            if (singer.contains(Singer)){
                urlStr = "http://lyrics.kugou.com/download";
                parm = "ver=1&client=pc&id=" + data.get("id") + "&accesskey=" + data.get("accesskey") + "&fmt=lrc&charset=utf8";

                break;
            }else if(i==json.size()-1){
                urlStr = "http://lyrics.kugou.com/download";
                parm = "ver=1&client=pc&id=" + json.getJSONObject(0).get("id") + "&accesskey=" + json.getJSONObject(0).get("accesskey") + "&fmt=lrc&charset=utf8";

                break;
            }
        }
        result = new String(Objects.requireNonNull(requests.doGet(urlStr, parm)), StandardCharsets.UTF_8);
        byte[] data = Base64.decode(String.valueOf(JSONObject.parseObject(result).get("content")),Base64.DEFAULT);
        result =new String(data);
        FileUtils.saveFile(FileUtils.getLrcDir()+(Title+"_"+Singer+".lrc"),result);
        return result;
//
    }

    public static ArrayList<Song> jingQuePiPei(ArrayList<Song> arrayList, String key){
        key = key.replaceAll(" ","");
        String k;
        String[] keys=key.split("");
        String[] ks;
        ArrayList<Float> ns=new ArrayList<Float>();
        for(int i =0;i<arrayList.size();i++){
            if(arrayList.get(i).getId().equals("") || arrayList.get(i).getId().equals("null") ||arrayList.get(i).getId()==null){
                arrayList.remove(i);
                i--;
                continue;
            }
            k= arrayList.get(i).getTitle().replaceAll(" ","")+arrayList.get(i).getSinger().replaceAll(" ","");
            ks=k.split("");
            int num=0;
            for (String x:ks){
                if (key.contains(x)){
                    num++;
                }
            }
            ns.add((float) ((float)num/(key.length()+(k.length()*2))));
            for (int j=0;j<i;j++){
                if (ns.get(i)>ns.get(j)){
                    Song s_re = arrayList.get(i);
                    arrayList.remove(i);
                    arrayList.add(j,s_re);
                    float f_re = ns.get(i);
                    ns.remove(i);
                    ns.add(j,f_re);
                    break;
                }
            }
        }
//        for (float f:ns){
//            System.out.println("-----------------"+f);
//        }
//        for (Song song:arrayList){
//            System.out.println(song.getTitle()+"-----------------"+song.getSinger());
//        }
        return arrayList;
    }

    public static ArrayList<Song> json2obj(JSONArray jsonArray){
        ArrayList<Song> arrayList = new ArrayList<Song>();
        Song song;
        for (int i=0;i<jsonArray.size();i++){
            JSONObject json = jsonArray.getJSONObject(i);

            song = new Song();

                song.setId(String.valueOf(json.get("id")));
            if (isNumble(String.valueOf(json.get("duration")))){
                song.setDuration(Integer.parseInt(String.valueOf(json.get("duration"))));
            }else {
                System.out.println(String.valueOf(json));
            }
            song.setSinger(String.valueOf(json.get("singer")));
            song.setSize(String.valueOf(json.get("size")));
            song.setAlbum(String.valueOf(json.get("album")));
            song.setSource(String.valueOf(json.get("source")));
            song.setTitle(String.valueOf(json.get("title")));
            arrayList.add(song);
        }
        return arrayList;
    }

    private static boolean isNumble(String str){
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher((CharSequence) str);
        boolean result = matcher.matches();
        if (result) {
            System.out.println("true");
            return true;
        } else {
            System.out.println("false");
            return false;
        }
    }

}
