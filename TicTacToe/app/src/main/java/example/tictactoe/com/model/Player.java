package example.tictactoe.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import example.tictactoe.com.enums.PlayerMoves;
import example.tictactoe.com.enums.State;

/**
 * Created by Vika on 5/11/2015.
 */
public class Player implements Serializable {

    private String name;
    private PlayerMoves move;
    private State state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerMoves getMove() {
        return move;
    }

    public void setMove(PlayerMoves move) {
        this.move = move;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
