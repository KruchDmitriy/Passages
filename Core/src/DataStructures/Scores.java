package DataStructures;

import java.io.Serializable;

public class Scores implements Serializable {
    private int blueScore;
    private int redScore;

    public Scores() {
        this.blueScore = 0;
        this.redScore = 0;
    }

    public Scores(int blueScore, int redScore) {
        this.blueScore = blueScore;
        this.redScore = redScore;
    }

    public int getBlueScore() {
        return blueScore;
    }

    public int getRedScore() {
        return redScore;
    }

    public void incBlueScore() {
        blueScore++;
    }

    public void incRedScore() {
        redScore++;
    }
}
