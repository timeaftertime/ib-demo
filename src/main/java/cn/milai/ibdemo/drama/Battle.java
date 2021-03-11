package cn.milai.ibdemo.drama;

import cn.milai.ib.container.Container;
import cn.milai.ib.container.DramaContainer;
import cn.milai.ib.drama.Drama;
import cn.milai.ib.ex.IBException;
import cn.milai.ib.util.Waits;

/**
 * 剧情战斗流程
 * @author milai
 * @date 2020.03.28
 */
public abstract class Battle {

	protected Drama drama;
	private DramaContainer container;
	private Thread battleThread;

	public Battle(Drama drama, DramaContainer container) {
		this.drama = drama;
		this.container = container;
	}

	/**
	 * 尝试获取所使用容器，若已经停止，将抛出异常
	 * 子类在每次获取容器时应该通过该方法，以及时响应 {@link Container} 或 {@link Battle} 的关闭
	 * @return
	 * @throws BattleStoppedException
	 */
	public DramaContainer container() throws BattleStoppedException {
		DramaContainer c = this.container;
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
