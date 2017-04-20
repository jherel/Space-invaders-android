package as.space.mock;

/**
 * Created by danic on 19/04/2017.
 */

public class InicioMock {
    public boolean isUserInapp;
    public boolean isUserInInicio;
    public boolean isStartButtonPressed;

    public InicioMock(){
        this.isUserInapp= true;
        this.isUserInInicio = true;
        this.isStartButtonPressed = false;
    }
    public String clickPlay(){
        this.isStartButtonPressed=true;
        return "Play button successfully clicked";
    }





}
