package me.zhenxin.zmusic.music;

import com.google.gson.*;
import me.zhenxin.zmusic.ZMusic;
import me.zhenxin.zmusic.config.Config;
import me.zhenxin.zmusic.data.PlayerData;
import me.zhenxin.zmusic.music.searchSource.NeteaseCloudMusic;
import me.zhenxin.zmusic.utils.HelpUtils;
import me.zhenxin.zmusic.utils.OtherUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayList {


    /**
     * 歌单子命令
     *
     * @param args   参数
     * @param player 玩家
     */
    public static void subCommand(String[] args, Object player) {
        switch (args[1]) {
            case "next":
                ZMusic.message.sendNormalMessage("正在切换到下一首歌曲,请稍后...", player);
                PlayListPlayer plp = PlayerData.getPlayerPlayListPlayer(player);
                if (plp != null) {
                    plp.nextMusic = true;
                } else {
                    ZMusic.message.sendErrorMessage("错误: 当前未在播放歌单", player);
                }
                return;
            case "prev":
                ZMusic.message.sendNormalMessage("正在切换到上一首歌曲,请稍后...", player);
                PlayListPlayer plp2 = PlayerData.getPlayerPlayListPlayer(player);
                if (plp2 != null) {
                    plp2.prevMusic = true;
                } else {
                    ZMusic.message.sendErrorMessage("错误: 当前未在播放歌单", player);
                }
                return;
            case "jump":
                int jumpSong = 1;
                String id = "";
                try {
                    jumpSong = Integer.parseInt(args[2]);
                } catch (Exception ignored) {
                }
                ZMusic.message.sendNormalMessage("正在跳转到当前歌单中ID为§r[§e" + jumpSong + "§r]§a的歌曲,请稍后...", player);
                PlayListPlayer plp3 = PlayerData.getPlayerPlayListPlayer(player);
                if (plp3 != null) {
                    try {
                        id = args[3];
                    } catch (Exception ignored) {
                    }
                    if (!id.isEmpty()) {
                        if (!id.equals(plp3.id)) {
                            ZMusic.message.sendErrorMessage("错误: 当前未在播放此歌单", player);
                            return;
                        }
                    }
                    plp3.jumpSong = jumpSong;
                    plp3.jumpMusic = true;
                } else {
                    ZMusic.message.sendErrorMessage("错误: 当前未在播放歌单", player);
                }
                return;
            case "type":
                switch (args[2]) {
                    case "normal":
                        PlayerData.setPlayerPlayListType(player, args[2]);
                        ZMusic.message.sendNormalMessage("成功将歌单播放类型设置为[§e顺序播放§a].", player);
                        break;
                    case "loop":
                        PlayerData.setPlayerPlayListType(player, args[2]);
                        ZMusic.message.sendNormalMessage("成功将歌单播放类型设置为[§e循环播放§a].", player);
                        break;
                    case "random":
                        PlayerData.setPlayerPlayListType(player, args[2]);
                        ZMusic.message.sendNormalMessage("成功将歌单播放类型设置为[§e随机播放§a].", player);
                        break;
                    default:
                        ZMusic.message.sendErrorMessage("错误: 未知的播放类型", player);
                        ZMusic.message.sendErrorMessage("/zm playlist type normal - 顺序播放", player);
                        ZMusic.message.sendErrorMessage("/zm playlist type loop - 循环播放", player);
                        ZMusic.message.sendErrorMessage("/zm playlist type random - 随机播放", player);
                        break;
                }
                return;
            case "global":
                String platform = "";
                if (args.length >= 3) {
                    platform = args[2];
                    switch (platform) {
                        case "163":
                        case "netease":
                            platform = "netease";
                            break;
                        case "qq":
                            break;
                        default:
                            ZMusic.message.sendErrorMessage("错误：未知的平台", player);
                            return;
                    }
                } else {
                    HelpUtils.sendHelp("playlist", player);
                    return;
                }
                if (args.length >= 4) {
                    switch (args[3]) {
                        case "import":
                            if (ZMusic.player.hasPermission(player, "zmusic.admin")) {
                                importPlayList(args[4], platform, player, true);
                                break;
                            } else {
                                ZMusic.message.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", player);
                                break;
                            }
                        case "list":
                            showPlayListList(platform, player, true);
                            break;
                        case "show":
                            int start = 0;
                            try {
                                start = Integer.parseInt(args[5]);
                            } catch (Exception ignored) {
                            }
                            showPlayList(args[4], platform, player, true, start);
                            break;
                        case "play":
                            OtherUtils.resetPlayerStatus(player);
                            playPlayList(args[4], platform, player, new ArrayList<>(), true);
                            break;
                        case "playall":
                            List<Object> players = ZMusic.player.getOnlinePlayerList();
                            for (Object p : players) {
                                OtherUtils.resetPlayerStatus(p);
                            }
                            playPlayList(args[4], platform, null, players, true);
                            break;
                        case "update":
                            if (ZMusic.player.hasPermission(player, "zmusic.admin")) {
                                updatePlayListInfo(platform, player, true);
                                break;
                            } else {
                                ZMusic.message.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", player);
                                break;
                            }
                        default:
                            HelpUtils.sendHelp("playlist", player);
                            break;
                    }
                } else {
                    HelpUtils.sendHelp("playlist", player);
                    return;
                }
                return;
            default:
                break;
        }
        if (args.length >= 3) {
            String platform = args[1];
            switch (platform) {
                case "163":
                case "netease":
                    platform = "netease";
                    break;
                case "qq":
                    ZMusic.message.sendErrorMessage("由于不可抗力因素。", player);
                    ZMusic.message.sendErrorMessage("QQ音乐搜索源已于2.5.0版本移除, API服务已关闭。", player);
                    return;
                default:
                    ZMusic.message.sendErrorMessage("错误：未知的平台", player);
                    return;
            }
            switch (args[2]) {
                case "import":
                    importPlayList(args[3], platform, player, false);
                    break;
                case "list":
                    showPlayListList(platform, player, false);
                    break;
                case "show":
                    int start = 0;
                    try {
                        start = Integer.parseInt(args[4]);
                    } catch (Exception ignored) {
                    }
                    showPlayList(args[3], platform, player, false, start);
                    break;
                case "play":
                    OtherUtils.resetPlayerStatus(player);
                    playPlayList(args[3], platform, player, new ArrayList<>(), false);
                    break;
                case "update":
                    updatePlayListInfo(platform, player, false);
                    break;
                default:
                    HelpUtils.sendHelp("playlist", player);
                    break;
            }
        } else {
            HelpUtils.sendHelp("playlist", player);
        }
    }

    private static void importPlayList(String url, String platform, Object player, boolean isGlobal) {
        ZMusic.message.sendNormalMessage("正在导入歌单，可能时间较长，请耐心等待...", player);
        try {
            JsonObject json;
            String id;
            String platformName;
            switch (platform) {
                case "netease":
                    id = url.split("playlist\\?id=")[1].split("&")[0];
                    json = NeteaseCloudMusic.getMusicSongList(id);
                    platformName = "网易云音乐";
                    break;
                default:
                    ZMusic.message.sendErrorMessage("错误：未知的平台", player);
                    return;
            }
            String name = json.get("name").getAsString();
            int songs = json.get("songs").getAsInt();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String data = gson.toJson(json);
            String filePath;
            if (isGlobal) {
                filePath = ZMusic.dataFolder
                        + "/playlist/global/" + platform;
            } else {
                filePath = ZMusic.dataFolder
                        + "/playlist/" + platform + "/" + ZMusic.player.getName(player);
            }
            File path = new File(filePath);
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(filePath, id + ".json");
            OtherUtils.saveStringToLocal(file, data);
            ZMusic.log.sendDebugMessage(file.getAbsolutePath());
            ZMusic.message.sendNormalMessage("成功在" + platformName + "导入(§e" + name + "§a)共计§e" + songs + "§a首。", player);
            ZMusic.message.sendNormalMessage("其中可能包含无版权或VIP音乐。", player);
        } catch (Exception e) {
            e.printStackTrace();
            ZMusic.message.sendErrorMessage("导入失败,请检查链接格式是否正确.", player);
            ZMusic.message.sendErrorMessage("QQ音乐: https://y.qq.com/n/yqq/playlist/1937967578.html", player);
            ZMusic.message.sendErrorMessage("网易云音乐: https://music.163.com/#/my/m/music/playlist?id=363046232", player);
        }
    }

    private static void showPlayListList(String platform, Object player, boolean isGlobal) {
        ArrayList<String> files;
        String filePath = "";
        if (isGlobal) {
            filePath = ZMusic.dataFolder
                    + "/playlist/global/" + platform;
        } else {
            filePath = ZMusic.dataFolder
                    + "/playlist/" + platform + "/" + ZMusic.player.getName(player);
        }
        files = OtherUtils.queryFileNames(filePath);
        try {
            ZMusic.message.sendNormalMessage("§6=========================================", player);
            if (files != null) {
                int i = 0;
                for (String s : files) {
                    i++;
                    Gson gson = new GsonBuilder().create();
                    File file = new File(filePath, s);
                    String id = s.split(".json")[0];
                    JsonObject j = gson.fromJson(OtherUtils.readFileToString(file), JsonObject.class);
                    String name = j.get("name").getAsString();
                    String songs = j.get("songs").getAsString();
                    TextComponent message = new TextComponent(Config.prefix + "§a" + i + "." + id + " : " + name + "(§e共" + songs + "首§a)");
                    TextComponent play = new TextComponent("§r[§e播放§r]§r");
                    TextComponent show = new TextComponent("§r[§e查看§r]§r");
                    if (isGlobal) {
                        play.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist global " + platform + " play " + id));
                        show.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist global " + platform + " show " + id));
                    } else {
                        play.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist " + platform + " play " + id));
                        show.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist " + platform + " show " + id));
                    }
                    play.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击播放此歌单").create()));
                    show.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击查看此歌单").create()));
                    message.addExtra(" ");
                    message.addExtra(play);
                    message.addExtra(" ");
                    message.addExtra(show);
                    if (ZMusic.player.hasPermission(player, "zmusic.admin")) {
                        TextComponent playAll = new TextComponent("§r[§e全服播放§r]§r");
                        if (isGlobal) {
                            playAll.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist global " + platform + " playall " + id));
                            playAll.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击为全体玩家播放此歌单").create()));
                            message.addExtra(" ");
                            message.addExtra(playAll);
                        }
                    }
                    ZMusic.message.sendJsonMessage(message, player);
                }
            } else {
                ZMusic.message.sendErrorMessage("错误: 在当前平台未导入过任何歌单", player);
            }
        } catch (Exception e) {
            e.printStackTrace();
            TextComponent message = new TextComponent(Config.prefix + "§c错误: 读取文件错误,请尝试");
            TextComponent update = new TextComponent("§r[§e更新歌单§r]§r");
            if (isGlobal) {
                update.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist global " + platform + " update"));
            } else {
                update.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist " + platform + " update"));
            }
            update.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击更新此平台的全部歌单").create()));
            message.addExtra(update);
            ZMusic.message.sendJsonMessage(message, player);
        }
        ZMusic.message.sendNormalMessage("§6=========================================", player);
    }

    private static void showPlayList(String id, String platform, Object player, boolean isGlobal, int start) {
        JsonObject json;
        Gson gson = new GsonBuilder().create();
        String filePath = "";
        File file;
        if (isGlobal) {
            filePath = ZMusic.dataFolder
                    + "/playlist/global/" + platform + "/" + id + ".json";
        } else {
            filePath = ZMusic.dataFolder
                    + "/playlist/" + platform + "/" + ZMusic.player.getName(player) + "/" + id + ".json";
        }
        file = new File(filePath);
        json = gson.fromJson(OtherUtils.readFileToString(file), JsonObject.class);
        JsonArray list = json.get("list").getAsJsonArray();
        TextComponent messageStart = new TextComponent(Config.prefix + "§6================");
        TextComponent messageEnd = new TextComponent("§6=================");
        TextComponent prev = new TextComponent("§r[§e上一页§r]§r");
        if ((start - 10) >= 0) {
            if (isGlobal) {
                prev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist global " + platform + " show " + id + " " + (start - 10)));
            } else {
                prev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist " + platform + " show " + id + " " + (start - 10)));
            }
            prev.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击返回上一页").create()));
        } else {
            prev.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b已经到达第一页").create()));
        }
        TextComponent next = new TextComponent("§r[§e下一页§r]§r");
        if ((start + 10) < list.size()) {
            if (isGlobal) {
                next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist global " + platform + " show " + id + " " + (start + 10)));
            } else {
                next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist " + platform + " show " + id + " " + (start + 10)));
            }
            next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击前往下一页").create()));
        } else {
            next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b已经到达最后一页").create()));
        }
        TextComponent hr = new TextComponent();
        hr.addExtra(messageStart);
        hr.addExtra(prev);
        hr.addExtra(messageEnd);
        ZMusic.message.sendJsonMessage(hr, player);
        for (int i = start; i < list.size(); i++) {
            if (i == start + 10) {
                break;
            }
            JsonObject info = list.get(i).getAsJsonObject();
            String name = info.get("name").getAsString();
            String singer = info.get("singer").getAsString();
            String mid = info.get("id").getAsString();
            TextComponent message = new TextComponent(Config.prefix + "§a" + (i + 1) + "." + name + " - " + singer);
            TextComponent play = new TextComponent("§r[§e播放§r]§r");
            TextComponent music = new TextComponent("§r[§e点歌§r]§r");
            TextComponent jump = new TextComponent("§r[§e跳转§r]§r");
            play.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm play " + platform + " -id:" + mid));
            play.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击播放").create()));
            music.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm music " + platform + " -id:" + mid));
            music.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击点歌").create()));
            jump.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist jump " + (i + 1) + " " + id));
            jump.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击跳转").create()));
            message.addExtra(" ");
            message.addExtra(play);
            message.addExtra(" ");
            message.addExtra(music);
            message.addExtra(" ");
            message.addExtra(jump);
            ZMusic.message.sendJsonMessage(message, player);
        }
        hr = new TextComponent();
        hr.addExtra(messageStart);
        hr.addExtra(next);
        hr.addExtra(messageEnd);
        ZMusic.message.sendJsonMessage(hr, player);
    }

    private static void playPlayList(String id, String platform, Object p, List<Object> players, boolean isGlobal) {
        if (p != null) {
            players.add(p);
        }
        for (Object player : players) {
            String filePath = "";
            if (isGlobal) {
                filePath = ZMusic.dataFolder + "/playlist/global/" + platform;
            } else {
                filePath = ZMusic.dataFolder + "/playlist/" + platform + "/" + ZMusic.player.getName(player);
            }
            File file = new File(filePath, id + ".json");
            Gson gson = new GsonBuilder().create();
            if (!file.exists()) {
                ZMusic.message.sendErrorMessage("错误: 指定歌单不存在", player);
                return;
            }
            JsonObject json = gson.fromJson(OtherUtils.readFileToString(file), JsonObject.class);
            String name = json.get("name").getAsString();
            JsonArray list = json.get("list").getAsJsonArray();
            List<JsonObject> playList = new ArrayList<>();
            for (JsonElement j : list) {
                playList.add(j.getAsJsonObject());
            }
            PlayListPlayer plp = PlayerData.getPlayerPlayListPlayer(player);
            if (plp != null) {
                plp.isStop = true;
                PlayerData.setPlayerPlayListPlayer(player, null);
                OtherUtils.resetPlayerStatus(player);
                ZMusic.music.stop(player);
            }
            PlayListPlayer playListPlayer = new PlayListPlayer();
            String type = PlayerData.getPlayerPlayListType(player);
            if (type == null || type.isEmpty()) {
                type = "normal";
            }
            playListPlayer.id = id;
            playListPlayer.playListName = name;
            playListPlayer.type = type;
            playListPlayer.playList = playList;
            playListPlayer.platform = platform;
            playListPlayer.player = player;
            playListPlayer.init();
            ZMusic.runTask.runAsync(playListPlayer);
            PlayerData.setPlayerPlayListPlayer(player, playListPlayer);
        }
    }

    private static void updatePlayListInfo(String platform, Object player, boolean isGlobal) {
        String filePath = "";
        if (isGlobal) {
            filePath = ZMusic.dataFolder + "/playlist/global/" + platform;
        } else {
            filePath = ZMusic.dataFolder + "/playlist/" + platform + "/" + ZMusic.player.getName(player);
        }
        ArrayList<String> files = OtherUtils.queryFileNames(filePath);
        if (files != null) {
            for (String s : files) {
                ZMusic.message.sendNormalMessage("§6=========================================", player);
                switch (platform) {
                    case "qq":
                        importPlayList("playlist/" + s.split("\\.json")[0] + ".html", platform, player, isGlobal);
                        break;
                    case "netease":
                        importPlayList("playlist?id=" + s.split("\\.json")[0], platform, player, isGlobal);
                        break;
                    default:
                        break;
                }
            }
            ZMusic.message.sendNormalMessage("§6=========================================", player);
        } else {
            ZMusic.message.sendErrorMessage("错误: 在当前平台未导入过任何歌单", player);
        }
    }
}
