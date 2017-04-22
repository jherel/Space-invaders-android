package as.space.mock;

/**
 * Created by danic on 19/04/2017.
 */

public class MainActivityMock {

    public boolean isUserInMainActivity;
    public boolean isOptionButtonPressed;
    public boolean isRightButtonClicked, isLeftButtonClicked, isMiddleButtonClicked;
    public int heigh, weight;

    public int[] space = {100, 50};
    public int[] ammo = {-1};

    public MainActivityMock() {
        this.heigh = 100;
        this.weight = 100;
        this.isUserInMainActivity = true;
        this.isRightButtonClicked = false;
        this.isLeftButtonClicked = false;
        this.isOptionButtonPressed = false;

    }

    @Override
    public String toString() {
        return "Heigh: " + this.heigh + "\nWeight: " + this.weight + "\nShip position: " + this.toStringg();
    }
    public String clickOption() {
        this.isOptionButtonPressed = true;
        return "Option button successfully clicked";
    }

    public boolean pressRightButton() {
        this.isRightButtonClicked = true;
        return this.isRightButtonClicked;
    }

    public String moveSpaceToLeft() {
        this.space[1] = 45;
        return "Ship has been moved to " + this.toStringg() + " coordinates.";
    }

    public boolean pressLeftButton() {
        this.isLeftButtonClicked = true;
        return this.isLeftButtonClicked;
    }

    public String moveSpaceToRight() {
        this.space[1] = 55;
        return "Space has been moved to " + this.toStringg() + " coordinates.";
    }

    public boolean pressMiddleButton() {
        this.isMiddleButtonClicked = true;
        return this.isMiddleButtonClicked;
    }

    public String releaseAmmo() {
        this.ammo[0] = 100;
        return "Space is shooting ammo!.";
    }

    private String toStringg() {
        return this.space[0] + "," + this.space[1];
    }

}
