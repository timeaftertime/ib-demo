package cn.milai.ibdemo.container.plugin.form;

import org.springframework.stereotype.Component;

import cn.milai.ib.container.plugin.ui.screen.form.MouseLocationMapping;
import cn.milai.ib.container.plugin.ui.screen.form.MouseMapping;
import cn.milai.ibdemo.container.plugin.control.CmdCode;

/**
 * {@link MouseMapping} 示例实现
 * @author milai
 * @date 2021.05.15
 */
@Component
public class DemoMouseMapping extends MouseLocationMapping {

	@Override
	protected int clickedCmdType() {
		return CmdCode.CLICK.getValue();
	}

	@Override
	protected int movedCmdTYpe() {
		return CmdCode.OVER.getValue();
	}
}
