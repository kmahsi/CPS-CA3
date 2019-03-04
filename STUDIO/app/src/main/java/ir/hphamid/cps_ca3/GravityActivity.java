package ir.hphamid.cps_ca3;


import android.content.DialogInterface;
import android.icu.util.BuddhistCalendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by kamran on 3/4/2019.
 */

public class GravityActivity extends AppCompatActivity {
    private boolean started = false;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity);
        Bundle b = getIntent().getExtras();
        int previous_command = -1;
        if (b != null)
            previous_command = b.getInt("command");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ImageView s = (ImageView) findViewById(R.id.ball);
        if (started != true){
            s.setY(500);
            s.setX(700);
            started = true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        ImageView s = (ImageView) findViewById(R.id.ball);
//        s.setY(300);
//        s.setX(300);

    }



}
