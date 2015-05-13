package example.tictactoe.com.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import example.tictactoe.com.enums.PlayerMoves;
import example.tictactoe.com.enums.State;
import example.tictactoe.com.model.Player;

/**
 * Created by Vika on 5/10/2015.
 */
public class AdditionalDataActivity extends Activity {

    public static final int PLAYER_ONE = 1;
    public static final int PLAYER_TWO = 2;

    private TextView playerMoveLbl;
    private CheckedTextView ticChecked;
    private CheckedTextView toeChecked;

    private EditText playerOneEditName;
    private EditText playerTwoEditName;

    private int playerQuantity;

    private Button startGame;

    private ArrayList<Player> playerList = new ArrayList<Player>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.additional_data_layout);

        playerOneEditName = (EditText) findViewById(R.id.firstPlayerEditName);
        playerTwoEditName = (EditText) findViewById(R.id.secondPlayerEditName);
        playerMoveLbl = (TextView) findViewById(R.id.playerMoveLbl);
        ticChecked = (CheckedTextView) findViewById(R.id.ticChecked);
        toeChecked = (CheckedTextView) findViewById(R.id.toeChecked);
        startGame = (Button) findViewById(R.id.startGameButton);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.get("number") != null) {
            playerQuantity = bundle.getInt("number");
        }
        switch (playerQuantity) {
            case PLAYER_ONE:
                playerTwoEditName.setText(getString(R.string.computer_lbl));
                playerTwoEditName.setFocusable(false);
                playerTwoEditName.setEnabled(false);
                playerOneEditName.setHint(getString(R.string.first_player_name_hint_with_comp));
                break;
            case PLAYER_TWO:
                playerTwoEditName.setFocusable(true);
                playerTwoEditName.setEnabled(true);
                playerMoveLbl.setVisibility(View.GONE);
                ticChecked.setVisibility(View.GONE);
                toeChecked.setVisibility(View.GONE);
                break;
        }


        ticChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ticChecked.isChecked()) {
                    ticChecked.setChecked(true);
                    toeChecked.setChecked(false);
                } else {
                    toeChecked.setChecked(true);
                }
            }
        });
        toeChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toeChecked.isChecked()) {
                    ticChecked.setChecked(true);
                } else {
                    toeChecked.setChecked(true);
                    ticChecked.setChecked(false);
                }
            }
        });

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerQuantity == PLAYER_ONE) {
                    if (playerOneEditName.getText().toString() != null && playerOneEditName.getText().toString().length() > 0) {
                        Player player = new Player();
                        player.setName(playerOneEditName.getText().toString());

                        Player player2 = new Player();
                        player2.setName(getString(R.string.computer_lbl));
                        player.setState(State.PLAYER1);
                        player2.setState(State.PLAYER2);
                        if (ticChecked.isChecked()) {
                            player.setMove(PlayerMoves.Tic);
                            player2.setMove(PlayerMoves.Toe);

                        } else {
                            player.setMove(PlayerMoves.Toe);
                            player2.setMove(PlayerMoves.Tic);
                        }
                        playerList.clear();
                        playerList.add(player);
                        playerList.add(player2);

                        startGameActivity(true);
                    } else {
                        Toast.makeText(AdditionalDataActivity.this, getString(R.string.write_name_message), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (playerOneEditName.getText().toString() != null && playerOneEditName.getText().toString().length() > 0 && playerTwoEditName.getText().toString() != null && playerTwoEditName.getText().toString().length() > 0) {
                        if (playerOneEditName.getText().toString().equals(playerTwoEditName.getText().toString())) {
                            Toast.makeText(AdditionalDataActivity.this, getString(R.string.name_message_error), Toast.LENGTH_SHORT).show();
                        } else {
                            Player player = new Player();
                            player.setName(playerOneEditName.getText().toString());
                            player.setMove(PlayerMoves.Tic);
                            player.setState(State.PLAYER1);

                            Player player2 = new Player();
                            player2.setName(playerTwoEditName.getText().toString());
                            player2.setMove(PlayerMoves.Toe);
                            player2.setState(State.PLAYER2);

                            playerList.clear();
                            playerList.add(player);
                            playerList.add(player2);
                            startGameActivity(false);
                        }
                    } else {
                        Toast.makeText(AdditionalDataActivity.this, getString(R.string.write_name_message), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void startGameActivity(boolean withComputer) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("list", playerList);
        intent.putExtra("withComputer", withComputer);
        startActivity(intent);
    }
}
