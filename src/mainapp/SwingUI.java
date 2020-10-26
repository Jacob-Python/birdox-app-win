import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class SwingUI {
    JTextField uf1;
    JTextField uf2;
    JTextField uf3;
    JButton update;
    JButton bs1;
    JButton bs;
    JLabel scl;
    JTextField sc;
    JButton submit1;
    JFrame frame;
    JPanel panel;
    JLabel header;
    JButton backup;
    JTextField i1;
    JTextField i2;
    JTextField i3;
    JButton submit;
    JTextField df;
    JButton delete;
    JLabel comp1l;
    JSeparator sep1;
    JLabel comp2l;
    JLabel list;
    JButton up;
    JButton down;

    public SwingUI(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        panel = new JPanel();
        panel.setLayout(null);
        frame = new JFrame("Birdox");
        frame.setSize(screenSize.width,screenSize.height);
        frame.setVisible(true);
        URL imageURL = Main.class.getClassLoader().getResource("favicon.png");
        ImageIcon img = new ImageIcon(imageURL);
        frame.setIconImage(img.getImage());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panel);
        header = new JLabel("<html><h1>Welcome to Birdox!</h1><h3>Made by birders, for birders.</h3>");
        header.setBounds(310, 10, 300, 150);
        header.setVisible(true);
        panel.add(header);
        comp1l = new JLabel("<html><h4>List</h4>");
        comp1l.setBounds(610, 100, 300, 150);
        comp1l.setVisible(true);
        panel.add(comp1l);
        sep1 = new JSeparator();
        sep1.setOrientation(SwingConstants.VERTICAL);
        panel.add(sep1);
        comp2l = new JLabel("<html><h4>Tools</h4><p>Delete bird (enter ID)&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;Update selected bird <br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;&nbsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;(enter ID, new date and place)</p><br><br><br><br><br><br><p><p>Add bird (enter name, date, and place)");
        comp2l.setBounds(60, 150, 450, 260);
        comp2l.setVisible(true);
        panel.add(comp2l);
        df = new JTextField("");
        df.setBounds(60, 250, 100, 30);
        panel.add(df);
        delete = new JButton("Delete bird");
        delete.setBounds(60, 300, 160, 30);
        delete.addActionListener(new Main());
        panel.add(delete);
        uf1 = new JTextField("");
        uf1.setBounds(300, 250, 100, 30);
        panel.add(uf1);
        uf2 = new JTextField("");
        uf2.setBounds(300, 300, 100, 30);
        panel.add(uf2);
        uf3 = new JTextField("");
        uf3.setBounds(300, 350, 100, 30);
        panel.add(uf3);
        update = new JButton("Update entry");
        update.setBounds(300, 400, 160, 30);
        update.addActionListener(new Main());
        panel.add(update);
        i1 = new JTextField("");
        i1.setBounds(60, 400, 100, 30);
        panel.add(i1);
        i2 = new JTextField("");
        i2.setBounds(60, 450, 100, 30);
        panel.add(i2);
        i3 = new JTextField("");
        i3.setBounds(60, 500, 100, 30);
        panel.add(i3);
        submit = new JButton("Add bird");
        submit.setBounds(60, 550, 160, 30);
        submit.addActionListener(new Main());
        panel.add(submit);
        backup = new JButton("Back list up to internet");
        backup.setBounds(60, 600, 190, 30);
        backup.addActionListener(new Main());
        panel.add(backup);
        list = new JLabel("");
        list.setBorder(BorderFactory.createBevelBorder(1));
        list.setBounds(550, 200, 500, 410);
        panel.add(list);
        up = new JButton("↑");
        up.setBounds(500, 200, 40, 60);
        up.addActionListener(new Main());
        panel.add(up);
        down = new JButton("↓");
        down.setBounds(500, 550, 40, 60);
        down.addActionListener(new Main());
        panel.add(down);
        submit1 = new JButton("Scroll to bird");
        submit1.setBounds(890, 630, 160, 30);
        submit1.addActionListener(new Main());
        panel.add(submit1);
        sc = new JTextField("");
        sc.setBounds(700, 630, 160, 30);
        panel.add(sc);
        scl = new JLabel("Scroll to a bird");
        scl.setBounds(600, 630, 160, 30);
        panel.add(scl);
        bs = new JButton("Scroll to bottom");
        bs.setBounds(890, 670, 160, 30);
        bs.addActionListener(new Main());
        panel.add(bs);
        bs1 = new JButton("Scroll to top");
        bs1.setBounds(700, 670, 160, 30);
        bs1.addActionListener(new Main());
        panel.add(bs1);
    }
}