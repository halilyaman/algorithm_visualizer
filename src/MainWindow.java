
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

enum NodeTypes {
   StartNode,
   EndNode,
   AvailableNode,
   WallNode,
   PathNode,
   SearchedNode,
}

public class MainWindow {
   final private int ROW_SIZE = 20;
   final private int COL_SIZE = 50;

   private JFrame frame = new JFrame();
   private JPanel panel = new JPanel();
   private JPanel bottomPanel = new JPanel();
   private JPanel gridViewPanel = new JPanel();
   private Node[][] nodes = new Node[ROW_SIZE][COL_SIZE];

   //index0 start node, index1 end node
   private Node[] previousNodes = new Node[2];

   // keeping for drawing wall
   private Node tempNode;

   private int startNodeRow = -1;
   private int startNodeCol = -1;

   void buildScreen() {
      panel.setLayout(new BorderLayout());
      gridViewPanel.setLayout(new GridLayout(ROW_SIZE, COL_SIZE));
      gridViewPanel.setBorder(new EmptyBorder(80,50,80,50));
      buildGridView();

      JButton clearWholePatternButton = new JButton("Clear All");
      JButton clearAlgorithmSchemaButton = new JButton("Clear Algorithm");
      JButton runAlgorithmButton = new JButton("Run Algorithm");

      clearWholePatternButton.addActionListener(new ClearAllButtonListener());
      clearAlgorithmSchemaButton.addActionListener(new ClearWithoutWallsButtonListener());
      runAlgorithmButton.addActionListener(new RunAlgorithmButtonListener());

      bottomPanel.setLayout(new FlowLayout());
      bottomPanel.add(clearWholePatternButton);
      bottomPanel.add(clearAlgorithmSchemaButton);
      bottomPanel.add(runAlgorithmButton);
      bottomPanel.setBackground(Color.BLUE);

      panel.add(gridViewPanel, BorderLayout.CENTER);
      panel.add(bottomPanel, BorderLayout.SOUTH);

      frame.setLayout(null);
      frame.setContentPane(panel);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLocation(0,0);
      frame.setSize(new Dimension(1500, 800));
      frame.setVisible(true);
      frame.setResizable(true);
   }

   private void buildGridView() {
      for(int row = 0; row < ROW_SIZE; row++) {
         for(int col = 0; col < COL_SIZE; col++) {
            nodes[row][col] = new Node(row, col, NodeTypes.AvailableNode);
            nodes[row][col].getNodePanel().addMouseListener(new NodeMouseListener(nodes[row][col]));
            nodes[row][col].getNodePanel().addMouseMotionListener(new NodeMouseMotionListener());
            gridViewPanel.add(nodes[row][col].getNode());
         }
      }
   }

   private void updateNode(int val, Node selectedNode) {
      if(val == 1) {
         if(!(previousNodes[0] == null)) {
            if(selectedNode.getNodeType() == NodeTypes.EndNode) {
               previousNodes[1] = null;
            }
            previousNodes[0].setNodeType(NodeTypes.AvailableNode);
         }
         previousNodes[0] = selectedNode;
         selectedNode.setNodeType(NodeTypes.StartNode);
         startNodeRow = selectedNode.getRow();
         startNodeCol = selectedNode.getCol();
      } else if(val == 0) {
         if(!(previousNodes[1] == null)) {
            if(selectedNode.getNodeType() == NodeTypes.StartNode) {
               previousNodes[0] = null;
            }
            previousNodes[1].setNodeType(NodeTypes.AvailableNode);
         }
         previousNodes[1] = selectedNode;
         selectedNode.setNodeType(NodeTypes.EndNode);
      }
   }

   private void clearAllPattern() {
      for(int row = 0; row < ROW_SIZE; row++) {
         for(int col = 0; col < COL_SIZE; col++) {
            nodes[row][col].setNodeType(NodeTypes.AvailableNode);
         }
      }
      startNodeRow = -1;
      startNodeCol = -1;
   }

   private void clearWithoutWalls() {
      for(int row = 0; row < ROW_SIZE; row++) {
         for(int col = 0; col < COL_SIZE; col++) {
            if(!(nodes[row][col].getNodeType() == NodeTypes.WallNode)) {
               nodes[row][col].setNodeType(NodeTypes.AvailableNode);
            }
         }
      }
      startNodeRow = -1;
      startNodeCol = -1;
   }

