import java.util.*;
import java.awt.*;
import java.awt.event.*;

import java.io.File;  //This library is used to search for the audio file in the game       
import java.io.IOException; //This library is used when exceptions or errors occur while using the File library.
import javax.sound.sampled.*; //This library is especially needed when dealing with audio in this program.
//The above three libraries have to be imported for the music in the game.

import javax.swing.*;

public class CookieClicker extends JFrame {
    // non graphical variables
    private int cookies = 0;
    private int clicker = 1;
    private int clickerPrice = 20;
    
    //These two variables are used to dictate the size of the JFrame.
    private static int screenWidth=420; 
    private static int screenHeight=400;
    
    private final static Font FONT=new Font("Serif", Font.BOLD, 14); //The fonts of most texts are set to this font. This is a bold Serif 14 font.
    private final static Color WINDOW_COLOR=new Color(125,125,125); //This gray color is applied to the windows in the game.
    private final static Color LABEL_COLOR= new Color(0, 102, 0) ; //This green color is applied to the labels.
    private final static Color BUTTON_COLOR=new Color(153,0,0); //This red color is applied to the buttons.
    // graphical variables
    int numberOfColumns = 7;

    Container container;

    JLabel cookieLabel;
    JButton increaseCookiesButton;

    JLabel clickerLabel;
    JButton increaseClickerButton;

    // buildings
    Building bakery;
    boolean bakeryUnlocked;

    Building robot;
    boolean robotUnlocked;

    Building factory;
    boolean factoryUnlocked;

    //This image of a notification is used
    public static ImageIcon notification=new ImageIcon("Images/notification.jpg"); 
    
