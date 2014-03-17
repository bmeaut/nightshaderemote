package hu.bme.aut.nightshaderemote;

/**
 * Created by Marci on 2014.03.17..
 */
public class CustomButton {
    private String title;
    private String scriptText;
    public CustomButton(String aTitle, String aScriptText){
        title = aTitle;
        scriptText = aScriptText;
    }

    public String getTitle(){
        return title;
    }

    public String getScriptText(){
        return scriptText;
    }
}
