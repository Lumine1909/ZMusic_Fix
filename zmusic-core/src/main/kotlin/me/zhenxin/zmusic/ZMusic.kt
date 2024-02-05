package me.zhenxin.zmusic

import me.zhenxin.zmusic.config.initConfig
import me.zhenxin.zmusic.platform.Logger
import me.zhenxin.zmusic.utils.httpGet
import java.io.File
import kotlin.concurrent.thread

/**
 * ZMusic
 *
 * @author 真心
 * @since 2023/5/23 16:57
 * @email qgzhenxin@qq.com
 */
object ZMusic {

    private const val LOGO = "" +
            "  ______  __  __                 _        \n" +
            " |___  / |  \\/  |               (_)       \n" +
            "    / /  | \\  / |  _   _   ___   _    ___ \n" +
            "   / /   | |\\/| | | | | | / __| | |  / __|\n" +
            "  / /__  | |  | | | |_| | \\__ \\ | | | (__ \n" +
            " /_____| |_|  |_|  \\__,_| |___/ |_|  \\___|\n"

    /**
     * 插件启用
     */
    fun onEnable() {
        LOGO.split("\n").forEach { logger.info("&b$it") }
        logger.info("\t&6v${ZMusicConstants.PLUGIN_VERSION}\tby ZhenXin")
        logger.info("")
        logger.info("ZMusic is loading...")

        initConfig()

        thread {
            httpGet("https://api.zplu.cc/version?plugin=zmusic&type=dev")
        }

        logger.info("ZMusic is enabled.")
    }

    /**
     * 插件禁用
     */
    fun onDisable() {
        logger.info("ZMusic is disabled.")
    }
}

/**
 * 插件数据文件夹
 */
lateinit var dataFolder: File

/**
 * 日志
 */
lateinit var logger: Logger
