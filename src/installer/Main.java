import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main implements ActionListener {
    public static SwingUI ui;
    public static String mname;
    public static void main(String[] args) {
        ui=new SwingUI();
    }
    static String[][] files = new String[][]{{"favicon.ico","1"},{"birdox.exe","1"},{"name.txt","1"},{"masterlist.bdff","1"},{"list.bdff","1"},{"Birdox.lnk","0"}};

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ui.submitname){
            String name = ui.name.getText();
            mname = name;
            if (hasBadChars(name)){
                popup("Name can only contain a-z, A-Z, and 0-9.");
            } else {
                if(checkName(name)){
                    if (name.toCharArray().length < 40){
                        if (name.toCharArray().length > 5){
                            showLicense();
                            ui.name.setVisible(false);
                            ui.submitname.setVisible(false);
                        } else {
                            popup("Name too short.");
                        }
                    } else {
                        popup("Name too long.");
                    }
                } else {
                    popup("Name taken.");
                }
            }
        }
        if (e.getSource() == ui.licensea){
            ui.header.setBounds(ui.cx, 50, 300, 150);
            ui.licensea.setVisible(false);
            ui.header.setText("<html><h1>Thanks for installing!</h1>");
            copyFiles();
            makeUser();
        }
    }

    private void makeUser() {
        System.out.println(mname);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://www.birdoxapp.tk/nuserc?n="+mname)).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String res = response.body();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    private static String encrypt(String in, String key){
        int k = 0;
        for (char c: key.toCharArray()){
            k+=(int)c;
        }
        String out = "";
        String[] ins = in.split("\\n");
        int newLine=((int)'\n')+k;
        for (String line: ins){
            String enc = "";
            for (char c: line.toCharArray()){
                if ((int)c+k == newLine){
                    enc = enc + "\n";
                } else {
                    enc = enc + String.valueOf((int) c + k) + "|";
                }
            }
            out=out+enc+"\n";
        }
        return out;
    }

    private void copyFiles() {
        try {
            makeDir("C:/Users/"+System.getProperty("user.name")+"/AppData/Local/birdox");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String[] f : files){
            if (f[1].equals("1")){
                copyFile(f[0],String.format("C:/Users/%s/AppData/Local/birdox/%s",System.getProperty("user.name"),f[0]));
            }
            if (f[1].equals("0")){
                copyFile(f[0],String.format("C:/Users/%s/Desktop/%s",System.getProperty("user.name"),f[0]));
            }
        }
        FileWriter ff = null;
        try {
            ff = new FileWriter(String.format("C:/Users/%s/AppData/Local/birdox/name.txt",System.getProperty("user.name")));
            ff.write(mname);
            ff.close();
            ff = new FileWriter(String.format("C:/Users/%s/AppData/Local/birdox/list.bdff",System.getProperty("user.name")));
            ff.write(encrypt("bdff",mname));
            ff.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyFile(String s, String s1) {
        try {
            Path p2 = Paths.get(s1);
            Files.copy(Main.class.getResourceAsStream(s), p2);
        } catch (FileAlreadyExistsException e){

        } catch (IOException e) {

        }
    }

    private void makeDir(String dirname) throws IOException {
        Path p = Paths.get(dirname);
        if (!Files.exists(p)){
            Files.createDirectories(p);
        }
    }

    private void showLicense() {
        ui.header.setBounds(ui.cx, 50, 300, 600);
        ui.licensea.setVisible(true);
        ui.header.setText("<html><h1>Agreee to the license agreement:</h1><p>MIT License<br>" +
                "<br>" +
                "Copyright (c) 2020 Jacob-Python<br>" +
                "Permission is hereby granted, free of charge, to any person obtaining a copy<br>" +
                "of this software and associated documentation files (the \"Software\"), to deal<br>" +
                "in the Software without restriction, including without limitation the rights<br>" +
                "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell<br>" +
                "copies of the Software, and to permit persons to whom the Software is<br>" +
                "furnished to do so, subject to the following conditions:<br>" +
                "The above copyright notice and this permission notice shall be included in all<br>" +
                "copies or substantial portions of the Software.<br>" +
                "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR<br>" +
                "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,<br>" +
                "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE<br>" +
                "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER<br>" +
                "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,<br>" +
                "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE<br>" +
                "SOFTWARE.");
    }

    private boolean hasBadChars(String name) {
        boolean has = true;
        for (char c : name.toCharArray()){
            has = !Character.isLetter(c)||Character.isDigit(c);
            if (!has) break;
        }
        return has;
    }

    private boolean checkName(String name) {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://www.birdoxapp.tk/userexists?n="+name)).build();
                HttpResponse<String> response = null;
                try {
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    String res = response.body();
                    return !res.equals("True");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        return false;
    }

    private static void popup(String txt){
        JOptionPane.showMessageDialog(null, txt);
    }
}
