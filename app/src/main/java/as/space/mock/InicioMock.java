package as.space.mock;

/**
 * Created by danic on 19/04/2017.
 */

public class InicioMock {
    public boolean isUserInApp;
    public boolean isUserInInicioActivity;
    public boolean isOptionButtonPressed;
    public boolean isPlayButtonPressed;
    public String[] optionMenu = {"Fondo de juego", "Skin aliada", "Skin enemiga"};

    public InicioMock() {
        this.isUserInApp = true;
        this.isUserInInicioActivity = true;
        this.isOptionButtonPressed = false;
        this.isPlayButtonPressed = false;

    }

    public String clickOption() {
        this.isOptionButtonPressed = true;
        return "Option button successfully clicked";
    }
    public String clickPlay() {
        this.isPlayButtonPressed = true;
        return "Play button successfully clicked";
    }

    @Override
    public String toString() {
        return this.optionMenu[0] + " | " + this.optionMenu[1] + " | " + this.optionMenu[2];
    }





}
