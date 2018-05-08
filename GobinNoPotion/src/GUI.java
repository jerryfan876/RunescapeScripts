import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    private JFrame frame = new JFrame();
    private JLabel label1 = new JLabel();

    private JComboBox<String> comboBoxFood = new JComboBox();
    private String[] stringFoodArray = new String[]{"Herring", "Salmon", "Trout"};

    private JButton saveButton = new JButton();


    public GUI() {
        frame.setTitle("GUI - Jerry");
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setAlwaysOnTop(false);
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        this.comboBoxFood.setModel(new DefaultComboBoxModel(stringFoodArray));

        saveButton.setText("Submit");
        saveButton.setActionCommand("save");

        label1.setText("pick a food: ");


        frame.add(saveButton);
        frame.add(label1);
        frame.add(comboBoxFood);
        saveButton.addActionListener(new MyListener());


    }


    public class MyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String name = e.getActionCommand();
            if (name.equals("save")) {

                GobinNoPotion.STRINGFOOD = GUI.this.comboBoxFood.getSelectedItem().toString();

                GobinNoPotion.setRunTrue();
                frame.dispose();
            }

        }
    }


}
