package cn.milai.ibdemo.endless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import cn.milai.ib.IBBeans;
import cn.milai.ib.container.Stage;
import cn.milai.ib.container.lifecycle.ContainerEventLoopGroup;
import cn.milai.ib.mode.AbstractGameMode;

/**
 * 无尽模式
 * @author milai
 */
@Order(100)
@Component
public class EndlessBattleMode extends AbstractGameMode {

	private static final Logger LOG = LoggerFactory.getLogger(EndlessBattleMode.class);

	private static final String THREAD_NAME = "EndlessBattle";

	private Stage stage;
	private ContainerEventLoopGroup eventLoopGroup;

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
			eventLoopGroup = new ContainerEventLoopGroup(1);
			eventLoopGroup.next().register(stage).awaitUninterruptibly();
			endlessBattle.run();
		} catch (Exception e) {
			LOG.debug("捕获异常并退出", e);
			eventLoopGroup.shutdownGracefully();
		}
	}

}
