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
        Date dt = StringUtil.formateDateTime("2017-06-03T00:12:400Z");
        DateTime datetime = new DateTime(dt);
        DateTime now = new DateTime(new Date());
        Hours hours = Hours.hoursBetween(datetime, now);
        System.out.println(hours);
    }
}