    public CookieClicker() {
        container = getContentPane();
        /**The first parameter of GridLayout is set to the variable numberOfColumns, which is equal to 7. 
         * 7 columns is needed for the beginning of the game.
         */
        container.setLayout(new GridLayout(numberOfColumns, 1)); 
        container.setBackground(WINDOW_COLOR); //The container's background color is set to the window color.
        bakery = new Building("Bakery", 0, 1, 20, "Images/bakery.jpg");
        bakeryUnlocked = false;

        robot = new Building("Robot", 0, 5, 100, "Images/robot.jpg");
        robotUnlocked = false;

        factory = new Building("Factory", 0, 10, 200, "Images/factory.jpg");
        factoryUnlocked = false;

        // produce cookies by hand
        cookieLabel = new JLabel("Cookies: " + cookies);
        cookieLabel.setForeground(LABEL_COLOR);//The label's color is set to the variable LABEL_COLOR.
        cookieLabel.setFont(FONT); //The label's font is set to the variable FONT.
        
        increaseCookiesButton=new JButton(new ImageIcon("Images/cookie.jpg"));
        /**The button's foreground color is set to the variable BUTTON_COLOR.
         * Due to this, the button's text is the same as the color of BUTTON_COLOR.
         * BUTTON_COLOR is a red color, so the button's text is red.
         */
        increaseCookiesButton.setForeground(BUTTON_COLOR); 
        //The text of the button is set to "Increase cookies".
        increaseCookiesButton.setText("Increase cookies");
        //The button's font is set to the variable FONT.
        increaseCookiesButton.setFont(FONT);
        //By putting the increaseCookiesButton into the first parameter of the function, this button's background aspects will be hidden.
        transparentBackground(increaseCookiesButton);
        increaseCookiesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cookies += clicker;
            }
        });

        // improve clicking production rate
        clickerLabel = new JLabel("Clicker Level: " + clicker);
        clickerLabel.setForeground(LABEL_COLOR);
        ImageIcon clickerIcon=new ImageIcon("Images/clicker.jpg");
        //The label's font is set to the variable FONT.
        clickerLabel.setFont(FONT);
        increaseClickerButton = new JButton(clickerIcon);
        /**The button's foreground color is set to the variable BUTTON_COLOR.
         * Due to this, the button's text is the same as the color of BUTTON_COLOR.
         * BUTTON_COLOR is a red color, so the button's text is red.
         */
        increaseClickerButton.setForeground(BUTTON_COLOR);
        //By putting the increaseClickerButton into the first parameter of the function, this button's background aspects will be hidden.
        transparentBackground(increaseClickerButton);
        //The button's font is set to the variable FONT.
        increaseClickerButton.setFont(FONT);
        increaseClickerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                increaseClicker();
            }
            

            private void increaseClicker() {
            	/** This label is used to display a message to the user in the JOptionPane.
            	 * The label's font is set to the variable FONT and the text color, or foreground color, of the label is set to the 
            	 * variable LABEL_COLOR. 
            	 */
            	JLabel notificationLabel=new JLabel("");
            	notificationLabel.setFont(FONT);
            	notificationLabel.setForeground(LABEL_COLOR);
                if(cookies >= clickerPrice) {
                    clicker++;
                    cookies -= clickerPrice;
                    clickerPrice *= 2;
                    //The label's text is set to "You have improved your clicker!".
                    notificationLabel.setText("You have improved your clicker!");
                    /**The JOptionPane displays the notificationLabel in a plain message format. 
                     *The title of the window is Notification and an image of a notification is shown.
                     */
                    JOptionPane.showMessageDialog(null, notificationLabel, "Notification", JOptionPane.PLAIN_MESSAGE, notification);
                } else {
                	 //The label's text is set to "You do not have enough money!".
                	notificationLabel.setText("You do not have enough money!");
                	/**The JOptionPane displays the notificationLabel in a plain message format. 
                     *The title of the window is Notification and an image of a notification is shown.
                     */
                    JOptionPane.showMessageDialog(null, notificationLabel, "Notification", JOptionPane.PLAIN_MESSAGE, notification);
                    
                }
            }
        });

        java.util.Timer actualizeProgress = new java.util.Timer();
        actualizeProgress.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                cookieLabel.setText("Cookies: " + cookies);
                clickerLabel.setText("Clicker Level: " + clicker);
                increaseClickerButton.setText("Improve Clicker (Costs: " + clickerPrice + ")");
            }
        }, 0, 25);

        java.util.Timer getMoreBuildings = new java.util.Timer(); 
        getMoreBuildings.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (bakeryUnlocked == false && clicker >= 2) {
                    bakery.unlock();
                    bakeryUnlocked = true;
                }
                if (robotUnlocked == false && bakery.getLevel() >= 2) {
                    robot.unlock();
                    robotUnlocked = true;
                }         
                if (factoryUnlocked == false && robot.getLevel() >= 2) {
                    factory.unlock();
                    factoryUnlocked = true;
                }
            }
        }, 0, 2000);

        java.util.Timer produceWithBuildings = new java.util.Timer();
        produceWithBuildings.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                cookies += bakery.getProductionRate() + robot.getProductionRate() + factory.getProductionRate();
            }
        }, 0, 1000);

        container.add(cookieLabel);
        container.add(increaseCookiesButton);
        container.add(new JLabel("")); // blank label
        container.add(clickerLabel);
        container.add(increaseClickerButton);
    }

    public class Building {
        // non graphical variables
        private String name;
        private int level;
        private int productionRate;
        private int costs;

        // graphical variables
        JLabel label;
        JButton button;
        ImageIcon icon; //This icon is applied to the button
        
        
       //The parameter iconName is equal to the location of an image in the files.
        public Building(String name, int level, int productionRate, int costs, String iconName) {
            // non graphical variables
            this.name = name;
            this.level = level;
            this.productionRate = productionRate;
            this.costs = costs;

            // graphical variables
            label = new JLabel();
            icon=new ImageIcon(iconName); 
            button = new JButton(icon); //The button displays an icon image
            
            /** Here, the label's and button's fonts are both set to
             * the variable FONT. The button's color is set to BUTTON_COLOR
             * and the label's color is set to LABEL_COLOR.
             */ 
            label.setForeground(LABEL_COLOR); 
            button.setForeground(BUTTON_COLOR);
            label.setFont(FONT);
            button.setFont(FONT);
            
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    improve();
                }
            });
       /**By adding the variable button as an argument for transparentBackground,
        * the background aspects of the button are hidden.
        */
            transparentBackground(button);
        }

        public int getLevel() {
            return level;
        }

        public void unlock() {
            numberOfColumns += 3;
            container.setLayout(new GridLayout(numberOfColumns, 1));
            container.add(new JLabel(""));
            container.add(label);
            container.add(button);
            setSize(screenWidth, getHeight() + 120);
            actualize();
        }

        public void improve() {
        	/** This label is used to display a message to the user in the JOptionPane.
        	 * The label's font is set to the variable FONT and the text color, or foreground color, of the label is set to the 
        	 * variable LABEL_COLOR. 
        	 */
        	JLabel notificationLabel=new JLabel("");
        	notificationLabel.setFont(FONT);
        	notificationLabel.setForeground(LABEL_COLOR);
            if(cookies >= costs) {
                level++;
                cookies -= costs;
                costs *= 2;
                //The label's text is changed to tell the user what has been improved.
                notificationLabel.setText("You have improved the " + name + "!");
                /**The JOptionPane displays the notificationLabel in a plain message format. 
                 *The title of the window is Notification and an image of a notification is shown.
                 */
                JOptionPane.showMessageDialog(null, notificationLabel, "Notification", JOptionPane.PLAIN_MESSAGE, notification);
            } else {
            	//The label's text is changed to tell the user what has been improved.
            	notificationLabel.setText("You have not enough money!");
                /**The JOptionPane displays the notificationLabel in a plain message format. 
                 *The title of the window is Notification and an image of a notification is shown.
                 */
            	JOptionPane.showMessageDialog(null, notificationLabel, "Notification", JOptionPane.PLAIN_MESSAGE, notification);
            }
            actualize();
        }

        public int getProductionRate() {
            return productionRate * level;
        }

        public void actualize() {
            label.setText(name + " Prod. Rate: " + getProductionRate());
            button.setText("Improve (costs: " + costs + ")");
        }
    }

    public static void main(String[] args) {
        CookieClicker cookieClicker = new CookieClicker();
        cookieClicker.setTitle("Cookie Clicker");
        /**The width of the JFrame is set to screenWidth.
         * The height of the JFrame is set to screenHeight.
         */
        cookieClicker.setSize(screenWidth, screenHeight);
        //The background of the JFrame is set to the variable WINDOW_COLOR
        cookieClicker.setBackground(WINDOW_COLOR);
        cookieClicker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cookieClicker.setVisible(true);
        //Resizable is set to false so the user is prevented from changing the size of the JFrame.
        cookieClicker.setResizable(false);
        //This function is called to customize UI related to panes.
        optionPaneCustomization();
        //This function is called to play the music of the game.
        music();
    }
    
    //This function takes in a button as a parameter and makes the background hidden.
    public static void transparentBackground(JButton button) {
        button.setBorderPainted(false); //The border of the button is not painted.
    }
 
    //This function customizes UI related to panes.
    public static void optionPaneCustomization() { 
    	//The background color of JOptionPanes are set to the variable WINDOW_COLOR.
    	UIManager.put("OptionPane.background", WINDOW_COLOR);
    	//The background color of JPanels are set to the variable WINDOW_COLOR.
    	UIManager.put("Panel.background", WINDOW_COLOR);
    	
    }
    
    //This function is used to play the music in the game.
    public static void music() { 
    	/**The try and catch method is used to avoid compiler errors.
    	 * In the try method, the program tries to play music.
    	 */
    	try {
    		/**The variable audio is set to a .wav file. 
    		 * The .wav file is accessed so that this music file can be played.
    		*/
			AudioInputStream audio=AudioSystem.getAudioInputStream(new File("Audio/CookieClicker.wav")); 
		    Clip soundClip=AudioSystem.getClip();
		    soundClip.open(audio);
		    //The music will loop endlessly until the program stops
		    soundClip.loop(Clip.LOOP_CONTINUOUSLY); 
    	} 
    	/**In the catch method, the function audioError is called.
    	 * This function is only called when there has been an error
    	 * with playing the audio.
    	 */
    	 catch (UnsupportedAudioFileException e) {
			audioError();
		} 
    	 catch (IOException e) {
			audioError();
		} 
    	 catch (LineUnavailableException e) {
		   audioError();
		}
    	
    	
    }
    /**In this function, a pop-up displays if an error with the audio in the game occurred.
     * The pop-up informs the user that an error with the audio in the game occurred.  
     */
    public static void audioError() {
    	ImageIcon errorIcon=new ImageIcon("Images/error.jpg"); //This variable stores an error icon, which is a .jpg.
    	/** This label is used to display a message to the user in the JOptionPane.
    	 * The label's font is set to the variable FONT and the text color, or foreground color, of the label is set to the 
    	 * variable LABEL_COLOR. 
    	 */
    	JLabel notificationLabel=new JLabel("");
    	notificationLabel.setFont(FONT);
    	notificationLabel.setForeground(LABEL_COLOR);
    	notificationLabel.setText("There has been an error with the audio!");
        /**The JOptionPane displays the notificationLabel in a plain message format. 
          *The title of the window is Error and an image of an error is shown.
          */
        JOptionPane.showMessageDialog(null, notificationLabel, "Error!", JOptionPane.PLAIN_MESSAGE, errorIcon);
    }
}