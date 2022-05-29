package cn.milai.ibdemo.endless;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import cn.milai.ib.IBBeans;
import cn.milai.ib.lifecycle.LifecycleLoopGroup;
import cn.milai.ib.mode.AbstractGameMode;
import cn.milai.ib.stage.Stage;

/**
 * 无尽模式
 * @author milai
 */
@Order(100)
@Component
public class EndlessBattleMode extends AbstractGameMode {

	private static final String THREAD_NAME = "EndlessBattle";

	private Stage stage;
	private LifecycleLoopGroup eventLoopGroup;

	private EndlessBattle endlessBattle;

	@Override
	public String name() {
		return "无尽模式";
	}

	@Override
	public void init() {
		stage = IBBeans.getBean(Stage.class);
		endlessBattle = new EndlessBattle(stage);
	}

	@Override
	public void run() {
		try {
			setName(THREAD_NAME);
			eventLoopGroup = new LifecycleLoopGroup(1);
			eventLoopGroup.next().register(stage.lifecycle()).awaitUninterruptibly();
			endlessBattle.run();
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}

}
