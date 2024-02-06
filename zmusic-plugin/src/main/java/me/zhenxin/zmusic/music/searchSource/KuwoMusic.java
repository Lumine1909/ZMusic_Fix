package me.zhenxin.zmusic.music.searchSource;

import com.google.gson.*;
import me.zhenxin.zmusic.utils.NetUtils;

import java.net.URLEncoder;

public class KuwoMusic {
    /**
     * 获取音乐链接
     *
     * @param musicName 音乐名称
     */
    public static JsonObject getMusicUrl(String musicName) {
        try {
            String getUrl = "http://search.kuwo.cn/r.s?all=" + URLEncoder.encode(musicName, "utf-8") + "&ft=music&itemset=web_2013&client=kt&pn=0&rn=1&rformat=json&encoding=utf8";
            String jsonStr = NetUtils.getNetString(getUrl, null);
            jsonStr = jsonStr.replaceAll("&nbsp;", " ");
            Gson gson = new GsonBuilder().create();
            JsonObject json = gson.fromJson(jsonStr, JsonObject.class);
            JsonObject abslist = json.getAsJsonArray("abslist").get(0).getAsJsonObject();
            String musicID = abslist.get("MUSICRID").getAsString().replaceAll("MUSIC_", "");
            String name = abslist.get("NAME").getAsString();
            String singer = abslist.get("ARTIST").getAsString();
            String time = abslist.get("DURATION").getAsString();
            String url = NetUtils.getNetString("http://antiserver.kuwo.cn/anti.s?type=convert_url&format=mp3&rid=" + musicID, null);
            JsonObject returnJson = new JsonObject();
            returnJson.addProperty("url", url);
            returnJson.addProperty("name", name);
            returnJson.addProperty("time", time);
            returnJson.addProperty("singer", singer);
            returnJson.addProperty("lyric", "");
            returnJson.addProperty("lyricTr", "");
            String sb = "酷我音乐暂不支持歌词显示\n" +
                    "酷我音乐暂不支持翻译显示\n";
            returnJson.addProperty("error", sb);
            return returnJson;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取音乐列表
     *
     * @param musicName 音乐名称
     * @return 音乐列表数组
     */
    public static JsonArray getMusicList(String musicName) {
        try {
            String getUrl = "http://search.kuwo.cn/r.s?all=" + URLEncoder.encode(musicName, "utf-8") + "&ft=music&itemset=web_2013&client=kt&pn=0&rn=10&rformat=json&encoding=utf8";
            String jsonStr = NetUtils.getNetString(getUrl, null);
            jsonStr = jsonStr.replaceAll("&nbsp;", " ");
            Gson gson = new GsonBuilder().create();
            JsonObject json = gson.fromJson(jsonStr, JsonObject.class);
            JsonArray abslist = json.getAsJsonArray("abslist");
            JsonArray returnJson = new JsonArray();
            for (JsonElement j : abslist) {
                String name = j.getAsJsonObject().get("NAME").getAsString();
                String singer = j.getAsJsonObject().get("ARTIST").getAsString();
                JsonObject returnJsonObj = new JsonObject();
                returnJsonObj.addProperty("name", name);
                returnJsonObj.addProperty("singer", singer);
                returnJson.add(returnJsonObj);
            }
            return returnJson;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
