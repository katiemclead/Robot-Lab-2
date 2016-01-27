import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.net.*;
import javax.swing.*;

public class Draw extends JComponent implements MouseListener
{
  private static Draw instance;
  private static JFrame frame;
  private static BufferedImage image;
  private static Color color;
  private static boolean filled;
  private static int fontSize;
  private static Point mouse;
  
  public static void window(int width, int height)
  {
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = image.getGraphics();
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, width, height);
    color = Color.BLACK;
    filled = false;
    fontSize = 12;
    mouse = null;
    frame = new JFrame();
    frame.setTitle("Draw");
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    instance = new Draw();
    instance.setPreferredSize(new Dimension(width, height));
    instance.addMouseListener(instance);
    frame.getContentPane().add(instance);
    frame.add(instance);
    frame.pack();
    frame.setVisible(true);
  }
  
  public static void setTitle(String title)
  {
    frame.setTitle(title);
  }
  
  public static void setColor(int red, int green, int blue)
  {
    color = new Color(red, green, blue);
  }
  
  public static void setFill(boolean isFilled)
  {
    filled = isFilled;
  }
  
  public static void setFont(int size)
  {
    fontSize = size;
  }
  
  public static void line(int x1, int y1, int x2, int y2)
  {
    if (instance == null)
      throw new RuntimeException("Must call window method first");
    Graphics g = image.getGraphics();
    g.setColor(color);
    g.drawLine(x1, y1, x2, y2);
    instance.repaint();
  }
  
  public static void rectangle(int x, int y, int width, int height)
  {
    if (instance == null)
      throw new RuntimeException("Must call window method first");
    Graphics g = image.getGraphics();
    g.setColor(color);
    if (filled)
      g.fillRect(x, y, width, height);
    else
      g.drawRect(x, y, width, height);
    instance.repaint();
  }
  
  public static void oval(int x, int y, int width, int height)
  {
    if (instance == null)
      throw new RuntimeException("Must call window method first");
    Graphics g = image.getGraphics();
    g.setColor(color);
    if (filled)
      g.fillOval(x, y, width, height);
    else
      g.drawOval(x, y, width, height);
    instance.repaint();
  }
  
  public static void polygon(int... coords)
  {
    if (instance == null)
      throw new RuntimeException("Must call window method first");
    if (coords.length < 3)
      throw new RuntimeException("Must be at least x1, y1, x2, y2, x3, y3");
    if (coords.length % 2 == 1)
      throw new RuntimeException("Must be an even number of arguments");
    Graphics g = image.getGraphics();
    g.setColor(color);
    int n = coords.length / 2;
    int[] x = new int[n];
    int[] y = new int[n];
    for (int i = 0; i < n; i++)
    {
      x[i] = coords[2 * i];
      y[i] = coords[2 * i + 1];
    }
    if (filled)
      g.fillPolygon(x, y, n);
    else
      g.drawPolygon(x, y, n);
    instance.repaint();
  }
  
  public static void text(int x, int y, String text)
  {
    if (instance == null)
      throw new RuntimeException("Must call window method first");
    Graphics g = image.getGraphics();
    g.setColor(color);
    Font font = new Font(null, Font.PLAIN, fontSize);
    g.setFont(font);
    FontMetrics metrics = g.getFontMetrics();
    int actualX = x - metrics.stringWidth(text) / 2;
    int actualY = y + metrics.getAscent() / 2;
    g.drawString(text, actualX, actualY);
    instance.repaint();
  }
  
  public static void image(int x, int y, int width, int height, String file)
  {
    if (instance == null)
      throw new RuntimeException("Must call window method first");
    Graphics g = image.getGraphics();
    URL url = Draw.class.getResource(file);
    if (url == null)
      throw new RuntimeException("Cannot find file:  " + file);
    ImageIcon icon = new ImageIcon(file);
    g.drawImage(icon.getImage(), x, y, width, height, null);
    instance.repaint();
  }

  public static void pause(double seconds)
  {
    try
    {
      Thread.sleep((int)(seconds * 1000));
    }
    catch(InterruptedException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public static void pauseUntilMouse()
  {
    if (instance == null)
      throw new RuntimeException("Must call window method first");
    mouse = null;
    while (mouse == null)
      pause(0.01);
  }
  
  public static int getMouseX()
  {
    if (mouse == null)
      throw new RuntimeException("Must click mouse in window first");
    return (int)mouse.getX();
  }
  
  public static int getMouseY()
  {
    if (mouse == null)
      throw new RuntimeException("Must click mouse in window first");
    return (int)mouse.getY();
  }
  
  public void paintComponent(Graphics g)
  {
    g.drawImage(image, 0, 0, null);
  }

  public void mouseExited(MouseEvent e)
  {
  }
  
  public void mouseEntered(MouseEvent e)
  {
  }
  
  public void mouseReleased(MouseEvent e)
  {
  }
  
  public void mousePressed(MouseEvent e)
  {
    mouse = e.getPoint();
  }
  
  public void mouseClicked(MouseEvent e)
  {
  }
}