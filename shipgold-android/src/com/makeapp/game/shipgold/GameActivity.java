package com.makeapp.game.shipgold;
/**
 *  Copyright(c) Shanghai YiJun Network Technologies Inc. All right reserved.
 */

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import com.makeapp.game.shipgold.Main;
import org.ccj.android.CocosJavaExtrasActivity;
import org.cocos2dx.lib.Cocos2dxActivity;

/**
 * @author <a href="mailto:yuanyou@makeapp.co">yuanyou</a>
 * @version $Date:2014/3/31 10:16 $
 *          $Id$
 */
public class GameActivity
        extends CocosJavaExtrasActivity {


    @Override
    public void main(String[] strings) {
        Main.main(strings);
    }

    public void onBackPressed() {
        Main.end();
        finish();
    }
}
