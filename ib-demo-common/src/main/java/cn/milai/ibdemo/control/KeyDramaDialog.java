package cn.milai.ibdemo.control;

import java.util.Map;

import org.springframework.core.annotation.Order;

import cn.milai.ib.config.ConfigAware;
import cn.milai.ib.container.plugin.control.cmd.Cmd;
import cn.milai.ib.control.text.DramaDialog;
import cn.milai.ibdemo.container.plugin.control.CmdCode;

/**
 * 通过按键翻页的 {@link DramaDialog}
 * @author milai
 * @date 2021.05.15
 */
@Order(-100)
public class KeyDramaDialog extends DramaDialog implements ConfigAware {

	public KeyDramaDialog(Map<String, Object> params) {
		super(params);
	}

	@Override
	protected boolean isPageDownCmd(Cmd cmd) {
		return cmd.getType() == CmdCode.U_A.getValue();
	}

	@Override
	public String getConfigCode() { return DramaDialog.class.getName(); }

}
