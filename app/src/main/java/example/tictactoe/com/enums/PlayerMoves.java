package example.tictactoe.com.enums;

/**
 * Created by Vika on 5/11/2015.
 */
public enum PlayerMoves {
    Tic(0), Toe(1);
    private int id = 0;

    private PlayerMoves(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }

    public static PlayerMoves fromInt(int i) {
        for (PlayerMoves playerMove : PlayerMoves.values()) {
            if (playerMove.getValue() == i) {
                return playerMove;
            }
        }
        return null;
    }
}
