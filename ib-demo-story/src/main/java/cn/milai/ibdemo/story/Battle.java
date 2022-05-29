package cn.milai.ibdemo.story;

import cn.milai.ib.ex.IBException;
import cn.milai.ib.plugin.control.PauseSwitcher;
import cn.milai.ib.stage.Stage;
import cn.milai.ib.stage.Waits;

/**
 * 剧情战斗流程
 * @author milai
 * @date 2020.03.28
 */
public abstract class Battle {

	protected DemoDrama drama;
	private Stage stage;
	private Thread battleThread;

	public Battle(DemoDrama drama, Stage stage) {
		this.drama = drama;
		this.stage = stage;
	}

	/**
	 * 尝试获取所使用 {@link Stage} ，若已经停止，将抛出异常
	 * 子类在每次获取容器时应该通过该方法，以及时响应 {@link Stage} 或 {@link Battle} 的关闭
	 * @return
	 * @throws BattleStoppedException
	 */
	public Stage stage() throws BattleStoppedException {
		Stage stage = this.stage;
		if (stage == null || stage.lifecycle().isClosed()) {
			this.stage = null;
			throw new BattleStoppedException();
		}
		return stage;
	}

	/**
	 * 通知运行中的当前 Battle 停止
	 */
	public void stop() {
		this.stage = null;
		Waits.notify(battleThread);
	}

	/**
	 * 开启 Battle 流程，返回是否通关
	 */
	public final boolean run() {
		this.battleThread = Thread.currentThread();
		try {
			stage().addActor(new PauseSwitcher());
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
