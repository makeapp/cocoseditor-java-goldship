package com.makeapp.game.shipgold;

import org.ccj.d2.Sprite;
import org.ccj.d2.action.DelayTime;
import org.ccj.d2.action.RepeatForever;
import org.ccj.d2.action.RotateBy;
import org.ccj.d2.action.Sequence;
import org.ccj.editor.cce.Bind;
import org.ccj.editor.cce.NodeController;

/**
 * ControllerTurn 运黄金的小车
 */
public class ControllerTurn extends NodeController {

    @Bind(tag = 2001)
    public Sprite leftWheel;

    @Bind(tag = 2002)
    public Sprite rightWheel;

    @Bind(tag = 2005)
    public Sprite tramcarDial;

    @Override
    public void onEnter() {
        super.onEnter();
        leftWheel.runAction(RepeatForever.create(
                Sequence.create(RotateBy.create(ControllerMain.WHEEL_TIME, -1800), DelayTime.create(15))));
        rightWheel.runAction(RepeatForever.create(
                Sequence.create(RotateBy.create(ControllerMain.WHEEL_TIME, -1800), DelayTime.create(15))));
        tramcarDial.runAction(RepeatForever.create(Sequence.create(
                DelayTime.create(ControllerMain.WHEEL_TIME),
                RotateBy.create(ControllerMain.WHEEL_TIME, 1880),
                DelayTime.create(ControllerMain.WHEEL_TIME),
                RotateBy.create(ControllerMain.WHEEL_TIME, -1880))));
    }

    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
    }

    @Override
    public void onExit() {
        super.onExit();
    }
}
