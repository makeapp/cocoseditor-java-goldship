package com.makeapp.game.shipgold;


import org.ccj.*;
import org.ccj.base.FileUtils;
import org.ccj.editor.cce.NodeReader;

/**
 * -Game Engine:Cocos2d-Java
 * -
 * -Game Develop Tool:CocosEditor
 * -
 * -Doc Links
 * http://www.cocoseditor.com/ (Office Website)
 * http://blog.makeapp.co/ (Office Blog)
 * http://blog.csdn.net/touchsnow (csdn Blog)
 * https://github.com/makeapp      (github)
 * -
 * -Support
 * E-Mail:  zuowen@makeapp.co
 * QQ    :  232361142
 * -
 * -
 * author
 * Program write:      dupeiyong
 * Program clear:      touchsnow
 */
public class Main {
    public static void runMain() {
        String lan = Application.getInstance().getCurrentLanguage() == Application.CHINESE ? "img/cn" : "img/en";
        FileUtils.getInstance().addSearchPath(lan);
        Director director = Director.getInstance();
//        director.setDisplayStats(true);
        director.getOpenGLView().setDesignResolutionSize(720, 1280, GLView.POLICY_EXACT_FIT);
        NodeReader nodeReader = NodeReader.create();
        director.runWithScene(nodeReader.readScene("layout/Loading.cce"));
    }

    public static void pause() {
        Director director = Director.getInstance();
        director.pause();
    }

    public static void resume() {
        Director director = Director.getInstance();
        director.resume();
    }

    public static void end() {
        Director director = Director.getInstance();
        director.end();
    }

    public static void main(String[] args) {
        int w = 720 / 2;
        int h = 1280 / 2;

        if (args != null && args.length >= 2) {
            w = Integer.parseInt(args[0]);
            h = Integer.parseInt(args[1]);
        }

        final int width = w;
        final int height = h;

        Application app = new Application() {
            public boolean applicationDidFinishLaunching() {
                if (OS_WINDOWS == getTargetPlatform() || OS_ANDROID == getTargetPlatform() || OS_MAC == getTargetPlatform()) {
                    GLView eglView = GLView.create("CocosPlayer");
                    eglView.setFrameSize(width, height);
                    Director.getInstance().setOpenGLView(eglView);
                }
                runMain();
                return true;
            }

            @Override
            public boolean applicationDidEnterBackground() {
//                FunctionFactory.getInstance().call("onApplicationDidEnterBackground", null);
                return super.applicationDidEnterBackground();
            }

            @Override
            public boolean applicationWillEnterForeground() {
//                FunctionFactory.getInstance().call("onApplicationWillEnterForeground", null);
                return super.applicationWillEnterForeground();
            }
        };
        app.run();
    }
}
