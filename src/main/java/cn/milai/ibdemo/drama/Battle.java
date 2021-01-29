package cn.milai.ibdemo.drama;

import cn.milai.ib.container.DramaContainer;
import cn.milai.ib.drama.Drama;
import cn.milai.ib.ex.IBException;

/**
 * 剧情战斗流程
 * @author milai
 * @date 2020.03.28
 */
public abstract class Battle {

	protected Drama drama;
	private DramaContainer container;
	private Thread battleThread;
	private volatile boolean closed;

	public Battle(Drama drama, DramaContainer container) {
		this.drama = drama;
		this.container = container;
		closed = false;
	}

	/**
	 * 尝试获取所使用容器，若已经停止，将抛出异常
	 * 所有对 Contaienr 的操作都应该通过该方法获取，以确保及时响应容器的关闭
	 * @return
	 * @throws BattleStoppedException
	 */
	public DramaContainer container() throws BattleStoppedException {
		DramaContainer c = this.container;
		if (c == null) {
			throw new BattleStoppedException();
		}
		return container;
	}

	/**
	 * 通知当前 Battle 停止
	 * 实际是否停止由 Battle 当前状态是否响应决定
	 */
	public void stop() {
		this.container = null;
		// 使得 WaitUtil 相关操作被中断
		battleThread.interrupt();
		closed = true;
	}

	/**
	 * 开启 Battle 流程，返回是否通关
	 * 流程开始后可以通过 stop() 方法停止
	 */
	public final boolean run() {
		this.battleThread = Thread.currentThread();
		try {
			return doRun();
		} catch (BattleStoppedException e) {
			while (!closed) {
				// 等待 stop() 中的线程中断操作被调用
			}
			// 确保退出时清除线程中断状态
			Thread.interrupted();
			return false;
		}
	}

	/**
	 * 实际执行 Battle 的流程
	 * 若因 getContainer() 抛出 BattleStoppedException 而中断，应该保留线程中断状态
	 * @return 是否通关
	 */
	protected abstract boolean doRun();

	@SuppressWarnings("serial")
	protected static class BattleStoppedException extends IBException {
	}
}
