package as.space;

import android.content.Intent;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


/**
 * Created by Sergio on 21/04/2017.
 */

@RunWith(AndroidJUnit4.class)
public class IntegrationActivityTest {

    public ActivityTestRule<Inicio> InicioRule = new ActivityTestRule<>(Inicio.class, false);
    public ActivityTestRule<GameOver> GameOverRule = new ActivityTestRule<>(GameOver.class, false);
    public ActivityTestRule<MainActivity> MainActivityRule = new ActivityTestRule<>(MainActivity.class, true, false);
    Intent startIntent = new Intent();

    @Test
    public void test1(){
        InicioRule.launchActivity(startIntent);
        onView(withId(R.id.gifStart)).perform(click());
    }

    @Test
    public void test3(){
        GameOverRule.launchActivity(startIntent);
        onView(withId(R.id.txtJugar)).perform(click());
    }

    @Test
    public void test2(){
        MainActivityRule.launchActivity(startIntent);
        onView(withId(16908290)).perform(clickXY(1176,700));
        onView(withId(16908290)).perform(clickXY(1176,700));
        onView(withId(16908290)).perform(longClick());
        onView(withId(16908290)).perform(clickXY(0,700));
        onView(withId(16908290)).perform(clickXY(0,700));
    }

    public static ViewAction clickXY(final int x, final int y){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER);
    }
}
