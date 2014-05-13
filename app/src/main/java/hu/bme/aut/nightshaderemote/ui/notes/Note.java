package hu.bme.aut.nightshaderemote.ui.notes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
* @author Ákos Pap
*/
public class Note {
    private String title;
    private String content;
    private File file;

    public Note(File file) {
        this.file = file;
        String s = file.getName();
        this.title = s.substring(0, s.length() - 4);
    }

    public void delete() {
        file.delete();
    }


    public String getContent() {
        if (content == null) {
            content = readFile(file);
        }

        return content;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
        writeContents(file, content);
    }

    /**
     * Beolvassa a kapot File tartalmát
     * @param file File típusú objkektum, amiből olvasni szereténk
     * @return Visszatér egy Stringgel, ami a File tartalmát hordozza
     */
    protected String readFile(File file){

        if (! (file.exists() && file.canRead())) {
            return "";
        }

        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return text.toString();
    }

    /**
     * Felülírja a kívánt fájlt
     * @param file A File amit felül szeretnénk írni
     * @param text A File Új tartalma
     */
    public void writeContents(File file, String text){
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.append(text);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
