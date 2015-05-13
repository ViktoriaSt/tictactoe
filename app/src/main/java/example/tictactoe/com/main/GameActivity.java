package example.tictactoe.com.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import example.tictactoe.com.enums.PlayerMoves;
import example.tictactoe.com.enums.State;
import example.tictactoe.com.model.Player;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Vika on 5/11/2015.
 */
public class GameActivity extends Activity {

    private static final int[][] THREEINAROW = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}};


    private TextView playerOneName;
    private TextView playerTwoName;

    private TextView cell_00;
    private TextView cell_01;
    private TextView cell_02;
    private TextView cell_10;
    private TextView cell_11;
    private TextView cell_12;
    private TextView cell_20;
    private TextView cell_21;
    private TextView cell_22;

    private ArrayList<Player> listPlayer = new ArrayList<Player>();

    private State[] fieldStates = new State[9];
    private TextView[] cells = new TextView[9];

    private Player currentPlayer;
    private boolean withComputer;

    private Handler mHandler = new Handler(new ComputerHandlerCallback());
    private Random mRnd = new Random();

    private static final int MSG_COMPUTER_TURN = 1;
    private static final long COMPUTER_DELAY_MS = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        playerOneName = (TextView) findViewById(R.id.playerOneName);
        playerTwoName = (TextView) findViewById(R.id.playerTwoName);

        cell_00 = (TextView) findViewById(R.id.cell_00);
        cell_01 = (TextView) findViewById(R.id.cell_01);
        cell_02 = (TextView) findViewById(R.id.cell_02);
        cell_10 = (TextView) findViewById(R.id.cell_10);
        cell_11 = (TextView) findViewById(R.id.cell_11);
        cell_12 = (TextView) findViewById(R.id.cell_12);
        cell_20 = (TextView) findViewById(R.id.cell_20);
        cell_21 = (TextView) findViewById(R.id.cell_21);
        cell_22 = (TextView) findViewById(R.id.cell_22);

        for (int i = 0; i < fieldStates.length; i++) {
            fieldStates[i] = State.EMPTY;
        }

        cells[0] = cell_00;
        cells[1] = cell_01;
        cells[2] = cell_02;
        cells[3] = cell_10;
        cells[4] = cell_11;
        cells[5] = cell_12;
        cells[6] = cell_20;
        cells[7] = cell_21;
        cells[8] = cell_22;


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            withComputer = bundle.getBoolean("withComputer");
            if (bundle.getSerializable("list") != null) {
                listPlayer.clear();
                listPlayer.addAll((ArrayList<Player>) bundle.getSerializable("list"));
                if (listPlayer.size() > 1) {
                    playerOneName.setText(listPlayer.get(0).getName() + " - " + ((listPlayer.get(0).getMove() == PlayerMoves.Tic) ? "X" : "O"));
                    playerTwoName.setText(listPlayer.get(1).getName() + " - " + ((listPlayer.get(1).getMove() == PlayerMoves.Tic) ? "X" : "O"));
                    currentPlayer = (listPlayer.get(0).getMove() == PlayerMoves.Tic) ? listPlayer.get(0) : listPlayer.get(1);
                }
            }
        }


        for (int i = 0; i < cells.length; i++) {
            final int temp = i;
            final TextView textView = cells[i];
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentPlayer.getState() == State.WIN) {
                        GameActivity.this.finish();
                    }
                    if (fieldStates[temp] == State.EMPTY) {
                        if (currentPlayer.getMove() == PlayerMoves.Tic) {
                            textView.setText("X");
                        } else {
                            textView.setText("O");
                        }
                        fieldStates[temp] = currentPlayer.getState();
                        textView.setEnabled(false);
                        textView.setTextColor(getResources().getColor(android.R.color.background_dark));
                        finishTurn();
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (withComputer && currentPlayer.getMove() == PlayerMoves.Tic && currentPlayer.getState() == State.PLAYER2) {
            mHandler.sendEmptyMessageDelayed(MSG_COMPUTER_TURN, COMPUTER_DELAY_MS);
        }
    }

    private Player getOtherPlayer(Player player) {
        return player == listPlayer.get(0) ? listPlayer.get(1) : listPlayer.get(0);
    }

    private void finishTurn() {
        if (!checkGameFinished(currentPlayer)) {

            currentPlayer = getOtherPlayer(currentPlayer);

            selectTurn(currentPlayer.getState());
            if (currentPlayer.getState() == State.PLAYER2 && withComputer) {
                mHandler.sendEmptyMessageDelayed(MSG_COMPUTER_TURN, COMPUTER_DELAY_MS);
            }
        }
    }

    private State selectTurn(State player) {
        if (player == State.PLAYER1) {
            for (TextView view : cells) {
                view.setEnabled(true);
                view.setTextColor(getResources().getColor(android.R.color.background_dark));
            }
        }
        if (withComputer && player == State.PLAYER2) {
            for (TextView view : cells) {
                view.setEnabled(false);
                view.setTextColor(getResources().getColor(android.R.color.background_dark));
            }
        }

        return player;
    }


    public boolean checkGameFinished(Player player) {
        boolean full = true;
        int col = -1;
        int row = -1;
        int diagonal = -1;
        // check rows
        for (int j = 0, k = 0; j < 3; j++, k += 3) {
            if (fieldStates[k] != State.EMPTY && fieldStates[k] == fieldStates[k + 1] && fieldStates[k] == fieldStates[k + 2]) {
                row = j;
            }
            if (full && (fieldStates[k] == State.EMPTY ||
                    fieldStates[k + 1] == State.EMPTY ||
                    fieldStates[k + 2] == State.EMPTY)) {
                full = false;
            }
        }
        // check columns
        for (int i = 0; i < 3; i++) {
            if (fieldStates[i] != State.EMPTY && fieldStates[i] == fieldStates[i + 3] && fieldStates[i] == fieldStates[i + 6]) {
                col = i;
            }
        }
        // check diagonals
        if (fieldStates[0] != State.EMPTY && fieldStates[0] == fieldStates[1 + 3] && fieldStates[0] == fieldStates[2 + 6]) {
            diagonal = 0;
        } else if (fieldStates[2] != State.EMPTY && fieldStates[2] == fieldStates[1 + 3] && fieldStates[2] == fieldStates[0 + 6]) {
            diagonal = 1;
        }
        if (col != -1 || row != -1 || diagonal != -1) {
            setFinished(player, col, row, diagonal);
            return true;
        }
        // if we get here, there's no winner but the board is full.
        if (full) {
            Player player1 = new Player();
            player.setState(State.EMPTY);
            setFinished(player, -1, -1, -1);
            return true;
        }
        return false;
    }

    private void setFinished(Player player, int col, int row, int diagonal) {
        for (TextView view : cells) {
            view.setEnabled(false);
            view.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
        setWinState(player);
    }

    private void setWinState(Player player) {
        if (player.getState() == State.EMPTY) {
            showDialog(getString(R.string.nobody_wins_message));
        } else {
            showDialog(player.getName() + " " + getString(R.string.user_winner_message));
        }
    }

    private class ComputerHandlerCallback implements Handler.Callback {
        public boolean handleMessage(Message msg) {
            if (msg.what == MSG_COMPUTER_TURN) {

                //check best move
                if (checkBestMove(State.PLAYER2)) {
                    finishTurn();
                    return true;
                } else if (checkBestMove(State.PLAYER1)) {
                    finishTurn();
                    return true;
                } else {
                    // Pick a non-used cell at random. That's about all the AI you need for this game.
                    int used = 0;
                    while (used != 0x1F) {
                        int index = mRnd.nextInt(9);
                        if (((used >> index) & 1) == 0) {
                            used |= 1 << index;
                            if (fieldStates[index] == State.EMPTY) {
                                TextView view = cells[index];

                                if (currentPlayer.getMove() == PlayerMoves.Tic) {
                                    view.setText("X");
                                } else {
                                    view.setText("O");
                                }
                                fieldStates[index] = currentPlayer.getState();

                                break;
                            }
                        }
                    }
                }


                finishTurn();
                return true;
            }
            return false;
        }
    }

    public boolean checkBestMove(State state) {

        for (int j = 0; j < THREEINAROW.length; j++) {
            int quantity = 0;

            for (int k = 0; k < THREEINAROW[j].length; k++) {

                if (fieldStates[THREEINAROW[j][k]] == state) {
                    quantity++;
                }

                if (quantity == THREEINAROW[j].length - 1) {
                    for (int i = 0; i < THREEINAROW[j].length; i++) {
                        if (fieldStates[THREEINAROW[j][i]] == State.EMPTY) {
                            fieldStates[THREEINAROW[j][i]] = State.PLAYER2;
                            TextView view = cells[THREEINAROW[j][i]];
                            if (currentPlayer.getMove() == PlayerMoves.Tic) {
                                view.setText("X");
                            } else {
                                view.setText("O");
                            }
                            return true;
                        }
                    }

                }


            }
        }
        return false;
    }

    public void showDialog(final String winner) {
        final Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setContentView(R.layout.dialog_signin);

        TextView text = (TextView) builder.findViewById(R.id.dialogWinnerName);
        Button startGame = (Button) builder.findViewById(R.id.dialogStart);
        startGame.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });
        text.setText(winner);

        builder.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = builder.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }


}


