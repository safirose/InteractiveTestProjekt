package org.beginningandroid.interactivetest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SaldoActivityUITest {

        double totalSaldo;
    @Before
    public void setUp(){
        Context context = ApplicationProvider.getApplicationContext();
        MyDatabaseHelper db = new MyDatabaseHelper(context);

        db.insertKvittering(new Kvittering(1,4,3,5,"12:24","25-05-2025"));
        totalSaldo = db.beregnTotalSaldo();
    }
@Test
    public void testSaldoIsDisplayed(){
    ActivityScenario.launch(SaldoActivity.class);
    String beregnetSaldo = "Saldo: " + totalSaldo + " kr.";
    onView(withId(R.id.saldoText)).check(matches(withText(beregnetSaldo)));
}
}