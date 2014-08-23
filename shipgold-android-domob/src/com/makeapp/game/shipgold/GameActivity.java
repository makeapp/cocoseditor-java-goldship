package com.makeapp.game.shipgold;
/**
 *  Copyright(c) Shanghai YiJun Network Technologies Inc. All right reserved.
 */

import org.ccj.Director;
import org.ccj.android.CocosJavaExtrasActivity;

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
        Director.getInstance().end();
        finish();
    }
}
