import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.io.*;
import java.util.*;

public class Formatter extends JFrame {
  final static int WIDTH = 400;
  final static int HEIGHT = 300;
  StyledDocument doc;
  JTextPane pane;
  JLabel statusInfo;

  public Formatter(String lab) {
    super (lab);

    // Get ContentPane
    Container c = getContentPane();

    // Setup Status Message Area
    statusInfo = new JLabel();
    c.add (statusInfo, BorderLayout.SOUTH);

    // Setup Text Pane
    doc = new DefaultStyledDocument();
    pane = new JTextPane (doc);

    // Place in JScrollPane
    JScrollPane sp = new JScrollPane (pane);
    c.add(sp, BorderLayout.CENTER);

    // Setup Menus
    JMenuBar menuBar = new JMenuBar();
    setJMenuBar (menuBar);

    // Setup File Menu
    JMenu file = new JMenu ("File");
    JMenuItem item;
    file.add (item = new JMenuItem ("New"));
    item.addActionListener (new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        doNewCommand();
      }
    });
    file.add (item = new JMenuItem ("Open"));
    item.addActionListener (new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        doOpenCommand();
      }
    });
    file.add (item = new JMenuItem ("Load Text"));
    item.addActionListener (new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        doLoadCommand();
      }
    });
    file.add (item = new JMenuItem ("Save"));
    item.addActionListener (new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        doSaveCommand();
      }
    });
    file.addSeparator();
    file.add (item = new JMenuItem ("Close"));
    item.addActionListener (new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        doCloseCommand();
      }
    });
    menuBar.add (file);

    // Setup Color Menu
    JMenu color = new JMenu("Color");
    color.add (item = new JMenuItem ("Red"));
    item.setIcon (new ColoredBox(Color.red));
    item.addActionListener (new
      StyledEditorKit.ForegroundAction (
        "set-foreground-red", Color.red));
    color.add (item = new JMenuItem ("Orange"));
    item.setIcon (new ColoredBox(Color.orange));
    item.addActionListener (new
      StyledEditorKit.ForegroundAction (
        "set-foreground-orange", Color.orange));
    color.add (item = new JMenuItem ("Yellow"));
    item.setIcon (new ColoredBox(Color.yellow));
    item.addActionListener (new
      StyledEditorKit.ForegroundAction (
        "set-foreground-yellow", Color.yellow));
    color.add (item = new JMenuItem ("Green"));
    item.setIcon (new ColoredBox(Color.green));
    item.addActionListener (new
      StyledEditorKit.ForegroundAction (
        "set-foreground-green", Color.green));
    color.add (item = new JMenuItem ("Blue"));
    item.setIcon (new ColoredBox(Color.blue));
    item.addActionListener (new
      StyledEditorKit.ForegroundAction (
        "set-foreground-blue", Color.blue));
    color.add (item = new JMenuItem ("Magenta"));
    item.setIcon (new ColoredBox(Color.magenta));
    item.addActionListener (new
      StyledEditorKit.ForegroundAction (
        "set-foreground-magenta", Color.magenta));
    color.add (item = new JMenuItem ("Custom Color"));
    item.addActionListener (new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        doColorCommand();
      }
    });

    menuBar.add (color);

    // Setup Font Menu
    JMenu font = new JMenu("Font");
    font.add (item = new JMenuItem ("12"));
    item.addActionListener (new
      StyledEditorKit.FontSizeAction (
        "font-size-12", 12));
    font.add (item = new JMenuItem ("24"));
    item.addActionListener (new
      StyledEditorKit.FontSizeAction (
        "font-size-24", 24));
    font.add (item = new JMenuItem ("36"));
    item.addActionListener (new
      StyledEditorKit.FontSizeAction (
        "font-size-36", 36));
    font.addSeparator();
    font.add (item = new JMenuItem ("Serif"));
    item.setFont (new Font ("Serif", Font.PLAIN, 12));
    item.addActionListener (new
      StyledEditorKit.FontFamilyAction (
        "font-family-Serif", "Serif"));
    font.add (item = new JMenuItem ("SansSerif"));
    item.setFont (new Font ("SansSerif", Font.PLAIN, 12));
    item.addActionListener (new
      StyledEditorKit.FontFamilyAction (
        "font-family-SansSerif", "SansSerif"));
    font.add (item = new JMenuItem ("Monospaced"));
    item.setFont (new Font ("Monospaced", Font.PLAIN, 12));
    item.addActionListener (new
      StyledEditorKit.FontFamilyAction (
        "font-family-Monospaced", "Monospaced"));
    font.addSeparator();
    font.add (item = new JMenuItem ("Bold"));
    item.setFont (new Font ("Serif", Font.BOLD, 12));
    item.addActionListener (
      new StyledEditorKit.BoldAction ());
    font.add (item = new JMenuItem ("Italic"));
    item.setFont (new Font ("Serif", Font.ITALIC, 12));
    item.addActionListener (
      new StyledEditorKit.ItalicAction ());
