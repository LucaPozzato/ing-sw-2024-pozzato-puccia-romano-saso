package it.polimi.ingsw.codexnaturalis.view;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class ChatCli {
    String chat;
    BufferedReader stdin;
    String inputString = "";
    List<String> outputString = new ArrayList<String>();
    int width = 70;
    int height = 50;
    List<String> messages = new ArrayList<String>(List.of("<nickname, everyone>: ", "message: "));
    List<String> command = new ArrayList<String>(List.of("To", "Send"));

    public ChatCli(BufferedReader stdin) {
        this.stdin = stdin;
        this.chat = "";
    }

    public void updateChat(String chat) {
        this.chat = chat;
    }

    public List<String> print() {
        // set console size
        System.out.print("\u001B[8;" + height + ";" + width + "t");

        // clear console
        System.out.println("\033c");

        // function that prints the chat
        int x = 2;
        int y = 2;
        int boxWidth;
        int boxHeight;
        String title = "Chat";

        boxWidth = width - 2;
        boxHeight = height - 5;

        printBox(x, y, boxWidth, boxHeight, title);

        // get chat to fit in the box
        String[] chatLines = chat.split("\n");
        // if line is bigger than box width, split in the line and create new chat
        // string with the \n at the right place
        chat = "";
        for (int i = 0; i < chatLines.length; i++) {
            if (chatLines[i].length() > boxWidth - 2) {
                String[] splitLine = chatLines[i].split("(?<=\\G.{" + (boxWidth - 2) + "})");
                for (int j = 0; j < splitLine.length; j++) {
                    chat += splitLine[j] + "\n";
                }
            } else
                chat += chatLines[i] + "\n";
        }

        // print only the last boxHeight lines of the chat
        chatLines = chat.split("\n");
        int start = chatLines.length - boxHeight + 2;
        if (start < 0)
            start = 0;
        for (int i = start; i < chatLines.length; i++) {
            System.out.print("\u001B[" + (y + i - start + 1) + ";" + (x + 1) + "H" + chatLines[i]);
        }

        // print input box
        printBox(x, y + boxHeight, boxWidth, 3, "Input");

        for (int i = 0; i < messages.size(); i++) {
            // moves cursor to the left of input box
            System.out.print("\u001B[" + (y + boxHeight + 1) + ";"
                    + (x + 1) + "H" + "\u001B[48;5;22m" + command.get(i) + "\u001B[0m -> \u001B[38;5;242m"
                    + messages.get(i) + "\u001B[0m");

            // read input from user
            try {
                char c = (char) stdin.read();
                while (c != '\n') {
                    inputString += Character.toString(c);
                    c = (char) stdin.read();
                }
                outputString.add(inputString);
                inputString = "";
            } catch (Exception e) {
                e.printStackTrace();
            }

            // clear input box
            System.out.print("\u001B[" + (y + boxHeight + 1) + ";"
                    + (x + 1) + "H" + " ".repeat(boxWidth - 1));
        }
        return outputString;
    }

    private void printBox(int x, int y, int boxWidth, int boxHeight, String title) {
        // function that prints a box with a title in the center
        System.out.print("\u001B[38;5;242m"); // set color to gray

        for (int i = 0; i < boxHeight; i++) {
            for (int j = 0; j < boxWidth; j++) {
                if (i == 0 && j == 0)
                    System.out.print("\u001B[" + (y + i) + ";" + (x + j) + "H╭");
                else if (i == 0 && j == boxWidth - 1)
                    System.out.print("\u001B[" + (y + i) + ";" + (x + j) + "H╮");
                else if (i == boxHeight - 1 && j == 0)
                    System.out.print("\u001B[" + (y + i) + ";" + (x + j) + "H╰");
                else if (i == boxHeight - 1 && j == boxWidth - 1)
                    System.out.print("\u001B[" + (y + i) + ";" + (x + j) + "H╯");
                else if (i == 0 && j == (boxWidth - title.length()) / 2) {
                    System.out.print("\u001B[" + (y + i) + ";" + (x + j - 1) + "H " + title);
                    j = j + title.length();
                } else if (i == 0 || i == boxHeight - 1)
                    System.out.print("\u001B[" + (y + i) + ";" + (x + j) + "H─");
                else if (j == 0 || j == boxWidth - 1)
                    System.out.print("\u001B[" + (y + i) + ";" + (x + j) + "H│");
            }
        }
        System.out.print("\u001B[0m"); // reset color
    }
}
