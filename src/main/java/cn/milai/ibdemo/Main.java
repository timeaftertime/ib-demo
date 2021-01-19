package cn.milai.ibdemo;

import cn.milai.ib.InfinityBattle;
import cn.milai.ib.InfinityBattleApplication;

/**
 * 启动类
 * @author milai
 */
@InfinityBattleApplication
public class Main {

	public static void main(String[] args) {
		InfinityBattle.run(Main.class, args);
	}
}
