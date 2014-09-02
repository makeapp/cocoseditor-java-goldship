package com.makeapp.game.shipgold;

import org.ccj.Director;
import org.ccj.Scheduler;
import org.ccj.d2.Sprite;
import org.ccj.d2.action.RepeatForever;
import org.ccj.d2.action.RotateBy;
import org.ccj.editor.cce.Bind;
import org.ccj.editor.cce.NodeController;
import org.ccj.editor.cce.NodeReader;

/**
 * ControllerLoading.
 */
public class ControllerLoading
        extends NodeController {

    @Bind(tag = 4000)
    public Sprite light;

    @Override
    public void onEnter() {
        super.onEnter();
        light.runAction(RepeatForever.create(RotateBy.create(5, 360)));
        owner.scheduleOnce(new Scheduler.SchedulerCallback() {
            @Override
            public void onUpdate(float delta) {
                super.onUpdate(delta);
                Director director = Director.getInstance();
                director.replaceScene(NodeReader.create().readScene("layout/MainScene.cce"));
            }
        }, 3);
    }

}

