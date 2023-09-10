package minigames.client.wordle;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import io.vertx.core.json.JsonObject;
import minigames.client.GameClient;
import minigames.client.MinigameNetworkClient;
import minigames.rendering.GameMetadata;
import minigames.commands.CommandPackage;

public class Wordle implements GameClient {

    static final int wordLength = 5;
    static final int guessMax = 6;

    MinigameNetworkClient mnClient;

    /** We hold on to this because we'll need it when sending commands to the server */
    GameMetadata gm;

    /** Your name */    
    String player;

    JButton submit;
    JPanel panel;
    JTextArea text;
    String input;
    JTextField guess;
    wordleCanvas wCanvas;

    public Wordle() {
        
        wCanvas = new wordleCanvas();
        
        submit = new JButton("Submit");

       
        text = new JTextArea();
        guess = new JTextField(20);
        
        // submit.addActionListener((evt) -> sendCommand(guess.getText()));

        submit.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // the input is the correct number of chars
                if (guess.getText().length() == 5) {
                    sendCommand(guess.getText());
                }
            }
            });

        panel = new JPanel();
        for (Component c : new Component[] { guess, submit }) {
            panel.add(c);
        }

    }

    /** 
     * Sends a command to the game at the server.
     * This being a text adventure, all our commands are just plain text strings our gameserver will interpret.
     * We're sending these as 
     * { "command": command }
     */
    public void sendCommand(String command) {
        JsonObject json = new JsonObject().put("command", command);

        // Collections.singletonList() is a quick way of getting a "list of one item"
        mnClient.send(new CommandPackage(gm.gameServer(), gm.name(), player, Collections.singletonList(json)));
    }
 

    /**
     * What we do when our client is loaded into the main screen
     */
    @Override
    public void load(MinigameNetworkClient mnClient, GameMetadata game, String player) {
        this.mnClient = mnClient;
        this.gm = game;
        this.player = player;

        // Add our components to the north, south, east, west, or centre of the main window's BorderLayout
        mnClient.getMainWindow().addCenter(wCanvas);
        mnClient.getMainWindow().addSouth(panel);   

        

        // Don't forget to call pack - it triggers the window to resize and repaint itself
        mnClient.getMainWindow().pack();     
    }

    @Override
    public void execute(GameMetadata game, JsonObject command) {
        this.gm = game;

        switch (command.getString("command")) {
            case "Correct!" -> {
            String guessedString = command.getString("guess");
            String formattedWord = "";
            String[] chars = guessedString.split("");
            for (int c = 0; c < chars.length; c++){
                formattedWord = formattedWord + chars[c] + "_";
            }
            
            // Add a word to the canvas then repaint it
            wCanvas.addWord(formattedWord);
            wCanvas.repaint();
        }
            case "Incorrect!" -> {
            String guessedString = command.getString("guess");
            String formattedWord = "";
            String[] chars = guessedString.split("");
            for (int c = 0; c < chars.length; c++){
                formattedWord = formattedWord + chars[c] + "_";
            }
            
            // Add a word to the canvas then repaint it
            wCanvas.addWord(formattedWord);
            wCanvas.repaint();
        }};
        
    }

    @Override
    public void closeGame() {
        // Nothing to do        
    }

    
    
}
