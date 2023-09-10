package minigames.client.wordle;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

// A Baox contains a single guessed letter
public class Box {
    static final int boxSize = 64;
    static final int margin = 10;
    static final int gap = 5;

    private int column;
    private int row;
    private String letter;
    static final Font font = new Font(Font.MONOSPACED.toString(), Font.BOLD, boxSize);
    private Color colour;

    
    
    public Box(int col, int row, String letter){
        this.column = col;
        this.row = row;
        this.letter = letter;
        this.colour = Color.BLACK;
    }

    public void setLetter(String letter){
        this.letter = letter;
    }
    public String getLetter(){
        return this.letter;
    }

    public void setColour(Color colour){
        this.colour = colour;
    }

    int xOffset(Component comp){
        return (comp.getWidth() - ((2 * margin) + Wordle.wordLength * (gap + boxSize + gap)))/2;
    }

    Rectangle rect(Component comp) {
        int x = new wordleCanvas().left(this.column) + gap + xOffset(comp);
        int y = new wordleCanvas().top(this.row) + gap;
        return new Rectangle(x, y, boxSize, boxSize);
    }


    /** Paints this Box including the letter */
    public void draw(Graphics2D g2d, Component comp) {
        
        g2d.setFont(Box.font);
        // Calculate the coordinates for drawing the letter
        FontMetrics fm = g2d.getFontMetrics();
        int y = this.rect(comp).y + this.rect(comp).height + ((this.rect(comp).height - fm.getHeight()) / 2);
        int x = this.rect(comp).x + ((this.rect(comp).width - fm.stringWidth(this.letter))/2);
        // Draw things
        g2d.setColor(this.colour);
        g2d.fill(this.rect(comp));
        g2d.setColor(Color.WHITE);
        g2d.drawRect(this.rect(comp).x, this.rect(comp).y, this.rect(comp).width, this.rect(comp).height);
        g2d.setColor(Color.WHITE);
        g2d.drawString(this.letter.toUpperCase(), x, y);

    }
}