/* Add once FontChooser is available
    font.addSeparator();
    font.add (item = new JMenuItem ("Custom Font"));
    item.addActionListener (new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        doFontCommand();
      }
    });
*/
    menuBar.add (font);

    // Setup Insert Menu
    JMenu insert = new JMenu("Insert");
    insert.add (item = new JMenuItem ("Image File"));
    item.addActionListener (new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        doInsertImageCommand();
      }
    });
    menuBar.add (insert);
  }

  public static void main (String args[]) {
    Formatter frame = new Formatter("Mini Text Editor");
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {System.exit(0);}
    });
    frame.setSize(WIDTH, HEIGHT);
    frame.setVisible(true);
  }

  public void doNewCommand() {
    pane.setStyledDocument (doc = new DefaultStyledDocument());
  }

  public void doCloseCommand() {
    System.exit (0);
  }

  public void doOpenCommand() {
    try {
      FileInputStream fis = new FileInputStream ("doc.ser");
      ObjectInputStream ois = new ObjectInputStream (fis);
      doc = (StyledDocument)ois.readObject();
      ois.close();
      pane.setStyledDocument (doc);
      validate();
      statusInfo.setText ("Reloaded from disk");
    } catch (Exception e) {
      statusInfo.setText ("Unable to reload");
      e.printStackTrace();
    }
  }

  public void doSaveCommand() {
    try {
      FileOutputStream fos = new FileOutputStream ("doc.ser");
      ObjectOutputStream oos = new ObjectOutputStream (fos);
      oos.writeObject (doc);
      oos.flush();
      oos.close();
      statusInfo.setText ("Saved to disk");
    } catch (IOException e) {
      statusInfo.setText ("Unable to save");
      e.printStackTrace();
    }
  }

  public void doLoadCommand() {
    String msg;
    JFileChooser chooser = new JFileChooser();
    int status = chooser.showOpenDialog(this);
    if (status == JFileChooser.APPROVE_OPTION) {
      char data[];
      final Runnable doWaitCursor = new Runnable() {
        public void run() {
          setCursor (Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }
      };
      Thread appThread = new Thread() {
        public void run() {
          try {
             SwingUtilities.invokeAndWait(doWaitCursor);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      };
      appThread.start(); 
      File f = chooser.getSelectedFile();
      try {
        // Clear out current document
        pane.setStyledDocument (doc = new DefaultStyledDocument());
        // Read in text file
        FileReader fin = new FileReader (f);
        BufferedReader br = new BufferedReader (fin);
        char buffer[] = new char[4096];
        int len;
        while ((len = br.read (buffer, 0, buffer.length)) != -1) {
          // Insert into pane
          doc.insertString(doc.getLength(), 
            new String (buffer, 0, len), null);
        }
        statusInfo.setText ("Loaded: " + f.getName());
      } catch (BadLocationException exc) {
        statusInfo.setText ("Error loading: " + f.getName());
      } catch (FileNotFoundException exc) {
        statusInfo.setText ("File Not Found: " + f.getName());
      } catch (IOException exc) {
        statusInfo.setText ("IOException: " + f.getName());
      }
      final Runnable undoWaitCursor = new Runnable() {
        public void run() {
        setCursor (Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
      };
      appThread = new Thread() {
        public void run() {
          try {
             SwingUtilities.invokeAndWait(undoWaitCursor);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      };
      appThread.start(); 
    }
  }

/*
  public void doFontCommand() {
    Font font = FontChooser.ask (
      this, "Change font", getFont(), null);
    if (font != null) {
      MutableAttributeSet attr = new SimpleAttributeSet();
      StyleConstants.setFontFamily (attr, font.getFamily());
      StyleConstants.setFontSize (attr, font.getSize());
      StyleConstants.setBold (attr, font.isBold());
      StyleConstants.setItalic (attr, font.isItalic());
      pane.setCharacterAttributes(attr, false); 
    }
  }
*/

  public void doColorCommand() {
    Color color = JColorChooser.showDialog(
      this, "Color Chooser", Color.cyan);
    if (color != null) {
      MutableAttributeSet attr = new SimpleAttributeSet();
      StyleConstants.setForeground(attr, color);
      pane.setCharacterAttributes(attr, false); 
    }
  }

  public void doInsertImageCommand() {
    JFileChooser chooser = new JFileChooser();
    int status = chooser.showOpenDialog(this);
    if (status == JFileChooser.APPROVE_OPTION) {
      File file = chooser.getSelectedFile();
      Icon icon = new ImageIcon (file.getAbsolutePath());
      pane.insertIcon (icon);
    }
  }

  class ColoredBox implements Icon {
    Color color;
    public ColoredBox (Color c) {
      color = c;
    }
    public void paintIcon (Component c, Graphics g, int x, int y) {
      g.setColor(color);
      g.fillRect (x, y, getIconWidth(), getIconHeight());
    }
    public int getIconWidth() {
      return 10;
    }
    public int getIconHeight() { 
      return 10;
    }
  }
}
