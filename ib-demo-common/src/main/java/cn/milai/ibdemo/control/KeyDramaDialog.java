package cn.milai.ibdemo.control;

import java.util.Map;

import cn.milai.ib.container.Container;
import cn.milai.ib.container.plugin.control.cmd.Cmd;
import cn.milai.ib.control.text.DramaDialog;
import cn.milai.ibdemo.container.plugin.control.CmdCode;

/**
 * 通过按键翻页的 {@link DramaDialog}
 * @author milai
 * @date 2021.05.15
 */
public class KeyDramaDialog extends DramaDialog {

	public KeyDramaDialog(int x, int y, Container container, Map<String, Object> params) {
		super(x, y, container, params);
	}

	@Override
	protected boolean isPageDownCmd(Cmd cmd) {
		return cmd.getType() == CmdCode.U_A.getValue();
	}

}
