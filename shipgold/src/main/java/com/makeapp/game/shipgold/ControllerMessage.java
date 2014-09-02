package com.makeapp.game.shipgold;

import org.ccj.Director;
import org.ccj.base.Ref;
import org.ccj.editor.cce.Action;
import org.ccj.editor.cce.Bind;
import org.ccj.editor.cce.NodeController;
import org.ccj.editor.cce.NodeReader;
import org.fun.FunctionFactory;

/**
 * ControllerMessage
 */
public class ControllerMessage
        extends NodeController {

    public int score = 0;

    @Override
    public void onEnter() {
        super.onEnter();
    }

    @Bind("resume")
    @Action(Action.ActionType.WidgetTouchUp)
    public void onResumeTouched(Ref ref) {
        removeFromParent();
    }

    @Bind("restart")
    @Action(Action.ActionType.WidgetTouchUp)
    public void onButtonTouched(Ref ref) {
        Director director = Director.getInstance();
        removeFromParent();
        director.replaceScene(NodeReader.create().readScene("layout/MainScene.cce"));
    }

    @Bind("sharing")
    @Action(Action.ActionType.WidgetTouchUp)
    public void onShareTouched(Ref ref) {
        FunctionFactory.callFunction("weixin", "sendSession appData 运黄金 我得了" + score + "个黄金，你也来试试手气.");
    }

    @Bind("advise")
    @Action(Action.ActionType.WidgetTouchUp)
    public void onAdviseTouched(Ref ref) {
//        FunctionFactory.callFunction("unicompay", "payonline 71664 1 2 2");
        FunctionFactory.callFunction("weixin", "sendSession appData 运黄金 我在玩运黄金游戏，你也来试试.");
        removeFromParent();
    }
}

