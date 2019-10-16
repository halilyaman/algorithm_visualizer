import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainWindow {
   private JFrame frame = new JFrame();
   private JPanel panel = new JPanel();
   private JPanel gridViewPanel = new JPanel();

   void buildScreen() {
      panel.setLayout(new BorderLayout());
      gridViewPanel.setLayout(new GridLayout(20, 50));
      gridViewPanel.setBorder(new EmptyBorder(80,50,80,50));
      buildGridView();
      panel.add(gridViewPanel);
      frame.add(panel);
      frame.setSize(1700, 850);
      frame.setVisible(true);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }

   private void buildGridView() {
      for(int row = 0; row < 20; row++) {
         for(int col = 0; col < 50; col++) {
            if(row == 10 && col == 10) {
               gridViewPanel.add(new Node(row, col, Color.green).buildNode());
            } else if(row == 15 && col == 40) {
               gridViewPanel.add(new Node(row, col, Color.red).buildNode());
            } else {
               gridViewPanel.add(new Node(row, col, Color.white).buildNode());
            }
         }
      }
   }
}
