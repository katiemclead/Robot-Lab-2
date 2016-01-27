import java.awt.*;
import java.util.*;

public class Robot
{
  private static int robotX;
  private static int robotY;
  private static char direction;
  private static double delay = 0.25;
  private static boolean loaded = false;
  
  private static final String ROBOT_NORTH_IMAGE = "robotnorth.gif";
  private static final String ROBOT_SOUTH_IMAGE = "robotsouth.gif";
  private static final String ROBOT_EAST_IMAGE = "roboteast.gif";
  private static final String ROBOT_WEST_IMAGE = "robotwest.gif";
  
  private static final Color LIGHT_COLOR = new Color(230, 170, 120);
  private static final Color DARK_COLOR = new Color(150, 110, 80);
  
  public static void load(String mapFileName)
  {
    ArrayList<String> lines = new ArrayList<String>();
    while (Util.hasMoreLines(mapFileName))
      lines.add(Util.readLine(mapFileName));
    Util.close(mapFileName);
    
    int numRows = lines.size();
    int numCols = lines.get(0).length();
    Grid.window(numCols, numRows);
    Grid.setLineColor(0, 0, 0);
    Draw.setTitle(mapFileName);
    
    for (int row = 0; row < numRows; row++)
    {
      for (int col = 0; col < numCols; col++)
      {
        if (lines.get(row).length() != numCols)
          throw new RuntimeException("Inconsistent line length in map file \"" + mapFileName + "\"");
        
        char ch = lines.get(row).charAt(col);
        if ("NSEWnsew".indexOf(ch) != -1)
        {
          if ("NSEW".indexOf(ch) != -1)
            Grid.setColor(col, row, LIGHT_COLOR.getRed(),
                       LIGHT_COLOR.getGreen(), LIGHT_COLOR.getBlue());
          else
            Grid.setColor(col, row, DARK_COLOR.getRed(),
                       DARK_COLOR.getGreen(), DARK_COLOR.getBlue());
          
          if (ch == 'N' || ch == 'n')
          {
            Grid.setImage(col, row, ROBOT_NORTH_IMAGE);
            direction = 'N';
          }
          else if (ch == 'S' || ch == 's')
          {
            Grid.setImage(col, row, ROBOT_SOUTH_IMAGE);
            direction = 'S';
          }
          else if (ch == 'E' || ch == 'e')
          {
            Grid.setImage(col, row, ROBOT_EAST_IMAGE);
            direction = 'E';
          }
          else
          {
            Grid.setImage(col, row, ROBOT_WEST_IMAGE);
            direction = 'W';
          }
          robotX = col;
          robotY = row;
        }
        else if (ch == 'X')
          Grid.setImage(col, row, "wall.gif");
        else if (ch == '.')
          Grid.setColor(col, row, LIGHT_COLOR.getRed(),
                     LIGHT_COLOR.getGreen(), LIGHT_COLOR.getBlue());
        else if (ch == ':')
          Grid.setColor(col, row, DARK_COLOR.getRed(),
                        DARK_COLOR.getGreen(), DARK_COLOR.getBlue());
        else
          throw new RuntimeException("Invalid character '" + ch + "' in map file \"" + mapFileName + "\"");
      }
    }
    loaded = true;
  }
  
  public static void move()
  {
    if (!loaded)
      throw new RuntimeException("Map not loaded yet");
    int x = robotX;
    int y = robotY;
    if (direction == 'N')
      y = y - 1;
    else if (direction == 'S')
      y = y + 1;
    else if (direction == 'E')
      x = x + 1;
    else
      x = x - 1;
    
    if (!Grid.isValid(x, y))
      throw new RuntimeException("Attempt to move robot from " + Grid.loc(robotX, robotY) + " to invalid location " + Grid.loc(x, y));
    if (!Grid.getImage(x, y).equals(""))
      throw new RuntimeException("Attempt to move robot from " + Grid.loc(robotX, robotY) + " to occupied location " + Grid.loc(x, y));
    
    String image = Grid.getImage(robotX, robotY);
    Grid.setImage(robotX, robotY, "");
    robotX = x;
    robotY = y;
    Grid.setImage(robotX, robotY, image);
    Draw.pause(delay);
  }
  
  public static void turnLeft()
  {
    if (!loaded)
      throw new RuntimeException("Map not loaded yet");
    if (direction == 'N')
    {
      direction = 'W';
      Grid.setImage(robotX, robotY, ROBOT_WEST_IMAGE);
    }
    else if (direction == 'S')
    {
      direction = 'E';
      Grid.setImage(robotX, robotY, ROBOT_EAST_IMAGE);
    }
    else if (direction == 'E')
    {
      direction = 'N';
      Grid.setImage(robotX, robotY, ROBOT_NORTH_IMAGE);
    }
    else
    {
      direction = 'S';
      Grid.setImage(robotX, robotY, ROBOT_SOUTH_IMAGE);
    }
    Draw.pause(delay);
  }
  
  public static void makeLight()
  {
    if (!loaded)
      throw new RuntimeException("Map not loaded yet");
    if (!onDark())
      throw new RuntimeException("Location " + Grid.loc(robotX, robotY) + " is already light");
    Grid.setColor(robotX, robotY, LIGHT_COLOR.getRed(),
               LIGHT_COLOR.getGreen(), LIGHT_COLOR.getBlue());
    Draw.pause(delay);
  }
  
  public static void makeDark()
  {
    if (!loaded)
      throw new RuntimeException("Map not loaded yet");
    if (onDark())
      throw new RuntimeException("Location " + Grid.loc(robotX, robotY) + " is already dark");
    Grid.setColor(robotX, robotY, DARK_COLOR.getRed(),
               DARK_COLOR.getGreen(), DARK_COLOR.getBlue());
    Draw.pause(delay);
  }
  
  public static boolean onDark()
  {
    if (!loaded)
      throw new RuntimeException("Map not loaded yet");
    return Grid.getRed(robotX, robotY) == DARK_COLOR.getRed() &&
      Grid.getGreen(robotX, robotY) == DARK_COLOR.getGreen() &&
      Grid.getBlue(robotX, robotY) == DARK_COLOR.getBlue();
  }
  
  public static boolean frontIsClear()
  {
    if (!loaded)
      throw new RuntimeException("Map not loaded yet");
    int x = robotX;
    int y = robotY;
    if (direction == 'N')
      y = y - 1;
    else if (direction == 'S')
      y = y + 1;
    else if (direction == 'E')
      x = x + 1;
    else
      x = x - 1;
    
    return Grid.isValid(x, y) && Grid.getImage(x, y).equals("");
  }
  
  public static void setDelay(double seconds)
  {
    delay = seconds;
  }
}