import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;

public class MainWindow extends JFrame{
    private DataManager dm;
    private JPanel mainPanel;
    private JPanel buttonPanel;

    private JCanvasPanel canvasPanel;

    private JButton brighterButton;
    private JButton darkerButton;
    private JButton binarButton;
    private JButton downfilButton;
    private JButton upfilButton;
    private JButton gaussButton;
    private JButton erosButton;
    private JButton dylatButton;
    private JButton morfOpenButton;
    private JButton morfCloseButton;

    private JTextField binField;
    private JTextField brightField;

    private JButton reverseButton;


    public MainWindow (String title) {
        super(title);
        dm = new DataManager();

        //==============================================================================================
        try {
            BufferedImage bg = ImageIO.read(new File("obrazek.bmp"));
            dm.img = bg;
        } catch (IOException e) {
            e.printStackTrace();
        }

        //===============================================================================================
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        brighterButton = new JButton("Rozjasnij");
        darkerButton = new JButton("Sciemnij");
        binarButton = new JButton("Binaryzuj");
        downfilButton=new JButton("Filtr dolnoprzepustowy");
        upfilButton=new JButton("Filtr gornoprzepustowy");
        gaussButton=new JButton("Filtr Gaussa");
        erosButton=new JButton("Erozja");
        dylatButton=new JButton("Dylatacja");
        morfOpenButton=new JButton("Otwarcie morfologiczne");
        morfCloseButton=new JButton("Domkniecie morfologiczne");

        binField = new JTextField("100");
        binarButton.setBorder(new TitledBorder("Wartosc binaryzacji"));

        brightField = new JTextField("25");
        brightField.setBorder(new TitledBorder("Zmiana Jasnosci"));

        reverseButton = new JButton("Cofnij");

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(14, 2));

        buttonPanel.add(binField);
        buttonPanel.add(binarButton);

        buttonPanel.add(brightField);
        buttonPanel.add(brighterButton);
        buttonPanel.add(darkerButton);
        buttonPanel.add(downfilButton);
        buttonPanel.add(upfilButton);
        buttonPanel.add(gaussButton);
        buttonPanel.add(erosButton);
        buttonPanel.add(dylatButton);
        buttonPanel.add(morfOpenButton);
        buttonPanel.add(morfCloseButton);
        buttonPanel.add(reverseButton);

        buttonPanel.setPreferredSize(new Dimension(300,0));

        canvasPanel = new JCanvasPanel(dm);

        mainPanel.add(BorderLayout.CENTER, canvasPanel);
        mainPanel.add(BorderLayout.EAST, buttonPanel);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);

        this.setSize(new Dimension(1000, 600));
        this.setLocationRelativeTo(null);


        brighterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utility util = new Utility(dm);

                util.brighter(dm,Integer.parseInt(brightField.getText()));
                canvasPanel.repaint();
            }
        });

        darkerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utility util = new Utility(dm);

                util.darker(dm,Integer.parseInt(brightField.getText()));
                canvasPanel.repaint();
            }
        });

        binarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utility util = new Utility(dm);
                int x=Integer.parseInt(binField.getText());

                util.binarization(dm,Integer.parseInt(binField.getText()));
                canvasPanel.repaint();
            }
        });

        reverseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BufferedImage bg = ImageIO.read(new File("obrazek.bmp"));
                    dm.img = bg;
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
                canvasPanel.repaint();
            }
        });

        downfilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utility util = new Utility(dm);
                util.downFilter(dm);
                canvasPanel.repaint();
            }
        });

        upfilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utility util = new Utility(dm);
                util.upFilter(dm);
                canvasPanel.repaint();
            }
        });

        gaussButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utility util = new Utility(dm);
                util.gauss(dm);
                canvasPanel.repaint();
            }
        });

        erosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utility util = new Utility(dm);
                util.binarization(dm,70);
                util.erozja(dm);
                canvasPanel.repaint();
            }
        });

        dylatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utility util = new Utility(dm);
                util.binarization(dm,70);
                util.dylatacja(dm);
                canvasPanel.repaint();
            }
        });

        morfOpenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utility util = new Utility(dm);
                util.morfOpen(dm);
                canvasPanel.repaint();
            }
        });

        morfCloseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utility util = new Utility(dm);
                util.morfClose(dm);
                canvasPanel.repaint();
            }
        });

    }

    public static void main(String[] args){
        MainWindow mw = new MainWindow("Modelowanie dyskretne");
        mw.setVisible(true);
        mw.canvasPanel.repaint();

        Utility util = new Utility(mw.dm);

    }
}
