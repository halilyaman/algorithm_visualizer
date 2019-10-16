import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Node {
   private JPanel nodePanel = new JPanel();
   private JPanel inlineNodePanel = new JPanel();
   private int row;
   private int col;
   private Color color;

   Node(int row, int col, Color color) {
      this.row = row;
      this.col = col;
      this.color = color;
   }

   JPanel buildNode() {
      nodePanel.setBorder(new EmptyBorder(0,0,3,3));
      nodePanel.setLayout(new BorderLayout());
      inlineNodePanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
      inlineNodePanel.setBackground(this.color);
      nodePanel.add(inlineNodePanel, BorderLayout.CENTER);
      return nodePanel;
   }
}
