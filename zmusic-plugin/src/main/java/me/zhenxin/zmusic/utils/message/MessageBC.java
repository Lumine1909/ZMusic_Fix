package me.zhenxin.zmusic.utils.message;

import me.zhenxin.zmusic.ZMusicBC;
import me.zhenxin.zmusic.config.Config;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MessageBC implements Message {

    @Override
    public void sendNormalMessage(String message, Object playerObj) {
        CommandSender sender = (CommandSender) playerObj;
        sender.sendMessage(new TextComponent(Config.prefix + ChatColor.GREEN + message));
    }

    @Override
    public void sendErrorMessage(String message, Object playerObj) {
        CommandSender sender = (CommandSender) playerObj;
        sender.sendMessage(new TextComponent(Config.prefix + ChatColor.RED + message));
    }

    @Override
    public void sendJsonMessage(TextComponent message, Object playerObj) {
        ProxiedPlayer player = (ProxiedPlayer) playerObj;
        player.sendMessage(message);
    }

    @Override
    public void sendActionBarMessage(TextComponent message, Object playerObj) {
        ProxiedPlayer player = (ProxiedPlayer) playerObj;
        player.sendMessage(ChatMessageType.ACTION_BAR, message);
    }

    @Override
    public void sendTitleMessage(String title, String subTitle, Object playerObj) {
        ProxiedPlayer player = (ProxiedPlayer) playerObj;
        Title message = ZMusicBC.plugin.getProxy().createTitle();
        message.title(new TextComponent(title));
        message.subTitle(new TextComponent(subTitle));
        message.fadeIn(0);
        message.stay(200);
        message.fadeOut(20);
        player.sendTitle(message);
    }

    @Override
    public void sendNull(Object playerObj) {
        CommandSender sender = (CommandSender) playerObj;
        sender.sendMessage(new TextComponent(Config.prefix + ChatColor.GREEN + "输入 /zm help 查看帮助."));
    }
}
