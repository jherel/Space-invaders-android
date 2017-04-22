package as.space.mock;

/**
 * Created by danic on 22/04/2017.
 */

public class GameOverMock {
    public boolean isUserInGameOverActivity;
    public boolean isStartButtonClicked, isExitButtonClicked;


    public GameOverMock(){
        this.isUserInGameOverActivity = true;
        this.isExitButtonClicked = false;
        this.isStartButtonClicked = false;
    }

    public String clickExit(){
        this.isExitButtonClicked = true;
        return"Exit button succesfully clicked";
    }
    public String clickStart(){
        this.isStartButtonClicked = true;
        return"start button succesfully clicked";
    }
}
