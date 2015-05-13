package example.tictactoe.com.enums;

/**
 * Created by Vika on 5/11/2015.
 */
public enum State {
    UNKNOWN(-3),
    WIN(-2),
    EMPTY(0),
    PLAYER1(1),
    PLAYER2(2),
    PLAYER_COMPUTER(3);
    private int id = 0;

    private State(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }

}