   private void setDistanceToCurrentNode(int nodeRow, int nodeCol) {
      int distance;
      for(int row = 0; row < ROW_SIZE; row++) {
         for(int col = 0; col < COL_SIZE; col++) {
            distance = Math.abs(nodeRow - row) + Math.abs(nodeCol - col);
            nodes[row][col].setDistance(distance);
         }
      }
   }

   private void printDistance() {
      for(int row = 0; row < ROW_SIZE; row++) {
         System.out.println();
         for(int col = 0; col < COL_SIZE; col++) {
            System.out.print(nodes[row][col].getDistance() + " ");
         }
      }
   }

   private void runDijkstra() {
      int distance = 1;
      if(startNodeCol > -1 && startNodeRow > -1) {
         Queue<Node> queue = new LinkedList<>();
         queue.offer(nodes[startNodeRow][startNodeCol]);
         while(!queue.isEmpty()) {
            Node currentNode = queue.remove();
            setDistanceToCurrentNode(currentNode.getRow(), currentNode.getCol());
            ArrayList<Node> children = new ArrayList<>();
            for(int row = 0; row < ROW_SIZE; row++) {
               for(int col = 0; col < COL_SIZE; col++) {
                  if(nodes[row][col].getDistance() == 1) {
                     children.add(nodes[row][col]);
                  }
               }
            }

            for(Node child: children) {
               if(child.getNodeType() == NodeTypes.AvailableNode) {
                  try {
                     Thread.sleep(10);
                  } catch(InterruptedException ex) {
                     ex.printStackTrace();
                  }
                  child.setNodeType(NodeTypes.SearchedNode);
                  child.setDistanceToStartNode(distance++);
                  queue.offer(child);
               } else if(child.getNodeType() == NodeTypes.EndNode) {
                  drawShortestPath(child.getRow(), child.getCol());
                  return;
               }
            }
         }
      }
   }

   private void drawShortestPath(int nodeRow, int nodeCol) {
      Queue<Node> queue = new LinkedList<>();
      queue.offer(nodes[nodeRow][nodeCol]);
      while(!queue.isEmpty()) {
         Node currentNode = queue.remove();
         setDistanceToCurrentNode(currentNode.getRow(), currentNode.getCol());
         ArrayList<Node> children = new ArrayList<>();
         for(int row = 0; row < ROW_SIZE; row++) {
            for(int col = 0; col < COL_SIZE; col++) {
               if(nodes[row][col].getDistance() == 1) {
                  children.add(nodes[row][col]);
               }
            }
         }
         int minDistance = Integer.MAX_VALUE;
         Node closestNode = null;
         for(Node child : children) {
            if(child.getNodeType() == NodeTypes.SearchedNode || child.getNodeType() == NodeTypes.StartNode) {
               if(child.getNodeType() == NodeTypes.StartNode) {
                  return;
               }
               if(child.getDistanceToStartNode() < minDistance) {
                  minDistance = child.getDistanceToStartNode();
                  closestNode = child;
               }
            }
         }
         if(closestNode != null) {
            try{
               Thread.sleep(20);
            } catch(InterruptedException ex) {
               ex.printStackTrace();
            }
            closestNode.setNodeType(NodeTypes.PathNode);
            queue.offer(closestNode);
         }
      }
   }

   class NodeMouseListener implements MouseListener {

      private Node selectedNode;
      private String[] optionTextList = {"end", "start"};

      NodeMouseListener(Node selectedNode) {
         this.selectedNode = selectedNode;
      }

      @Override
      public void mouseClicked(MouseEvent e) {
         int val;
         val = JOptionPane.showOptionDialog(
            selectedNode.getNodePanel(),
            "Choose the Type of the Node",
            "",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            optionTextList,
            optionTextList[1]
         );
         updateNode(val, selectedNode);
      }

      @Override
      public void mousePressed(MouseEvent e) {

      }

      @Override
      public void mouseReleased(MouseEvent e) {

      }

      @Override
      public void mouseEntered(MouseEvent e) {
         tempNode = selectedNode;
      }

      @Override
      public void mouseExited(MouseEvent e) {

      }
   }

   class NodeMouseMotionListener implements MouseMotionListener {
      @Override
      public void mouseDragged(MouseEvent e) {
         tempNode.setNodeType(NodeTypes.WallNode);
      }

      @Override
      public void mouseMoved(MouseEvent e) {

      }
   }

   class ClearAllButtonListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         clearAllPattern();
      }
   }

   class ClearWithoutWallsButtonListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         clearWithoutWalls();
      }
   }

   class RunAlgorithmButtonListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         new Thread(() -> {
            runDijkstra();
         }).start();
      }
   }
}
