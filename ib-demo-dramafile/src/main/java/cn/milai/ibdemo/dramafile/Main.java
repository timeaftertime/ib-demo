package cn.milai.ibdemo.dramafile;

import cn.milai.ib.InfinityBattle;
import cn.milai.ib.InfinityBattleApplication;
import cn.milai.ib.container.autoconfig.annotation.EnableUIAutoConfig;

/**
 * 启动类
 * @author milai
 */
@EnableUIAutoConfig
@InfinityBattleApplication
public class Main {

	public static void main(String[] args) {
		InfinityBattle.run(Main.class, args);
	}
}