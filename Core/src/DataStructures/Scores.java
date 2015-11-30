package DataStructures;

import java.io.Serializable;

public class Scores implements Serializable {
    int blueScore;
    int redScore;

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

    public void setBlueScore(int blueScore) {
        this.blueScore = blueScore;
    }

    public void setRedScore(int redScore) {
        this.redScore = redScore;
    }
}
