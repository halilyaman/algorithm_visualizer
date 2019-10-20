import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Node {
   private JPanel nodePanel = new JPanel();
   private JPanel inlineNodePanel = new JPanel();

   private NodeTypes nodeType;
   private int row;
   private int col;
   private Color color;
   private int distance;
   private int distanceToStartNode = Integer.MAX_VALUE;

   public int getDistanceToStartNode() {
      return distanceToStartNode;
   }

   public void setDistanceToStartNode(int distanceToStartNode) {
      this.distanceToStartNode = distanceToStartNode;
   }

   public int getDistance() {
      return distance;
   }

   public void setDistance(int distance) {
      this.distance = distance;
   }

   public void setNodePanel(JPanel nodePanel) {
      this.nodePanel = nodePanel;
   }

   public JPanel getInlineNodePanel() {
      return inlineNodePanel;
   }

   public void setInlineNodePanel(JPanel inlineNodePanel) {
      this.inlineNodePanel = inlineNodePanel;
   }

   public NodeTypes getNodeType() {
      return nodeType;
   }

   public void setNodeType(NodeTypes nodeType) {
      this.nodeType = nodeType;
      if(nodeType == NodeTypes.AvailableNode) {
         this.setColor(Color.white);
      } else if(nodeType == NodeTypes.StartNode) {
         this.distanceToStartNode = 0;
         this.setColor(Color.green);
      } else if(nodeType == NodeTypes.EndNode) {
         this.setColor(Color.red);
      } else if(nodeType == NodeTypes.WallNode) {
         this.setColor(Color.black);
      } else if(nodeType == NodeTypes.PathNode) {
         this.setColor(Color.BLUE);
      } else if(nodeType == NodeTypes.SearchedNode) {
         this.setColor(Color.orange);
      }
   }

   public int getRow() {
      return row;
   }

   public void setRow(int row) {
      this.row = row;
   }

   public int getCol() {
      return col;
   }

   public void setCol(int col) {
      this.col = col;
   }

   public Color getColor() {
      return color;
   }

   public void setColor(Color color) {
      this.color = color;
      inlineNodePanel.setBackground(this.color);
   }

   public JPanel getNodePanel() {
      return nodePanel;
   }

   Node(int row, int col, NodeTypes nodeType) {
      this.row = row;
      this.col = col;
      this.nodeType = nodeType;
      if(nodeType == NodeTypes.AvailableNode) {
         this.color = Color.white;
      } else if (nodeType == NodeTypes.StartNode) {
         this.color = Color.green;
      } else if(nodeType == NodeTypes.EndNode) {
         this.color = Color.red;
      } else if(nodeType == NodeTypes.WallNode) {
         this.color = Color.black;
      }
   }

   JPanel getNode() {
      nodePanel.setBorder(new EmptyBorder(0,0,3,3));
      nodePanel.setLayout(new BorderLayout());
      inlineNodePanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
      inlineNodePanel.setBackground(this.color);
      nodePanel.add(inlineNodePanel, BorderLayout.CENTER);
      return nodePanel;
   }
}
