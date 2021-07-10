package cn.milai.ibdemo.role;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import cn.milai.ib.InfinityBattleMod;
import cn.milai.ib.config.IBConfig;
import cn.milai.ib.util.YamlProperySourceFactory;

/**
 * Spring {@link Configuration} 类
 * @author milai
 * @date 2021.07.06
 */
@InfinityBattleMod
@PropertySource(
	value = "classpath:ibconfig-demo.yml", encoding = "utf-8", name = IBConfig.CONFIG_PREFIX,
	factory = YamlProperySourceFactory.class
)
public class DemoRoleConfig {

}
