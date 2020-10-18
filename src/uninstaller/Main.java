import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.*;
import java.util.Scanner;

public class Main implements ActionListener {
    private static JFrame frame;
    private static JPanel panel;
    private static JLabel header;
    private static JTextField code;
    private static JLabel inp_1;
    private static JButton submit;
    private static JLabel status;
    private static JButton close;
    private static String uid;
    private static boolean has = false;
    private static String[] files = new String[]{"name.txt","masterlist.bdff","updatebirdox.exe","birdox.exe"};

    public static void main(String[] args) throws FileNotFoundException {
        panel = new JPanel();
        panel.setLayout(null);
        frame = new JFrame("Birdox Uninstaller");
        URL imageURL = Main.class.getClassLoader().getResource("birdoxremove.png");
        ImageIcon img = new ImageIcon(imageURL);
        frame.setIconImage(img.getImage());
        frame.setSize(1040, 700);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        try {
            File myObj = new File("C:/Users/" + System.getProperty("user.name") + "/AppData/Local/birdox/name.txt");
            Scanner myReader = new Scanner(myObj);
            uid = myReader.nextLine();
            has = true;
        } catch (FileNotFoundException e) {
            has = false;
        }
        mainf();
    }

    private static void mainf() {
        // Page 1
        header = new JLabel("<html><h1>Welcome to the Birdox Uninstaller</h1><p>We'll save your list.");
        header.setBounds(5, 55, 900, 79);
        panel.add(header);
        submit = new JButton("Next");
        submit.setBounds(5, 205, 90, 40);
        submit.addActionListener(new Main());
        panel.add(submit);
        // Uninstall page
        status = new JLabel("");
        status.setBounds(5, 350, 900, 79);
        panel.add(status);
        close = new JButton("Close Uninstaller");
        close.setBounds(5, 655, 150, 40);
        close.addActionListener(new Main());
        close.setVisible(false);
        panel.add(close);
        if (has) {
            submit.setVisible(true);
        } else {
            header.setText("<html><h1>Birdox not found on this computer");
            close.setVisible(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == close) {
            System.exit(1);
        }
        if (e.getSource() == submit) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://www.birdoxapp.tk/popu?n="+uid)).build();
            HttpResponse<String> response = null;
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String res = response.body();
                System.out.println(res);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            header.setText("<html><h1>Uninstalling...");
            submit.setVisible(false);
            uninst();
        }
    }

    private void uninst() {
        status.setText("Deleting files...");
        deleteFiles();
        status.setText("We're sorry to see you go.");
        header.setText("<html><h1>Done");
        close.setVisible(true);
    }

    private void deleteFiles() {
        Runtime rt = Runtime.getRuntime();
        try {
            for (String f : files){
                deleteFile("C:/Users/" + System.getProperty("user.name") + "/AppData/Local/birdox/"+f);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Files.delete(Paths.get("C:/Users/" + System.getProperty("user.name") + "/Desktop/Birdox.lnk"));
        } catch (NoSuchFileException e){
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteFile(String s) throws IOException, FileNotFoundException {
        File f=new File(s);
        f.delete();
    }

}