package cn.milai.ibdemo.story;

import cn.milai.ib.InfinityBattle;
import cn.milai.ib.InfinityBattleApplication;
import cn.milai.ib.autoconfig.annotation.EnableDresserAutoConfig;
import cn.milai.ib.autoconfig.annotation.EnableMediaAutoConfig;
import cn.milai.ib.autoconfig.annotation.EnableUIAutoConfig;

/**
 * 启动类
 * @author milai
 */
@EnableMediaAutoConfig
@EnableUIAutoConfig
@EnableDresserAutoConfig
@InfinityBattleApplication
public class Main {

	public static void main(String[] args) {
		InfinityBattle.run(Main.class, args);
	}
}