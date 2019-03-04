package ir.hphamid.cps_ca3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button GravityButton = (Button)findViewById(R.id.Gravity);
        GravityButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i  = new Intent(MainActivity.this, GravityActivity.class);
                Bundle b = new Bundle();
                b.putInt("command", 0);
                i.putExtras(b);
                startActivity(i);

            }
        });

    }
}

