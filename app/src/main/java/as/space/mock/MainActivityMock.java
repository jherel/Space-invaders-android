package as.space.mock;

/**
 * Created by danic on 19/04/2017.
 */

public class MainActivityMock {

    public boolean isUserInMainActivity;
    public boolean isRightButtonClicked, isLeftButtonClicked;
    public int height,weight;
    public int[]space={100,50};

    public MainActivityMock(){
        this.height=100;
        this.weight =100;
        this.isUserInMainActivity =true;
        this.isRightButtonClicked =false;
        this.isLeftButtonClicked = false;
    }
    @Override
    public String toString(){
        return "Heigh:"+this.height+"weight"+this.weight+"position"+this.toString();
    }
    public boolean pressRightButton(){
        this.isRightButtonClicked = true;
        return this.isRightButtonClicked;
    }
    public boolean pressLeftButton(){
        this.isLeftButtonClicked =true;
        return this.isLeftButtonClicked;
    }
    public String moveSpaceToRight(){
        this.space[1]=55;
        return"Space has been moved to"+this.toStringg();
    }
    public String moveSpaceToLeft(){
        this.space[1]=45;
        return"Space has been moved to"+this.toStringg();
    }


    private String toStringg(){
        return this.space[0]+","+this.space[1];
    }

}
