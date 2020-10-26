import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main implements ActionListener {
    static Random rnd = new Random();
    public static ArrayList<String[]> s = new ArrayList<String[]>();
    static int cnt = 0;
    public static ArrayList<String[]> birds = new ArrayList<>();
    public static ArrayList<String[]> masterlist = new ArrayList<>();
    public static String apppath = "C:/Users/"+System.getProperty("user.name")+"/AppData/Local/birdox";
    public static SwingUI ui;
    public static String user;
    public static void main(String[] args) {
        URL imageURL = Main.class.getClassLoader().getResource("favicon.png");
        ImageIcon img = new ImageIcon(imageURL);
        if (checkBirdoxComponents()) {
            update();
            user=readUsername();
            if (userExists(user)) {
                if (missingListFile()) {
                    createNewListFile();
                }
                if (encryptedRight()) {
                    mainm();
                } else {
                    popup("Please do not steal others' lists. Lists are irreversibly tied to you.");
                    System.exit(0);
                }
            } else if (!(userExists(user))) {
                popup("Username not found. Please reinstall Birdox.");
                System.exit(0);
            }
        } else {
            popup("Missing updater or username file. Please reinstall Birdox.");
        }
    }

    private static boolean encryptedRight() {
        File list = new File(apppath+"/list.bdff");
        try {
            Scanner lread = new Scanner(list);
            return decrypt(lread.nextLine(),user).equals("bdff");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void mainm() {
        loadLists();
        ui = new SwingUI();
        updatel();
    }

    private static void loadLists() {
        birds = new ArrayList<>();
        masterlist = new ArrayList<>();
        File list = new File(apppath+"/list.bdff");
        try {
            Scanner lread = new Scanner(list);
            lread.nextLine();
            while (lread.hasNextLine()) {
                String data = "";
                String data1 = "";
                String data2 = "";
                String data3 = "";
                try {
                    data = decrypt(lread.nextLine(),user);
                    data1 = decrypt(lread.nextLine(),user);
                    data2 = decrypt(lread.nextLine(),user);
                    data3 = decrypt(lread.nextLine(),user);
                    lread.nextLine();
                } catch (NoSuchElementException e){}
                if (!(data.equals("")&&data1.equals("")&&data2.equals("")&&data3.equals(""))) {
                    if (rare(data)){
                        birds.add(new String[]{data, data1, data2, data3, "rare"});
                    } else {
                        birds.add(new String[]{data, data1, data2, data3, "no"});
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File list1 = new File(apppath+"/masterlist.bdff");
        try {
            Scanner lread1 = new Scanner(list1);
            while (lread1.hasNextLine()) {
                String data = lread1.nextLine();
                String data1 = lread1.nextLine();
                String data2 =  lread1.nextLine();
                lread1.nextLine();
                masterlist.add(new String[]{data,data1,data2});
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static boolean userExists(String user) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://www.birdoxapp.tk/userexists?n="+user)).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String res = response.body();
            return res.equals("True");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        return false;
    }

    public static void addBird(String name, String date, String place){
        ArrayList<String> ids = new ArrayList<>();
        for (String[] s: birds){
            ids.add(s[3]);
        }
        int i = rnd.nextInt(1000000);
        while (ids.contains(String.valueOf(i))){
            i = rnd.nextInt(1000000);
        }
        birds.add(new String[]{name,date,place,String.valueOf(i)});
    }

    public static void popBird(String id){
        int i1=-1;
        int i=0;
        for (String[] s: birds){
            if (s[3].equals(id)){
                i1=i;
            }
            i++;
        }
        if (i1==-1){
            popup("This bird isn't on your list.");
        } else {
            birds.remove(i1);
        }
    }

    public static void updnet() throws IOException {
        String htmld = "";
        ArrayList<String> groups = new ArrayList<>();
        ArrayList<ArrayList<String[]>> birdcat = new ArrayList<>();
        for (String[] s : birds){
            String g = groupify(s[0]);
            if (!groups.contains(g)){
                groups.add(g);
                birdcat.add(new ArrayList<String[]>());
            }
            ArrayList<String[]> bc = birdcat.get(groups.indexOf(g));
            bc.add(s);
            birdcat.set(groups.indexOf(g),bc);
        }
        int i=0;
        for (ArrayList<String[]> birdss : birdcat) {
            htmld = htmld + "<h2>"+groups.get(i)+"</h2>";
            for (String[] s : birdss) {
                htmld = htmld + "<div style=\"border:2px solid black;\"><b><h3>" + s[0] + "</h3></b>";
                htmld = htmld + "<p>Seen at: " + s[1] + "</p>";
                htmld = htmld + "<p>Date: " + s[2] + "<p>";
                if (rare(s[0])) {
                    htmld = htmld + "<p><i>Rare or uncommon</i></p>";
                }
                htmld = htmld + "</div>";
            }
            i++;
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://www.birdoxapp.tk/suserdata?n="+enc(user));
        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("data", htmld));
        httppost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
        CloseableHttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            try (InputStream instream = entity.getContent()) {
            }
        }
    }

    private static String groupify(String s1) {
        String g = "Other";
        for (String[] s : masterlist){
            if (s[0].toLowerCase().equals(s1.toLowerCase())){
                g=s[1];
            }
        }
        return g;
    }

    private static boolean rare(String s1) {
        boolean rare = false;
        for (String[] s : masterlist){
            rare = (s[0].toLowerCase().equals(s1.toLowerCase()))&&(s[2].equals("rare"));
            if (rare) break;
        }
        return rare;
    }

    public static String enc(String in){
        try {
            return URLEncoder.encode(in, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void updateUserlists(){
        String lines = "bdff\n";
        for (String[] s : birds){
            lines=lines+s[0]+"\n";
            lines=lines+s[1]+"\n";
            lines=lines+s[2]+"\n";
            lines=lines+s[3]+"\n";
            lines=lines+":\n";
        }
        try {
            FileWriter fileWriter = new FileWriter(apppath+"/list.bdff");
            fileWriter.write(encrypt(lines, user));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readUsername() {
        File username = new File(apppath+"/name.txt");
        Scanner fscan = null;
        try {
            fscan = new Scanner(username);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            return fscan.nextLine();
        } catch (NoSuchElementException e){
            return "";
        }
    }

    static void advance(){
        if (birds.size() > cnt){
            cnt++;
        }
        try {
            updatel();
        } catch (IndexOutOfBoundsException e){
            cnt--;
        }
    }

    static void reverse(){
        cnt--;
        try {
            updatel();
        } catch (IndexOutOfBoundsException e){
            cnt++;
        }
    }

    static void updatel(){
        ArrayList<String> names = new ArrayList<>();
        for (String[] s1 : birds){
            names.add(s1[0]);
        }
        ArrayList<String[]> ss = new ArrayList<>();
        names.sort(String::compareToIgnoreCase);
        for (String st : names){
            ss.add(getData(st));
        }
        birds = ss;
        if (birds.size() < 4){
            s = new ArrayList<>();
            for (String[] st : birds){
                s.add(st);
            }
        } else {
            int i = cnt;
            int max = i + 4;
            if (birds.size() < max) {
                max = i + birds.size();
            }
            s = new ArrayList<>();
            while (i < max) {
                if (rare(birds.get(i)[0])) {
                    String[] g = birds.get(i);
                    g=new String[]{g[0],g[1],g[2],g[3],"rare"};
                    s.add(g);
                } else {
                    String[] g = birds.get(i);
                    g=new String[]{g[0],g[1],g[2],g[3],""};
                    s.add(g);
                }
                i++;
            }
        }
        printvals();
    }

    private static void createNewListFile() {
        File listFile = new File(apppath+"/list.bdff");
        try {
            listFile.createNewFile();
            FileWriter lf =  new FileWriter(apppath+"/list.bdff");
            lf.write(encrypt("bdff",user));
            lf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static boolean missingListFile() {
        File username = new File(apppath+"/name.txt");
        File list = new File(apppath+"/list.bdff");
        return !(list.exists())&&username.exists();
    }

    private static boolean checkBirdoxComponents() {
        File username = new File(apppath+"/name.txt");
        return username.exists();
    }

    private static void popup(String txt){
        JOptionPane.showMessageDialog(null, txt);
    }

    private static void update() {
        if (missingMasterlist()){
            try {
                updateFiles();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File masterlist= new File(apppath+"/masterlist.bdff");
            try {
                masterlist.createNewFile();
                updateFiles();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void updateFiles() throws IOException {
        FileWriter masterlist= new FileWriter(apppath+"/masterlist.bdff");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://www.birdoxapp.tk/masterlist")).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String res = response.body();
            masterlist.write(res.replace("<br>","\n"));
            masterlist.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    private static boolean missingMasterlist() {
        File masterlist= new File(apppath+"/masterlist.bdff");
        return masterlist.exists();
    }


    private static void printvals(){
        String st = "<html><ul>";
        for (String[] stt : s){
            if (stt[4].equals("rare")) {
                st = st + String.format("<li>Name: <strong>%s</strong><br>Seen at: <strong>%s</strong><br>Date: <strong>%s</strong><br>Group: <strong>%s</strong><br>ID Code: <strong>%s</strong><br>Rare or uncommon</li>", stt[0], stt[1], stt[2], groupify(stt[0]), stt[3]);
            } else {
                st = st + String.format("<li>Name: <strong>%s</strong><br>Seen at: <strong>%s</strong><br>Date: <strong>%s</strong><br>Group: <strong>%s</strong><br>ID Code: <strong>%s</strong></li>", stt[0], stt[1], stt[2], groupify(stt[0]), stt[3]);
            }
        }
        st=st+"</ul>";
        ui.list.setText(st);
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

    private static String decrypt(String in, String key){
        int k = 0;
        for (char c: key.toCharArray()){
            k+=(int)c;
        }
        String[] sst = in.split("\\|");
        String out = "";
        for (String s : sst){
            if (!s.equals("")) {
                out = out + (char) (Integer.parseInt(s) - k);
            }
        }
        return out;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()== ui.delete){
            popBird(ui.df.getText());
            updateUserlists();
            updatel();
        }
        if (e.getSource()== ui.submit){
            addBird(ui.i1.getText(),ui.i2.getText(),ui.i3.getText());
            updateUserlists();
            updatel();
        }
        if (e.getSource() == ui.backup){
            try {
                updnet();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        if (e.getSource() == ui.up){
            reverse();
        }
        if (e.getSource() == ui.down){
            advance();
        }
        if (e.getSource() == ui.submit1){
            if (listHas(ui.sc.getText())) {
                cnt = getListIndex(ui.sc.getText());
                if (birds.size()-cnt<4){
                    cnt=birds.size()-4;
                }
                updatel();
            }
        }
        if (e.getSource() == ui.bs){
            cnt=birds.size()-4;
            updatel();
        }
        if (e.getSource() == ui.bs1){
            cnt=0;
            updatel();
        }
        if (e.getSource() == ui.update){
            updateblc(ui.uf1.getText(),ui.uf2.getText(),ui.uf3.getText());
            updatel();
        }
    }

    private void updateblc(String id, String n, String d) {
        int i1=-1;
        int i=0;
        for (String[] s: birds){
            if (s[3].equals(id)){
                i1=i;
            }
            i++;
        }
        if (i1==-1){
            popup("This bird isn't on your list.");
        } else {
            String[] bg = birds.get(i1);
            birds.set(i1,new String[]{bg[0],n,d,bg[3]});
        }
    }

    private int getListIndex(String text) {
        int i = 0;
        int i1=0;
        for (String[] s : birds){
            if(s[0].toLowerCase().equals(text.toLowerCase())){
                i=i1;
                break;
            }
            i1++;
        }
        return i;
    }

    private boolean listHas(String text) {
        boolean has = false;
        for (String[] s : birds){
            has = s[0].toLowerCase().equals(text.toLowerCase());
            if (has) break;
        }
        return has;
    }

    private static String[] getData(String text) {
        String[] res = null;
        for (String[] s : birds){
            if (s[0].toLowerCase().equals(text.toLowerCase())){
                res = s;
            }
        }
        return res;
    }
}
