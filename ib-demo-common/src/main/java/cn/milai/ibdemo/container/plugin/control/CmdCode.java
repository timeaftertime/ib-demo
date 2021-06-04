package cn.milai.ibdemo.container.plugin.control;

import cn.milai.ib.container.plugin.control.cmd.Cmd;

/**
 * {@link Cmd} 类型
 * @author milai
 * @date 2021.05.12
 */
public enum CmdCode {

	NOOP(Cmd.NOOP),
	PAUSE(Cmd.PAUSE),
	OVER(Cmd.MOVE),
	CLICK(Cmd.CLICK),

	// 设置 上 指令
	UP(1),
	// 取消 上 指令
	U_UP(2),
	// 设置 下 指令
	DOWN(3),
	// 取消 下 指令
	U_DOWN(4),
	// 设置 左 指令
	LEFT(5),
	// 取消 左 指令
	U_LEFT(6),
	// 设置 右 指令
	RIGHT(7),
	// 取消 右 指令
	U_RIGHT(8),
	// 设置 A 动作指令
	A(9),
	// 取消 A 动作指令
	U_A(10),
	// 设置 B 动作指令
	B(11),
	// 取消 B 动作指令
	U_B(12),
	// 设置 C 动作指令
	C(13),
	// 取消 C 动作指令
	U_C(14),
	// 设置 D 动作指令
	D(15),
	// 取消 D 动作指令
	U_D(16);

	private int value;

	CmdCode(int value) {
		this.value = value;
	}

	public int getValue() { return value; }

	/**
	 * 获取 {@code value} 对应的 {@link CmdCode} ，若不存在，抛出异常
	 * @param value
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static CmdCode of(int value) {
		CmdCode find = find(value);
		if (find == null) {
			throw new IllegalArgumentException("未知 value:" + value);
		}
		return find;
	}

	/**
	 * 获取 {@code value} 对应的 {@link CmdCode} ，若不存在，返回 {@code null}
	 * @param value
	 * @return
	 */
	public static CmdCode find(int value) {
		for (CmdCode type : values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		return null;
	}

}
