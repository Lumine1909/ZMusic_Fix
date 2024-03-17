package me.zhenxin.zmusic;

import me.zhenxin.zmusic.dependencies.RuntimeEnv;
import me.zhenxin.zmusic.dependencies.annotation.RuntimeDependency;

import java.util.logging.Logger;

/**
 * ZMusic 运行时依赖
 *
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 2023/7/24 18:14
 */
@SuppressWarnings({"AlibabaClassNamingShouldBeCamel", "SpellCheckingInspection"})
@RuntimeDependency(
        value = "!org.dromara.hutool:hutool-core:" + ZMusicConstants.HUTOOL_VERSION,
        test = "!org.dromara.hutool.core.text.StrUtil"
)
@RuntimeDependency(
        value = "!org.dromara.hutool:hutool-http:" + ZMusicConstants.HUTOOL_VERSION,
        test = "!org.dromara.hutool.http.HttpUtil"
)
@RuntimeDependency(
        value = "!org.dromara.hutool:hutool-json:" + ZMusicConstants.HUTOOL_VERSION,
        test = "!org.dromara.hutool.json.JSONUtil"
)
@RuntimeDependency(
        value = "!io.netty:netty-buffer:" + ZMusicConstants.NETTY_VERSION,
        test = "!io.netty.buffer.ByteBuf"
)
@RuntimeDependency(
        value = "!com.electronwill.night-config:core:" + ZMusicConstants.NIGHT_CONFIG_VERSION,
        test = "!com.electronwill.nightconfig.core.Config"
)
@RuntimeDependency(
        value = "!com.electronwill.night-config:toml:" + ZMusicConstants.NIGHT_CONFIG_VERSION,
        test = "!com.electronwill.nightconfig.toml.TomlFormat"
)
@RuntimeDependency(
        value = "!org.bstats:bstats-base:" + ZMusicConstants.BSTATS_VERSION,
        test = "!me.zhenxin.zmusic.library.bstats.MetricsBase",
        relocate = {"!org.bstats.", "!me.zhenxin.zmusic.library.bstats."}
)
public class ZMusicRuntime {

    public static void setup(String dataFolder, Logger logger, Class<?>... classes) {
        logger.info("Loading libraries, please wait...");
        RuntimeEnv.ENV.setup(dataFolder);
        RuntimeEnv.ENV.inject(ZMusicRuntime.class);
        for (Class<?> clazz : classes) {
            RuntimeEnv.ENV.inject(clazz);
        }
        logger.info("Libraries loaded.");
    }
}
