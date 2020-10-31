import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class SwingUI {
    public int cx;
    JButton licensea;
    JButton submitname;
    JTextField name;
    JFrame frame;
    JPanel panel;
    JLabel header;

    public SwingUI(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        panel = new JPanel();
        panel.setLayout(null);
        cx = screenSize.width/4;
        cx-=cx/4;
        frame = new JFrame("Birdox Install Tool");
        frame.setSize(screenSize.width/2,screenSize.height);
        frame.setVisible(true);
        URL imageURL = Main.class.getClassLoader().getResource("favicona.png");
        ImageIcon img = new ImageIcon(imageURL);
        frame.setIconImage(img.getImage());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panel);
        header = new JLabel("<html><h1>Welcome to Birdox!</h1><h3>Easy listing is just a few clicks away.</h3><p>Enter username:");
        header.setBounds(cx, 50, 300, 150);
        header.setVisible(true);
        panel.add(header);
        name = new JTextField();
        name.setBounds(cx, 180, 300, 30);
        name.setVisible(true);
        panel.add(name);
        submitname = new JButton("Submit");
        submitname.setBounds(cx, 250, 300, 30);
        submitname.setVisible(true);
        submitname.addActionListener(new Main());
        panel.add(submitname);
        licensea = new JButton("I agree");
        licensea.setBounds(cx, 640, 300, 30);
        licensea.setVisible(false);
        licensea.addActionListener(new Main());
        panel.add(licensea);
    }
}
