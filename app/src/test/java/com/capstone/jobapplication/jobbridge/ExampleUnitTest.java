package com.capstone.jobapplication.jobbridge;

import com.capstone.jobapplication.jobbridge.util.StringUtil;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String wage = StringUtil.formatWage("13.5");
        System.out.println(wage);
    }
}