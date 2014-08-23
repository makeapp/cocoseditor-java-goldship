package com.makeapp.game.shipgold;
/**
 *  Copyright(c) Shanghai YiJun Network Technologies Inc. All right reserved.
 */

import android.os.Bundle;
import android.view.KeyEvent;
import com.makeapp.android.extras.FunctionBrowser;
import com.makeapp.android.extras.FunctionVibrator;
import com.makeapp.android.extras.baidu.BaiduFunctionAd;
import com.makeapp.android.extras.baidu.BaiduFunctionAnalysis;
import com.makeapp.android.extras.tencent.TencentFunctionAd;
import com.makeapp.android.extras.tencent.TencentFunctionAnalysis;
import com.makeapp.android.extras.tencent.TencentFunctionWeiXin;
import org.ccj.Director;
import org.ccj.android.CocosJavaExtrasActivity;
import org.fun.FunctionFactory;

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
