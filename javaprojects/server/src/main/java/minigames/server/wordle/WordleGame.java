package minigames.server.wordle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.util.JSONPObject;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import minigames.commands.CommandPackage;
import minigames.rendering.*;
import minigames.rendering.NativeCommands.LoadClient;

/**
 * Represents an actual Muddle game in progress
 */
public class WordleGame {

    
    /** A logger for logging output */
    private static final Logger logger = LogManager.getLogger(WordleGame.class);

    static int WIDTH = 2;
    static int HEIGHT = 2;

    record WordlePlayer(
        String name,
        int x, int y,
        List<String> inventory
    ) {    
    }

    /** Uniquely identifies this game */
    String name;
    // String list_file = "server\\src\\main\\resources\\target.txt";

    // String answer = getRandomWord();

    public WordleGame(String name) {
        this.name = name;
    }

    

    HashMap<String, WordlePlayer> players = new HashMap<>();

    /** The players currently playing this game */
    public String[] getPlayerNames() {
        return players.keySet().toArray(String[]::new);
    }

    /** Metadata for this game */
    public GameMetadata gameMetadata() {
        return new GameMetadata("Wordle", name, getPlayerNames(), true);
    }

    /** Describes the state of a player */
    private String describeState(WordlePlayer p) {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("[%d,%d] \n\n", p.x, p.y));

        return sb.toString();
    }

    


    public RenderingPackage runCommands(CommandPackage cp) {   
        logger.info("Received command package {}", cp);     
        WordlePlayer p = players.get(cp.player());

        // FIXME: Need to actually run the commands!

        ArrayList<JsonObject> renderingCommands = new ArrayList<>();
        String inputvalue = cp.commands().get(0).getString("command");
        //placeholder value answer because I can't get the logic to function yet
        String answer = "blank";
        if (inputvalue.equals(answer)) {
            renderingCommands.add(new JsonObject().put("command", "Correct!").put("guess", inputvalue));
        } else {
            renderingCommands.add(new JsonObject().put("command", "Incorrect!").put("guess", inputvalue));
        }
        
        return new RenderingPackage(this.gameMetadata(), renderingCommands);
    }

    /** Joins this game */
    public RenderingPackage joinGame(String playerName) {
        if (players.containsKey(playerName)) {
            return new RenderingPackage(
                gameMetadata(),
                Arrays.stream(new RenderingCommand[] {
                    new NativeCommands.ShowMenuError("That name's not available")
                }).map((r) -> r.toJson()).toList()
            );
        } else {
            WordlePlayer p = new WordlePlayer(playerName, 0, 0, List.of());
            players.put(playerName, p);

            ArrayList<JsonObject> renderingCommands = new ArrayList<>();
            renderingCommands.add(new LoadClient("Wordle", "Wordle", name, playerName).toJson());
            
            
            return new RenderingPackage(gameMetadata(), renderingCommands);
        }

    }

    
    // String list_file = "javaprojects/server/src/main/resources/target.txt";

    // String answer = getRandomWord();

    // public String getRandomWord() {
    //     List<String> word_list = readDictionary(list_file);
    //     Random rand = new Random(); //instance of random class
    //     int upperbound = word_list.size();
    //     // generate random values from 0 to arrayList size
    //     int int_random = rand.nextInt(upperbound);
    //     return word_list.get(int_random);
    // }

    // public List<String> readDictionary(String fileName) {

    //     List<String> wordList = new ArrayList<>();

    //     try {
    //         // Open and read the dictionary file
    //         InputStream in = getClass().getClassLoader().getResourceAsStream(fileName);
    //         assert in != null;
    //         BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    //         String strLine;

    //         //Read file line By line
    //         while ((strLine = reader.readLine()) != null) {
    //             wordList.add(strLine);
    //         }

    //         //Close the input stream
    //         in.close();

    //     } catch (Exception e) {//Catch exception if any
    //         System.err.println("Error: " + e.getMessage());
    //     }
    //     return wordList;
    // }

    
    
}
