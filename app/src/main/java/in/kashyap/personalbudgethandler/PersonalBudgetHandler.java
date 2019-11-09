package in.kashyap.personalbudgethandler;

import android.app.Application;
import com.orhanobut.hawk.Hawk;

public class PersonalBudgetHandler extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(getApplicationContext()).build();
    }
}
