package com.makeapp.game.shipgold;

import org.ccj.Director;
import org.ccj.Event;
import org.ccj.Touch;
import org.ccj.audio.AudioEngine;
import org.ccj.base.Ref;
import org.ccj.d2.Label;
import org.ccj.d2.Node;
import org.ccj.d2.Sprite;
import org.ccj.d2.action.*;
import org.ccj.editor.cce.Action;
import org.ccj.editor.cce.Bind;
import org.ccj.editor.cce.NodeController;
import org.ccj.editor.cce.NodeReader;
import org.ccj.math.Size;
import org.ccj.math.Vec2;
import org.ccj.physics.PhysicsBody;
import org.ccj.physics.PhysicsContact;
import org.ccj.physics.PhysicsContactListener;
import org.ccj.ui.Button;
import org.fun.Function;
import org.fun.FunctionFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * ControllerMain
 * 代码出现的FunctionFactory和广告分享等sdk相关 部分有注释掉但不可删除 根据不同的渠道打开
 */
public class ControllerMain
        extends NodeController {
    public static final int WHEEL_TIME = 5;//轮盘转动时间
    @Bind
    public Label score = null;//分数

    @Bind(tag = 1002)
    public Sprite board;//挡板
    @Bind(tag = 399)
    public Label tip;//tip提示
    @Bind(tag = 5555)
    public Sprite groundSprite;//陆地
    @Bind(tag = 298)
    public Sprite dangban;//挡板
    @Bind(tag = 1001)
    public Button pauseButton;//暂停按钮
    @Bind(tag = 8000)
    public Button startButton;//游戏开始按钮
    @Bind(tag = 51)
    private PhysicsBody line2Body;//矿槽金子掉落的开关

    private Size size = null;//屏幕大小
    private float currentTime = 0.0f;//当前时间
    private float lastCreateCarTime = 0.0f;//上一次创建矿车的时间
    private float plankRotate = 0.0f;//挡板的角度
    private float tempRotate = 0.0f;//临时角度变量
    private int goldChangeTag = 0;//金子随时变化的tag
    private int carCount = 0;//矿车的数量
    private int formerCount = 0;//原有的金子数量
    private int ggCount = 0;//掉落在地上的金子数量
    private boolean playGame = false;//游戏暂停或者结束
    List<Sprite> golds = new ArrayList<Sprite>();

    @Override
    public void onEnter() {
        super.onEnter();

        Director director = Director.getInstance();
//        director.pause();
        pauseButton.setTouchEnabled(false);
        //获取屏幕的大小
        size = director.getWinSize().fetch();
        //创建梯子
        showChainTrack();
        //设置陆地碰撞参数
        setGroundCollsition();
        //挡板角度初始化
        setPlank();
        //金子从正上方掉落下来
        showAllGold();
        //金子所在矿槽正中央下方的线条，点击屏幕可以显示和消失，用来控制金子是否掉落
        Node line2 = Node.create();
        line2Body = PhysicsBody.createEdgeSegment(new Vec2(316, 743), new Vec2(422, 743));
        line2.setPhysicsBody(line2Body);
        owner.addChild(line2);
        AudioEngine.getInstance().playBackgroundMusic("audio/bg.mp3", true);
        this.playGame = false;
        Size size = Director.getInstance().getOpenGLView().getFrameSize().fetch();
        int width = (int) (size.width * 4 / 5);
        int height = width / 6;
        FunctionFactory.callFunction("ad", "show Banner Top " + width + " " + height);
//        FunctionFactory.callFunction("ad", "show Banner Top");
        FunctionFactory.registerFunction("onApplicationDidEnterBackground", new Function() {
            @Override
            public Object apply(Object o) {
                onStop();
                return null;
            }
        });

        FunctionFactory.registerFunction("onApplicationWillEnterForeground", new Function() {
            @Override
            public Object apply(Object o) {
                onRestart();
                return null;
            }
        });

    }

    public void onStop() {
        for (Sprite sprite : golds) {
            PhysicsBody physicsBody = sprite.getPhysicsBody();
            if (physicsBody != null) {
                physicsBody.setEnable(false);
            }
        }
    }

    public void onRestart() {
        for (Sprite sprite : golds) {
            PhysicsBody physicsBody1 = sprite.getPhysicsBody();
            physicsBody1.setEnable(true);
        }
    }

    private void setPlank() {
        plankRotate = board.getRotation();
        PhysicsBody plankSpritePhysicsBody = board.getPhysicsBody();
        plankSpritePhysicsBody.setGravityEnable(false);
        double random = Math.floor(Math.random() * 2) % 2;
        if (random == 0) {
            board.setRotation(-50);
            board.setPosition(new Vec2(400, 500));
        } else {
            board.setRotation(50);
            board.setPosition(new Vec2(320, 500));
        }
        plankRotate = board.getRotation();
    }

    //金子爆炸，金子掉落到地上爆炸碎了
    private void goldExplode(Vec2 loc) {
        final Sprite goldSprite = NodeReader.create().readSprite("layout/GoldExplode.cce");
        if (loc != null && goldSprite != null) {
            goldSprite.setPosition(loc);//爆炸位置
            owner.addChild(goldSprite);
        }
        if (goldSprite != null) {
            goldSprite.runAction(Sequence.create(DelayTime.create(0.3f), new CallFunc() {
                @Override
                public void execute() {
                    super.execute();
                    goldSprite.removeFromParent();
                }
            }));
        }
        AudioEngine.getInstance().playEffect("audio/fallgd.mp3");
    }

    //创建一定数量的金子，设置它的tag(后面获取通过它)和物理碰撞监听器参数
    //本身的位掩码setCategoryBitmask(十六进制)
    //相交时对象的位掩码setContactTestBitmask(十六进制)
    //相交时对象的碰撞位掩码setCollisionBitmask(十六进制)
    private void showAllGold() {
        NodeReader reader = NodeReader.create();
        int goldCountHorizon = 7;
        int goldCountVertical = 7;
        for (int j = 0; j < goldCountHorizon; j++) {//横排
            for (int k = 0; k < goldCountVertical; k++) {//竖排

                String goldName = "Gold" + ((int) Math.floor(Math.random() * 4) + 1) + ".cce";
                Sprite sprite = reader.readSprite("layout/" + goldName);
                sprite.setTag(1000);
                sprite.setPosition(new Vec2(k * 40 + (size.getWidth() / 3 + 20), size.getHeight() - 350 - j * 20));
                PhysicsBody physicsBody1 = sprite.getPhysicsBody();

                physicsBody1.setCategoryBitmask(0x02);
                physicsBody1.setContactTestBitmask(0x08);
                owner.addChild(sprite);

                golds.add(sprite);
            }
        }
        AudioEngine.getInstance().playEffect("audio/down.mp3");
        formerCount = goldCountHorizon * goldCountVertical;//原有金子总数
    }

    //点击页面的暂停按钮弹出暂停窗口
    //暂停窗口弹出来后，暂停按钮不能再点击
    //游戏处于暂停状态
    private void showPauseLayer() {
        final NodeController controller = NodeReader.create().showNode(owner, "layout/Pause.cce");
        controller.setOnExitListener(new Function<Ref, Void>() {
            @Override
            public Void apply(Ref ref) {
                setTouchEnabled(true);
                pauseButton.setTouchEnabled(true);
                onRestart();
                AudioEngine.getInstance().resumeBackgroundMusic();
                return null;
            }
        });
        pauseButton.setTouchEnabled(false);
        setTouchEnabled(false);
        onStop();
        controller.getOwner().setZOrder(1000);
        AudioEngine.getInstance().playEffect("audio/click.mp3");
        AudioEngine.getInstance().pauseBackgroundMusic();
//        FunctionFactory.callFunction("ad", "show Interstitial");
    }

    //游戏结束后弹出的窗口
    //游戏结束窗口弹出后，暂停按钮不能点击
    //屏幕触摸失效，游戏暂停
    private void showGameLayer() {
        final ControllerMessage controller = (ControllerMessage)
                NodeReader.create().showNode(owner, "layout/GameOver.cce");
        controller.score = Integer.parseInt(score.getString());
        setTouchEnabled(false);
        pauseButton.setTouchEnabled(false);
        this.playGame = false;
//        FunctionFactory.callFunction("ad", "show Interstitial");
    }

    //给陆地加入碰撞监听器以及参数
    //陆地本身的位掩码setCategoryBitmask(十六进制)
    //与陆地所要接触的对象的位掩码setContactTestBitmask(十六进制)
    private void setGroundCollsition() {

        PhysicsBody gBody = groundSprite.getPhysicsBody();
        gBody.setCategoryBitmask(0x08);
        gBody.setContactTestBitmask(0x02);
        groundSprite.addPhysicsContactListener(new GroundAndGoldContact());
    }

    //创建左边向上移动的矿车云梯和右边向下移动的矿车云梯
    private void showChainTrack() {
        Sprite leftChain = NodeReader.create().readSprite("layout/FirstChainTrackLeft.cce");
        leftChain.setPosition(new Vec2(55, 660));
        owner.addChild(leftChain);
        Sprite rightChain = NodeReader.create().readSprite("layout/FirstChainTrackRight.cce");
        rightChain.setPosition(new Vec2(670, 660));
        owner.addChild(rightChain);
    }

    //暂停按钮绑定的实现方法，通过tag绑定
    //第二个参数WidgetTouchUp，按钮触摸
    @Bind(tag = 1001)
    @Action(Action.ActionType.WidgetTouchUp)
    public void onPauseTouched(Ref ref) {
        showPauseLayer();
    }

    //游戏开始按钮点击事件，通过tag绑定
    //第二个参数WidgetTouchUp，按钮触摸
    @Bind(tag = 8000)
    @Action(Action.ActionType.WidgetTouchUp)
    public void onStartTouched(Ref ref) {
        setTouchMode(Touch.MODE_ONE_BY_ONE);
        setTouchEnabled(true);
        pauseButton.setTouchEnabled(true);
        startButton.setVisible(false);
        startButton.setTouchEnabled(false);
        AudioEngine.getInstance().playEffect("audio/click.mp3");
        this.playGame = true;
    }

    //屏幕触摸开启后，触摸开始
    @Override
    public boolean onTouchBegan(Touch touch, Event event) {
        Vec2 loc = touch.getLocation();

        if (pauseButton.getBoundingBox().containsPoint(loc)) {
            return false;
        }
        if (dangban != null) {
            ActionInterval moveTo = MoveTo.create(0f, new Vec2(468, 728));
            dangban.runAction(moveTo);
        }
        System.out.println("line2Body.isEnabled()" + line2Body.isEnabled());
        if (line2Body != null) {
            line2Body.setEnable(false);
            System.out.println("open door");
            System.out.println("began line2Body.isEnabled()" + line2Body.isEnabled());
        }
        return true;
    }

    //屏幕触摸开启后，触摸移动
    @Override
    public void onTouchMoved(Touch touch, Event event) {
        super.onTouchMoved(touch, event);
    }

    //屏幕触摸开启后，触摸结束，即离开
    @Override
    public void onTouchEnded(Touch touch, Event event) {
        System.out.println("end line2Body.isEnabled()" + line2Body.isEnabled());
        if (dangban != null) {
            ActionInterval back = MoveTo.create(0f, new Vec2(368, 728));
            dangban.runAction(back);
        }
        if (line2Body != null) {
            line2Body.setEnable(true);
            System.out.println("closed door");
            System.out.println("line2Body.isEnabled()" + line2Body.isEnabled());
        }
        super.onTouchEnded(touch, event);
    }

    //游戏更新方法一直调用
    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        if (!this.playGame) {
            return;
        }
        this.currentTime += delta;
        //陆地所接受的金子数量与刚开始产生的金子数量相等，游戏结束
        if (ggCount == formerCount) {
            showGameLayer();
        }
        //游戏中多余4辆矿车就不产生矿车
        if (carCount < 4) {
            createTramcar();
        }
        //金子挡板角度随时更新
        setPlankRotate(delta);
    }

    @Override
    public void onExit() {
        super.onExit();
        AudioEngine.getInstance().stopAllEffects();
        AudioEngine.getInstance().stopBackgroundMusic(true);
    }

    /**
     * 创建矿车设置它的tag(获取需要通过它)，设置矿车物理碰撞监听器以及物理参数
     * 矿车本身的位掩码setCategoryBitmask(十六进制)
     * 与矿车发生相交的对象的位掩码setContactTestBitmask(十六进制)
     * 矿车移动过程：从右下角--->左下角--->左上角--->右上角--->右下角
     * 移动过程是无限循环过程，移动的过程中接受金子，并将金子倒入矿槽
     * *
     */
    public synchronized void createTramcar() {

        if (this.currentTime - this.lastCreateCarTime >= 5) {

            carCount += 1;
            NodeReader reader = NodeReader.create();
            final Node tramcarNode = reader.readNode("layout/Tramcar.cce");
            tramcarNode.setPosition(new Vec2(675, 180));
            tramcarNode.setTag(0);
            tramcarNode.setZOrder(10);
            PhysicsBody physicsBody2 = tramcarNode.getPhysicsBody();
            physicsBody2.setCategoryBitmask(0x08);
            physicsBody2.setContactTestBitmask(0x02);
            tramcarNode.addPhysicsContactListener(new TramcarAndGoldContact());
            owner.addChild(tramcarNode);
            final Node cheshen = tramcarNode.getChildByTag(100);
            //矿车运行线路
            tramcarNode.runAction(RepeatForever.create(Sequence.create(
                    MoveTo.create(WHEEL_TIME, new Vec2(50, 180)),
                    new CallFunc() {
                        @Override
                        public void execute() {
                            super.execute();
                            goldChangeTag = 0;
                        }
                    },
                    Sequence.create(MoveTo.create(WHEEL_TIME, new Vec2(50, 1100))),
                    Sequence.create(MoveTo.create(2.5f, new Vec2(360, 1100)), new CallFunc() {
                        @Override
                        public void execute() {
                            super.execute();
                            int gold = tramcarNode.getTag();
                            tramcarNode.setTag(0);
                            cheshen.removeAllChildren();
                            tramcarNode.getPhysicsBody().setEnable(true);
                            if (gold > 0 && gold != 1001) {
                                for (int a = 0; a < gold; a++) {
                                    NodeReader reader = NodeReader.create();
                                    String goldName = "Gold" + ((int) Math.floor(Math.random() * 4) + 1) + ".cce";
                                    Sprite sprite = reader.readSprite("layout/" + goldName);
                                    sprite.setTag(1000);
                                    sprite.setZOrder(5);
                                    sprite.setPosition(new Vec2(a * 15 + 300, 1050));
                                    PhysicsBody physicsBody1 = sprite.getPhysicsBody();
                                    physicsBody1.setCategoryBitmask(0x02);
                                    physicsBody1.setContactTestBitmask(0x08);
                                    owner.addChild(sprite);
                                }
                            }
                        }
                    }, Sequence.create(MoveTo.create(2.5f, new Vec2(675, 1100)))),
                    MoveTo.create(WHEEL_TIME, new Vec2(675, 180)))));
            this.lastCreateCarTime = this.currentTime;
        }
    }

    //黄金挡板角度控制
    public void setPlankRotate(float dt) {
        if (plankRotate < 0) {
            if (plankRotate >= -50) {
                tempRotate = -dt * 2;
            } else if (plankRotate <= -70) {
                tempRotate = dt * 2;
            }
        } else {
            if (plankRotate >= 70) {
                tempRotate = -dt * 2;
            } else if (plankRotate <= 50) {
                tempRotate = dt * 2;
            }
        }
//        if (plankRotate >= 30) {
//            tempRotate = -dt * 4;
//        } else if (plankRotate <= -30) {
//            tempRotate = dt * 4;
//        }
        plankRotate += tempRotate;
        board.setRotation(plankRotate);
    }

    /**
     * 创建陆地和金子碰撞监听类
     * 实现物理相交监听这个类里面的方法
     * 首先，注册要使用的方法，用的的方法都要先注册
     * 如：regContactBegin();
     * *
     */
    public class GroundAndGoldContact
            extends PhysicsContactListener {
        {
            regContactBegin();
        }

        @Override
        public boolean onContactBegin(PhysicsContact contact) {
            Node goldNode = contact.getNode(1000);
            Node groundNode = contact.getNode(5555);

            if (goldNode != null && groundNode != null) {
                Vec2 loc = goldNode.getPosition();
                goldExplode(loc);
                goldNode.removeFromParent();
                golds.remove(goldNode);
                ggCount++;
            }
            return false;
        }
    }

    /**
     * 创建矿车和金子碰撞监听类
     * 实现物理相交监听这个类里面的方法
     * 首先，注册要使用的方法，用的的方法都要先注册
     * 如：regContactBegin();
     * *
     */
    public class TramcarAndGoldContact
            extends PhysicsContactListener {
        {
            regContactBegin();
        }

        //设置分数，金子在正下方(小车在水平方向上接受到金子)，每个金子是100分
        //金子在左右两侧(即小车在左边上升或者右边下降过程中接受到金子),每个金子200分
        public void setScore(Vec2 loc) {

            if (loc.getY() > 180.0f) {
                int tempScore = Integer.parseInt(score.getString());
                tempScore += 200;
                score.setString(tempScore + "");
            } else {
                int tempScore = Integer.parseInt(score.getString());
                tempScore += 100;
                score.setString(tempScore + "");
            }
        }

        @Override
        public boolean onContactBegin(PhysicsContact contact) {

            Node gold = contact.getNode(1000);
            Node tramcar = contact.getNode(goldChangeTag);
            if (gold != null && tramcar != null) {
                AudioEngine.getInstance().playEffect("audio/fallcar.mp3");
                FunctionFactory.callFunction("vibrator", "10");

                Vec2 loc = tramcar.getPosition();
                setScore(loc);
                gold.removeFromParent();
                golds.remove(gold);
                goldChangeTag += 1;
                tramcar.setTag(goldChangeTag);
                Sprite sprite = NodeReader.create().readSprite("layout/Gold.cce");
                sprite.setPosition(new Vec2(70 + goldChangeTag * 5, 75));
                sprite.setZOrder(-1);
                Node cheshen = tramcar.getChildByTag(100);
                cheshen.addChild(sprite);
            }
            return false;
        }
    }
}

