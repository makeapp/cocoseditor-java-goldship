package com.makeapp.game.shipgold;

import com.googlecode.javacpp.Pointer;
import org.ccj.Event;
import org.ccj.Touch;
import org.ccj.d2.Sprite;
import org.ccj.d2.action.*;
import org.ccj.editor.cce.Bind;
import org.ccj.editor.cce.NodeController;
import org.ccj.physics.PhysicsBody;
import org.ccj.ui.Widget;

/**
 * ControllerTurn
 */
public class ControllerTurn extends NodeController {

    @Bind(tag = 2001)
    public Sprite leftWheel;

    @Bind(tag = 2002)
    public Sprite rightWheel;

    @Bind(tag = 2005)
    public Sprite tramcarDial;


    public ControllerTurn() {

    }

    public ControllerTurn(Pointer p) {
        super(p);
    }

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
