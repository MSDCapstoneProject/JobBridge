package com.capstone.jobapplication.jobbridge;

import com.capstone.jobapplication.jobbridge.util.StringUtil;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String[] objs = {"16 cedarwoods","Kitchener","ON","CA","N2C 2L4"};
        System.out.println(String.format("%s, %s, %s, %s, %s",objs));
    }
}