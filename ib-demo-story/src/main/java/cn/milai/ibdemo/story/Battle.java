package cn.milai.ibdemo.story;

import cn.milai.ib.container.Container;
import cn.milai.ib.container.Stage;
import cn.milai.ib.container.Waits;
import cn.milai.ib.container.plugin.control.PauseSwitcher;
import cn.milai.ib.ex.IBException;

/**
 * 剧情战斗流程
 * @author milai
 * @date 2020.03.28
 */
public abstract class Battle {

	protected DemoDrama drama;
	private Stage container;
	private Thread battleThread;

	public Battle(DemoDrama drama, Stage container) {
		this.drama = drama;
		this.container = container;
	}

	/**
	 * 尝试获取所使用容器，若已经停止，将抛出异常
	 * 子类在每次获取容器时应该通过该方法，以及时响应 {@link Container} 或 {@link Battle} 的关闭
	 * @return
	 * @throws BattleStoppedException
	 */
	public Stage container() throws BattleStoppedException {
		Stage c = this.container;
		if (c == null || c.isClosed()) {
			this.container = null;
			throw new BattleStoppedException();
		}
		return container;
	}

	/**
	 * 通知运行中的当前 Battle 停止
	 */
	public void stop() {
		this.container = null;
		Waits.notify(battleThread);
	}

	/**
	 * 开启 Battle 流程，返回是否通关
	 */
	public final boolean run() {
		this.battleThread = Thread.currentThread();
		try {
			container().addObject(new PauseSwitcher());
			return doRun();
		} catch (BattleStoppedException e) {
			return false;
		}
	}

	/**
	 * 实际执行 Battle 的流程
	 * @return 是否通关
	 */
	protected abstract boolean doRun();

	@SuppressWarnings("serial")
	protected static class BattleStoppedException extends IBException {
	}
}
