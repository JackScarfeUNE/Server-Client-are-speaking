package minigames.client.wordle;

import java.awt.*;
// import java.util.List;
import java.util.ArrayList;

/* Canvas to draw the letters on 
     * Strongly derived froms Dots and Boxes
    */
    class wordleCanvas extends Canvas {
        final ArrayList<Box> boxes = new ArrayList<>();
        int currentBox;

        // The coordinate of the top or left of a the painting area for this row/col
        public int top(int row) {
            return Box.margin + row * (Box.gap + Box.boxSize + Box.gap);
        }

        public int left(int col) {
            return Box.margin + col * (Box.gap + Box.boxSize + Box.gap);
        }

        // TODO: Improve this description, and all the rest.
        /** Add a new word to the canvas
            Accepts the string format that comes from the server
         */
        public void addWord(String formattedWord){
            String[] chars = formattedWord.split("");
            int charCount = chars.length;
            if (currentBox + charCount/2 <= boxes.size()){
                for (int c = 0; c < charCount; c = c + 2) {
                    Box box = boxes.get(currentBox);               
                    switch(chars[c + 1]){
                        case "!" -> box.setColour(Color.GREEN);
                        case "?" -> box.setColour(Color.YELLOW);
                        case "_" -> box.setColour(Color.GRAY);
                    }
                    box.setLetter(chars[c]);
                    currentBox += 1;
                }
            }  
        }

        public wordleCanvas(){
             // Size the canvas to just contain the elements
             int width = left(Wordle.wordLength) + Box.margin + Box.boxSize;
             int height = top(Wordle.guessMax) + Box.margin + Box.boxSize;
             this.setPreferredSize(new Dimension(width, height));

             // Create records for the boxes
            
             for (int row = 0; row < Wordle.guessMax; row++) {
                 for (int col = 0; col < Wordle.wordLength; col++) {
                     boxes.add(new Box(col, row, ""));
                 }
             }
            
        }

        @Override public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D)g;

            g.clearRect(0, 0, this.getWidth(), this.getHeight());
            g2d.setColor(Color.gray);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());

            // Paint the boxes
            for (Box b : boxes) {
                b.draw(g2d, this);
            }

        }
    }
