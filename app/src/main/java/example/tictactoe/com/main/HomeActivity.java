package example.tictactoe.com.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Vika on 5/9/2015.
 */
public class HomeActivity extends Activity {

    private Button onePlayerButton;
    private Button twoPlayerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        onePlayerButton = (Button) findViewById(R.id.onePlayrtButton);
        twoPlayerButton = (Button) findViewById(R.id.twoPlayersButton);

        onePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAdditionalDataActivity(AdditionalDataActivity.PLAYER_ONE);
            }
        });

        twoPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAdditionalDataActivity(AdditionalDataActivity.PLAYER_TWO);
            }
        });
    }

    private void startAdditionalDataActivity(int numberOfPlayer) {
        Intent intent = new Intent(this, AdditionalDataActivity.class);
        intent.putExtra("number", numberOfPlayer);
        startActivity(intent);
    }
}
