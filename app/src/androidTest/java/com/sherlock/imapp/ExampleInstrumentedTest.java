package com.sherlock.imapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.sherlock.imapp.utils.HttpUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.sherlock.imapp", appContext.getPackageName());
    }
    @Test
    public void httpGet() throws Exception {
        System.out.println(HttpUtil.get("http://localhost:18081/friend/getFriends?userId=4", null, null));
    }
}
