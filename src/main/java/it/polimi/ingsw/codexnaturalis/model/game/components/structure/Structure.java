package it.polimi.ingsw.codexnaturalis.model.game.components.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Objects;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Resource;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import javafx.util.Pair;

public class Structure {
    private List<Card> timeStamp;
    private Map<Card, Pair<Integer, Boolean>> cardToCoordinate;
    private Map<Integer, Pair<Card, Boolean>> coordinateToCard;
    private Integer destinationCoord;
    private Map<String, Integer> visibleSymbols;
    private char[][] visualStructure = new char[174][506];
    private String[][] skeletonStructure = new String[80][80];
    private int maxXVisual = 0;
    private int maxYVisual = 0;
    private int minXVisual = 506;
    private int minYVisual = 174;
    private int maxXSkeleton = 0;
    private int maxYSkeleton = 0;
    private int minXSkeleton = 80;
    private int minYSkeleton = 80;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[38;5;196m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_BG_BRIGHT_RED = "\u001B[48;5;124m";
    public static final String ANSI_DARK_GRAY = "\u001B[38;5;242m";
    public static final String ANSI_BG_YELLOW = "\u001B[48;5;94m";
    public static final String ANSI_BG_RED = "\u001B[48;5;88m";
    public static final String ANSI_BG_GREEN = "\u001B[48;5;22m";
    public static final String ANSI_BG_BLUE = "\u001B[48;5;19m";
    public static final String ANSI_BG_PURPLE = "\u001B[48;5;55m";
    private List<Card> avaiablePatterns;

    public Structure() {
        this.timeStamp = new ArrayList<>();
        this.cardToCoordinate = new HashMap<>();
        this.coordinateToCard = new HashMap<>();
        this.visibleSymbols = new HashMap<>(
                Map.of("VEGETABLE", 0, "ANIMAL", 0, "INSECT", 0, "SHROOM", 0, "INK", 0, "SCROLL", 0,
                        "FEATHER", 0, "EMPTY", 0, "NULL", 0));
    }

    // frontUp = true -> front of the card is visible
    // frontUp = false -> back of the card is visible
    public void placeCard(Card father, Card card, String position, Boolean frontUp) throws IllegalCommandException {
        // Calculates coordinates of new card
        destinationCoord = calcCoordinate(father, position);
        // Checks if the card can be placed and places it
        isPlaceable(father, card, destinationCoord, position);
        if (card instanceof InitialCard) {
            cardToCoordinate.put(card, new Pair<Integer, Boolean>(4040, frontUp));
            coordinateToCard.put(4040, new Pair<Card, Boolean>(card, frontUp));
        } else {
            cardToCoordinate.put(card, new Pair<Integer, Boolean>(destinationCoord, frontUp));
            coordinateToCard.put(destinationCoord, new Pair<Card, Boolean>(card, frontUp));

        }
        timeStamp.add(card);
        addCardToVisual(card, frontUp);
        addCardToSkeleton(card);
        calcVisibleSymbols(father, card, position, frontUp);
    }

    private void calcVisibleSymbols(Card father, Card card, String position, Boolean frontUp)
            throws IllegalCommandException {

        // Resets visible symbols
        for (String symbol : visibleSymbols.keySet()) {
            visibleSymbols.put(symbol, 0);
        }

        // Updates visible symbols
        for (int i = minYVisual - 2; i <= maxYVisual + 2; i++) {
            for (int j = minXVisual - 6; j <= maxXVisual + 6; j++) {
                switch (visualStructure[i][j]) {
                    case 'A':
                        visibleSymbols.put("ANIMAL", visibleSymbols.get("ANIMAL") + 1);
                        break;
                    case 'V':
                        visibleSymbols.put("VEGETABLE", visibleSymbols.get("VEGETABLE") + 1);
                        break;
                    case 'I':
                        visibleSymbols.put("INSECT", visibleSymbols.get("INSECT") + 1);
                        break;
                    case 'S':
                        visibleSymbols.put("SHROOM", visibleSymbols.get("SHROOM") + 1);
                        break;
                    case 's':
                        visibleSymbols.put("SCROLL", visibleSymbols.get("SCROLL") + 1);
                        break;
                    case 'i':
                        visibleSymbols.put("INK", visibleSymbols.get("INK") + 1);
                        break;
                    case 'f':
                        visibleSymbols.put("FEATHER", visibleSymbols.get("FEATHER") + 1);
                        break;
                    case 'N':
                        visibleSymbols.put("NULL", visibleSymbols.get("NULL") + 1);
                        break;
                    case 'E':
                        visibleSymbols.put("EMPTY", visibleSymbols.get("EMPTY") + 1);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public Map<String, Integer> getVisibleResources() {
        Map<String, Integer> visibleResources = new HashMap<>();
        for (Resource resource : Resource.values()) {
            visibleResources.put(resource.name(), visibleSymbols.get(resource.name()));
        }
        return visibleResources;
    }

    public Map<String, Integer> getVisibleObjects() {
        Map<String, Integer> visibleObjects = new HashMap<>();
        for (Objects resource : Objects.values()) {
            visibleObjects.put(resource.name(), visibleSymbols.get(resource.name()));
        }
        return visibleObjects;
    }

    public List<Card> getTimestamp() {
        return timeStamp;
    }

    /*
     * Verifies if the card can be placed in the structure
     * 
     * @param father the father card
     * 
     * @param coordinate the coordinate of the new card
     * 
     * @param position the position of the new card
     * 
     * @return true if the card can be placed, false otherwise
     */
    private Boolean isPlaceable(Card father, Card card, Integer coordinate, String position)
            throws IllegalCommandException {
        // checks if initial card is placeable
        if (card instanceof InitialCard) {
            if (coordinateToCard.containsKey(4040)) {
                throw new IllegalCommandException("Initial card already placed");
            } else
                return true;
        }

        // Checks if the father card is present
        if (father == null)
            throw new IllegalCommandException("Bottom card not specified");

        // Checks if another card is already placed in that coordinate
        if (coordinateToCard.containsKey(coordinate))
            throw new IllegalCommandException("Another card is already placed in that position");

        // Gets corners of correct side of the father card
        List<String> corners;

        if (cardToCoordinate.get(father).getValue())
            corners = father.getFrontCorners();
        else
            corners = father.getBackCorners();

        // Checks if the card can be placed in the coordinates if the bottom card's
        // angle is visible
        // BUG: need to check the angles of the other fathers
        switch (position) {
            case "TL":
                if (corners.get(0).equals("NULL"))
                    throw new IllegalCommandException("TL corner not visible");
                break;
            case "TR":
                if (corners.get(1).equals("NULL"))
                    throw new IllegalCommandException("TR corner not visible");
                break;
            case "BL":
                if (corners.get(2).equals("NULL"))
                    throw new IllegalCommandException("BL corner not visible");
                break;
            case "BR":
                if (corners.get(3).equals("NULL"))
                    throw new IllegalCommandException("BR corner not visible");
                break;
            default:
                break;
        }

        // Checks if the gold card has the requirements to be placed
        if (card instanceof GoldCard) {
            for (Map.Entry<String, Integer> entry : card.getRequirements().entrySet()) {
                if (visibleSymbols.get(entry.getKey()) < entry.getValue())
                    throw new IllegalCommandException("Gold card requirements not met");
            }
        }

        return true;
    }

    /*
     * Calculates the coordinate of the new card
     * 
     * @param father the father card
     * 
     * @param position the position of the new card
     * 
     * @return the coordinate of the new card
     */
    private Integer calcCoordinate(Card father, String position) {
        if (father == null)
            return 0;

        Integer fatherCoordinate = cardToCoordinate.get(father).getKey();

        // Important: this coordinates are correct in the Euclidean plane, but not when
        // thinking about the structure as a matrix. A matrix has the origin in the top
        // left corner, while the Euclidean plane has the origin in the bottom left
        // corner. This means that the y coordinate is inverted.

        switch (position) {
            case "TL":
                return fatherCoordinate - 99;
            case "TR":
                return fatherCoordinate + 101;
            case "BL":
                return fatherCoordinate - 101;
            case "BR":
                return fatherCoordinate + 99;
            default:
                return 0;
        }
    }

    public int countPattern(List<Card> patternList, Card placed) throws IllegalCommandException {
        int pointsFromPatterns = 0;

        if (patternList.isEmpty()) {
            return 0;
        } else {
            for (Card pattern : patternList) {
                if (pattern.getShape().equals("STAIRS")) {
                    if (searchStairPattern(pattern, placed)) {
                        pointsFromPatterns += 2;
                    }
                } else if (pattern.getShape().equals("CHAIR")) {
                    if (searchChairPattern(pattern, placed)) {
                        pointsFromPatterns += 3;
                    }
                } else
                    throw new IllegalCommandException("Unrecognized pattern");
            }
            return pointsFromPatterns;
        }
    }

    // TODO: implement calculatePoints method for GoldCard and ResourceCard points

    private boolean searchStairPattern(Card specificPattern, Card placed) throws IllegalCommandException {
        int placedCoordinate = cardToCoordinate.get(placed).getKey();
        switch (specificPattern.getIdCard()) {
            case "OP1":
                if ((coordinateToCard.get(placedCoordinate - 99) != null
                        && coordinateToCard.get(placedCoordinate - 99).getKey().getSymbol().equals("SHROOM")
                        && !coordinateToCard.get(placedCoordinate - 99).getKey().isVisited()) &&
                        coordinateToCard.get(placedCoordinate - 198) != null
                        && coordinateToCard.get(placedCoordinate - 198).getKey().getSymbol().equals("SHROOM")
                        && !coordinateToCard.get(placedCoordinate - 198).getKey().isVisited()) {
                    placed.visit();
                    coordinateToCard.get(placedCoordinate - 99).getKey().visit();
                    coordinateToCard.get(placedCoordinate - 198).getKey().visit();
                    return true;
                } else if ((coordinateToCard.get(placedCoordinate - 99) != null
                        && coordinateToCard.get(placedCoordinate - 99).getKey().getSymbol().equals("SHROOM")
                        && !coordinateToCard.get(placedCoordinate - 99).getKey().isVisited()) &&
                        coordinateToCard.get(placedCoordinate + 99) != null
                        && coordinateToCard.get(placedCoordinate + 99).getKey().getSymbol().equals("SHROOM")
                        && !coordinateToCard.get(placedCoordinate + 99).getKey().isVisited()) {
                    placed.visit();
                    coordinateToCard.get(placedCoordinate - 99).getKey().visit();
                    coordinateToCard.get(placedCoordinate + 99).getKey().visit();
                    return true;
                } else if ((coordinateToCard.get(placedCoordinate + 99) != null
                        && coordinateToCard.get(placedCoordinate + 99).getKey().getSymbol().equals("SHROOM")
                        && !coordinateToCard.get(placedCoordinate + 99).getKey().isVisited()) &&
                        coordinateToCard.get(placedCoordinate + 198) != null
                        && coordinateToCard.get(placedCoordinate + 198).getKey().getSymbol().equals("SHROOM")
                        && !coordinateToCard.get(placedCoordinate + 198).getKey().isVisited()) {
                    placed.visit();
                    coordinateToCard.get(placedCoordinate + 99).getKey().visit();
                    coordinateToCard.get(placedCoordinate + 198).getKey().visit();
                    return true;
                } else
                    return false;
            case "OP2":
                if ((coordinateToCard.get(placedCoordinate - 101) != null
                        && coordinateToCard.get(placedCoordinate - 101).getKey().getSymbol().equals("VEGETABLE")
                        && !coordinateToCard.get(placedCoordinate - 101).getKey().isVisited()) &&
                        coordinateToCard.get(placedCoordinate - 202) != null
                        && coordinateToCard.get(placedCoordinate - 202).getKey().getSymbol().equals("VEGETABLE")
                        && !coordinateToCard.get(placedCoordinate - 202).getKey().isVisited()) {
                    placed.visit();
                    coordinateToCard.get(placedCoordinate - 101).getKey().visit();
                    coordinateToCard.get(placedCoordinate - 202).getKey().visit();
                    return true;
                } else if ((coordinateToCard.get(placedCoordinate - 101) != null
                        && coordinateToCard.get(placedCoordinate - 101).getKey().getSymbol().equals("VEGETABLE")
                        && !coordinateToCard.get(placedCoordinate - 101).getKey().isVisited()) &&
                        coordinateToCard.get(placedCoordinate + 101) != null
                        && coordinateToCard.get(placedCoordinate + 101).getKey().getSymbol().equals("VEGETABLE")
                        && !coordinateToCard.get(placedCoordinate + 101).getKey().isVisited()) {
                    placed.visit();
                    coordinateToCard.get(placedCoordinate - 101).getKey().visit();
                    coordinateToCard.get(placedCoordinate + 101).getKey().visit();
                    return true;
                } else if ((coordinateToCard.get(placedCoordinate + 101) != null
                        && coordinateToCard.get(placedCoordinate + 101).getKey().getSymbol().equals("VEGETABLE")
                        && !coordinateToCard.get(placedCoordinate + 101).getKey().isVisited()) &&
                        coordinateToCard.get(placedCoordinate + 201) != null
                        && coordinateToCard.get(placedCoordinate + 201).getKey().getSymbol().equals("VEGETABLE")
                        && !coordinateToCard.get(placedCoordinate + 201).getKey().isVisited()) {
                    placed.visit();
                    coordinateToCard.get(placedCoordinate + 101).getKey().visit();
                    coordinateToCard.get(placedCoordinate + 201).getKey().visit();
                    return true;
                } else
                    return false;

            case "OP3":
                if ((coordinateToCard.get(placedCoordinate - 99) != null
                        && coordinateToCard.get(placedCoordinate - 99).getKey().getSymbol().equals("ANIMAL")
                        && !coordinateToCard.get(placedCoordinate - 99).getKey().isVisited()) &&
                        coordinateToCard.get(placedCoordinate - 198) != null
                        && coordinateToCard.get(placedCoordinate - 198).getKey().getSymbol().equals("ANIMAL")
                        && !coordinateToCard.get(placedCoordinate - 198).getKey().isVisited()) {
                    placed.visit();
                    coordinateToCard.get(placedCoordinate - 99).getKey().visit();
                    coordinateToCard.get(placedCoordinate - 198).getKey().visit();
                    return true;
                } else if ((coordinateToCard.get(placedCoordinate - 99) != null
                        && coordinateToCard.get(placedCoordinate - 99).getKey().getSymbol().equals("ANIMAL")
                        && !coordinateToCard.get(placedCoordinate - 99).getKey().isVisited()) &&
                        coordinateToCard.get(placedCoordinate + 99) != null
                        && coordinateToCard.get(placedCoordinate + 99).getKey().getSymbol().equals("ANIMAL")
                        && !coordinateToCard.get(placedCoordinate + 99).getKey().isVisited()) {
                    placed.visit();
                    coordinateToCard.get(placedCoordinate - 99).getKey().visit();
                    coordinateToCard.get(placedCoordinate + 99).getKey().visit();
                    return true;
                } else if ((coordinateToCard.get(placedCoordinate + 99) != null
                        && coordinateToCard.get(placedCoordinate + 99).getKey().getSymbol().equals("ANIMAL")
                        && !coordinateToCard.get(placedCoordinate + 99).getKey().isVisited()) &&
                        coordinateToCard.get(placedCoordinate + 198) != null
                        && coordinateToCard.get(placedCoordinate + 198).getKey().getSymbol().equals("ANIMAL")
                        && !coordinateToCard.get(placedCoordinate + 198).getKey().isVisited()) {
                    placed.visit();
                    coordinateToCard.get(placedCoordinate + 99).getKey().visit();
                    coordinateToCard.get(placedCoordinate + 198).getKey().visit();
                    return true;
                } else
                    return false;

            case "OP4":
                if ((coordinateToCard.get(placedCoordinate - 101) != null
                        && coordinateToCard.get(placedCoordinate - 101).getKey().getSymbol().equals("INSECT")
                        && !coordinateToCard.get(placedCoordinate - 101).getKey().isVisited()) &&
                        coordinateToCard.get(placedCoordinate - 202) != null
                        && coordinateToCard.get(placedCoordinate - 202).getKey().getSymbol().equals("INSECT")
                        && !coordinateToCard.get(placedCoordinate - 202).getKey().isVisited()) {
                    placed.visit();
                    coordinateToCard.get(placedCoordinate - 101).getKey().visit();
                    coordinateToCard.get(placedCoordinate - 202).getKey().visit();
                    return true;
                } else if ((coordinateToCard.get(placedCoordinate - 101) != null
                        && coordinateToCard.get(placedCoordinate - 101).getKey().getSymbol().equals("INSECT")
                        && !coordinateToCard.get(placedCoordinate - 101).getKey().isVisited()) &&
                        coordinateToCard.get(placedCoordinate + 101) != null
                        && coordinateToCard.get(placedCoordinate + 101).getKey().getSymbol().equals("INSECT")
                        && !coordinateToCard.get(placedCoordinate + 101).getKey().isVisited()) {
                    placed.visit();
                    coordinateToCard.get(placedCoordinate - 101).getKey().visit();
                    coordinateToCard.get(placedCoordinate + 101).getKey().visit();
                    return true;
                } else if ((coordinateToCard.get(placedCoordinate + 101) != null
                        && coordinateToCard.get(placedCoordinate + 101).getKey().getSymbol().equals("INSECT")
                        && !coordinateToCard.get(placedCoordinate + 101).getKey().isVisited()) &&
                        coordinateToCard.get(placedCoordinate + 201) != null
                        && coordinateToCard.get(placedCoordinate + 201).getKey().getSymbol().equals("INSECT")
                        && !coordinateToCard.get(placedCoordinate + 201).getKey().isVisited()) {
                    placed.visit();
                    coordinateToCard.get(placedCoordinate + 101).getKey().visit();
                    coordinateToCard.get(placedCoordinate + 201).getKey().visit();
                    return true;
                } else
                    return false;

            default:
                throw new IllegalCommandException("Illegal pattern recognized");
        }
    }

    private Boolean searchChairPattern(Card specificPattern, Card placed) throws IllegalCommandException {
        int placedCoordinate = cardToCoordinate.get(placed).getKey();
        switch (specificPattern.getIdCard()) {
            case "OP5":
                if (placed.getSymbol().equals("VEGETABLES")) {
                    if (coordinateToCard.get(placedCoordinate - 101) != null
                            && coordinateToCard.get(placedCoordinate - 101).getKey().getSymbol().equals("SHROOM")
                            && !coordinateToCard.get(placedCoordinate - 101).getKey().isVisited() &&
                            coordinateToCard.get(placedCoordinate - 201) != null
                            && coordinateToCard.get(placedCoordinate - 201).getKey().getSymbol().equals("SHROOM")
                            && !coordinateToCard.get(placedCoordinate - 201).getKey().isVisited()) {
                        placed.visit();
                        coordinateToCard.get(placedCoordinate - 101).getKey().visit();
                        coordinateToCard.get(placedCoordinate - 201).getKey().visit();
                        return true;
                    } else
                        return false;
                }
                if (placed.getSymbol().equals("SHROOM")) {
                    if (coordinateToCard.get(placedCoordinate + 100) != null
                            && coordinateToCard.get(placedCoordinate + 100).getKey().getSymbol().equals("SHROOM")
                            && !coordinateToCard.get(placedCoordinate + 100).getKey().isVisited() &&
                            coordinateToCard.get(placedCoordinate + 201) != null
                            && coordinateToCard.get(placedCoordinate + 201).getKey().getSymbol().equals("VEGETABLE")
                            && !coordinateToCard.get(placedCoordinate + 201).getKey().isVisited()) {
                        placed.visit();
                        coordinateToCard.get(placedCoordinate + 100).getKey().visit();
                        coordinateToCard.get(placedCoordinate + 201).getKey().visit();
                        return true;
                    } else if (coordinateToCard.get(placedCoordinate - 100) != null
                            && coordinateToCard.get(placedCoordinate - 100).getKey().getSymbol().equals("SHROOM")
                                    & !coordinateToCard.get(placedCoordinate - 100).getKey().isVisited()
                            &&
                            coordinateToCard.get(placedCoordinate + 101) != null
                            && coordinateToCard.get(placedCoordinate + 101).getKey().getSymbol().equals("VEGETABLE")
                            && !coordinateToCard.get(placedCoordinate + 101).getKey().isVisited()) {
                        placed.visit();
                        coordinateToCard.get(placedCoordinate - 100).getKey().visit();
                        coordinateToCard.get(placedCoordinate + 101).getKey().visit();
                        return true;
                    } else
                        return false;
                }

            case "OP6":
                if (placed.getSymbol().equals("INSECT")) {
                    if (coordinateToCard.get(placedCoordinate - 99) != null
                            && coordinateToCard.get(placedCoordinate - 99).getKey().getSymbol().equals("VEGETABLE")
                            && !coordinateToCard.get(placedCoordinate - 99).getKey().isVisited() &&
                            coordinateToCard.get(placedCoordinate - 199) != null
                            && coordinateToCard.get(placedCoordinate - 199).getKey().getSymbol().equals("VEGETABLE")
                            && !coordinateToCard.get(placedCoordinate - 199).getKey().isVisited()) {
                        placed.visit();
                        coordinateToCard.get(placedCoordinate - 99).getKey().visit();
                        coordinateToCard.get(placedCoordinate - 199).getKey().visit();
                        return true;
                    } else
                        return false;
                }
                if (placed.getSymbol().equals("VEGETABLE")) {
                    if (coordinateToCard.get(placedCoordinate + 100) != null
                            && coordinateToCard.get(placedCoordinate + 100).getKey().getSymbol().equals("VEGETABLE")
                            && !coordinateToCard.get(placedCoordinate + 100).getKey().isVisited() &&
                            coordinateToCard.get(placedCoordinate + 199) != null
                            && coordinateToCard.get(placedCoordinate + 199).getKey().getSymbol().equals("INSECT")
                            && !coordinateToCard.get(placedCoordinate + 199).getKey().isVisited()) {
                        placed.visit();
                        coordinateToCard.get(placedCoordinate + 100).getKey().visit();
                        coordinateToCard.get(placedCoordinate + 199).getKey().visit();
                        return true;
                    } else if (coordinateToCard.get(placedCoordinate - 100) != null
                            && coordinateToCard.get(placedCoordinate - 100).getKey().getSymbol().equals("SHROOM")
                            && !coordinateToCard.get(placedCoordinate - 100).getKey().isVisited() &&
                            coordinateToCard.get(placedCoordinate + 99) != null
                            && coordinateToCard.get(placedCoordinate + 99).getKey().getSymbol().equals("VEGETABLE")
                            && !coordinateToCard.get(placedCoordinate + 99).getKey().isVisited()) {
                        placed.visit();
                        coordinateToCard.get(placedCoordinate - 100).getKey().visit();
                        coordinateToCard.get(placedCoordinate + 99).getKey().visit();
                        return true;
                    } else
                        return false;
                }
            case "OP7":
                if (placed.getSymbol().equals("SHROOM")) {
                    if (coordinateToCard.get(placedCoordinate + 99) != null
                            && coordinateToCard.get(placedCoordinate + 99).getKey().getSymbol().equals("ANIMAL")
                            && !coordinateToCard.get(placedCoordinate + 99).getKey().isVisited() &&
                            coordinateToCard.get(placedCoordinate + 199) != null
                            && coordinateToCard.get(placedCoordinate + 199).getKey().getSymbol().equals("ANIMAL")
                            && !coordinateToCard.get(placedCoordinate + 199).getKey().isVisited()) {
                        placed.visit();
                        coordinateToCard.get(placedCoordinate + 99).getKey().visit();
                        coordinateToCard.get(placedCoordinate + 199).getKey().visit();
                        return true;
                    } else
                        return false;
                }
                if (placed.getSymbol().equals("ANIMAL")) {
                    if (coordinateToCard.get(placedCoordinate - 99) != null
                            && coordinateToCard.get(placedCoordinate - 99).getKey().getSymbol().equals("SHROOM")
                            && !coordinateToCard.get(placedCoordinate - 99).getKey().isVisited() &&
                            coordinateToCard.get(placedCoordinate + 100) != null
                            && coordinateToCard.get(placedCoordinate + 100).getKey().getSymbol().equals("ANIMAL")
                            && !coordinateToCard.get(placedCoordinate + 100).getKey().isVisited()) {
                        placed.visit();
                        coordinateToCard.get(placedCoordinate - 99).getKey().visit();
                        coordinateToCard.get(placedCoordinate + 100).getKey().visit();
                        return true;
                    } else if (coordinateToCard.get(placedCoordinate - 100) != null
                            && coordinateToCard.get(placedCoordinate - 100).getKey().getSymbol().equals("ANIMAL")
                            && !coordinateToCard.get(placedCoordinate - 100).getKey().isVisited() &&
                            coordinateToCard.get(placedCoordinate - 199) != null
                            && coordinateToCard.get(placedCoordinate - 199).getKey().getSymbol().equals("SHROOM")
                            && !coordinateToCard.get(placedCoordinate - 199).getKey().isVisited()) {
                        placed.visit();
                        coordinateToCard.get(placedCoordinate - 100).getKey().visit();
                        coordinateToCard.get(placedCoordinate - 199).getKey().visit();
                        return true;
                    } else
                        return false;
                }
            case "OP8":
                if (placed.getSymbol().equals("ANIMAL")) {
                    if (coordinateToCard.get(placedCoordinate + 101) != null
                            && coordinateToCard.get(placedCoordinate + 101).getKey().getSymbol().equals("INSECT")
                            && !coordinateToCard.get(placedCoordinate + 101).getKey().isVisited() &&
                            coordinateToCard.get(placedCoordinate + 201) != null
                            && coordinateToCard.get(placedCoordinate + 201).getKey().getSymbol().equals("INSECT")
                            && !coordinateToCard.get(placedCoordinate + 201).getKey().isVisited()) {
                        placed.visit();
                        coordinateToCard.get(placedCoordinate + 101).getKey().visit();
                        coordinateToCard.get(placedCoordinate + 201).getKey().visit();
                        return true;
                    } else
                        return false;
                }
                if (placed.getSymbol().equals("INSECT")) {
                    if (coordinateToCard.get(placedCoordinate - 101) != null
                            && coordinateToCard.get(placedCoordinate - 101).getKey().getSymbol().equals("ANIMAL")
                            && !coordinateToCard.get(placedCoordinate - 101).getKey().isVisited() &&
                            coordinateToCard.get(placedCoordinate + 100) != null
                            && coordinateToCard.get(placedCoordinate + 100).getKey().getSymbol().equals("INSECT")
                            && !coordinateToCard.get(placedCoordinate + 100).getKey().isVisited()) {
                        placed.visit();
                        coordinateToCard.get(placedCoordinate - 101).getKey().visit();
                        coordinateToCard.get(placedCoordinate + 100).getKey().visit();
                        return true;
                    } else if (coordinateToCard.get(placedCoordinate - 100) != null
                            && coordinateToCard.get(placedCoordinate - 100).getKey().getSymbol().equals("INSECT")
                            && !coordinateToCard.get(placedCoordinate - 100).getKey().isVisited() &&
                            coordinateToCard.get(placedCoordinate - 201) != null
                            && coordinateToCard.get(placedCoordinate - 201).getKey().getSymbol().equals("ANIMAL")
                            && !coordinateToCard.get(placedCoordinate - 201).getKey().isVisited()) {
                        placed.visit();
                        coordinateToCard.get(placedCoordinate - 100).getKey().visit();
                        coordinateToCard.get(placedCoordinate - 201).getKey().visit();
                        return true;
                    } else
                        return false;
                }
            default:
                throw new IllegalCommandException("Illegal pattern recognized");
        }
    }

    private void addCardToSkeleton(Card card) {
        int x = cardToCoordinate.get(card).getKey() / 100;
        // Inverting y coordinate to match the matrix structure
        int y = 40 - (cardToCoordinate.get(card).getKey() % 100 - 40);
        skeletonStructure[y][x] = card.getIdCard();
        if (x < minXSkeleton)
            minXSkeleton = x;
        if (x > maxXSkeleton)
            maxXSkeleton = x;
        if (y < minYSkeleton)
            minYSkeleton = y;
        if (y > maxYSkeleton)
            maxYSkeleton = y;
    }

    public void printSkeleton() {
        System.out.println();
        for (int i = minYSkeleton; i <= maxYSkeleton; i++) {
            for (int j = minXSkeleton; j <= maxXSkeleton; j++) {
                if (skeletonStructure[i][j] == null)
                    System.out.print("   ");
                else
                    System.out.print(skeletonStructure[i][j]);
            }
            System.out.printf("\n");
        }
        System.out.println();
    }

    private void addCardToVisual(Card card, Boolean frontUp) throws IllegalCommandException {
        List<String> corners;
        int x = cardToCoordinate.get(card).getKey() / 100;
        int y = cardToCoordinate.get(card).getKey() % 100;

        // Center of the matrix + x offset (6 pixels per card)
        x = 253 + ((x - 40) * 6);
        // Inverting y coordinate to match the matrix structure, center of the matrix +
        // y offset (2 pixels per card)
        y = 87 - ((y - 40) * 2);

        if (x < minXVisual)
            minXVisual = x;
        if (x > maxXVisual)
            maxXVisual = x;
        if (y < minYVisual)
            minYVisual = y;
        if (y > maxYVisual)
            maxYVisual = y;

        if (frontUp) {
            corners = card.getFrontCorners();
        } else {
            corners = card.getBackCorners();
        }

        drawTopLine(x, y);

        drawTopCornersLine(card, frontUp, corners, x, y);

        drawIdLine(card, frontUp, x, y);

        drawBottomCornersLine(card, frontUp, corners, x, y);

        drawBottomLine(x, y);
    }

    private void drawTopLine(int x, int y) {
        visualStructure[y - 2][x - 4] = '╭';
        visualStructure[y - 2][x - 3] = '─';
        visualStructure[y - 2][x - 2] = '┬';
        visualStructure[y - 2][x - 1] = '─';
        visualStructure[y - 2][x] = '─';
        visualStructure[y - 2][x + 1] = '─';
        visualStructure[y - 2][x + 2] = '┬';
        visualStructure[y - 2][x + 3] = '─';
        visualStructure[y - 2][x + 4] = '╮';
    }

    private void drawTopCornersLine(Card card, Boolean frontUp, List<String> corners, int x, int y)
            throws IllegalCommandException {
        visualStructure[y - 1][x - 4] = '│';
        if (corners.get(0).equals("INK") || corners.get(0).equals("SCROLL") || corners.get(0).equals("FEATHER"))
            visualStructure[y - 1][x - 3] = Character.toLowerCase(corners.get(0).charAt(0));
        else
            visualStructure[y - 1][x - 3] = corners.get(0).charAt(0);
        visualStructure[y - 1][x - 2] = '│';

        if (card instanceof GoldCard && !((GoldCard) card).getPointsType().equals("NULL")) {
            switch (((GoldCard) card).getPointsType()) {
                case "INK":
                    visualStructure[y - 1][x - 1] = 'І';
                    break;
                case "SCROLL":
                    visualStructure[y - 1][x - 1] = 'Ѕ';
                    break;
                case "FEATHER":
                    visualStructure[y - 1][x - 1] = 'Ϝ';
                    break;
                case "ANGLE":
                    visualStructure[y - 1][x - 1] = 'А';
                    break;
                default:
                    break;
            }
            visualStructure[y - 1][x] = ' ';
            visualStructure[y - 1][x + 1] = Integer.toString(card.getPoints()).charAt(0);
        } else if ((card instanceof ResourceCard || card instanceof GoldCard) && card.getPoints() > 0) {
            visualStructure[y - 1][x - 1] = ' ';
            visualStructure[y - 1][x] = Integer.toString(card.getPoints()).charAt(0);
            visualStructure[y - 1][x + 1] = ' ';
        } else if (card instanceof InitialCard) {
            if (card.getFrontCenterResources().size() == 2 && frontUp) {
                visualStructure[y - 1][x - 1] = ' ';
                visualStructure[y - 1][x] = card.getFrontCenterResources().get(1).charAt(0);
                visualStructure[y - 1][x + 1] = ' ';
            } else if (card.getFrontCenterResources().size() == 3 && frontUp) {
                visualStructure[y - 1][x - 1] = card.getFrontCenterResources().get(1).charAt(0);
                visualStructure[y - 1][x] = ' ';
                visualStructure[y - 1][x + 1] = card.getFrontCenterResources().get(2).charAt(0);
            }
        } else {
            visualStructure[y - 1][x - 1] = ' ';
            visualStructure[y - 1][x] = ' ';
            visualStructure[y - 1][x + 1] = ' ';
        }

        visualStructure[y - 1][x + 2] = '│';
        if (corners.get(1).equals("INK") || corners.get(1).equals("SCROLL") || corners.get(1).equals("FEATHER"))
            visualStructure[y - 1][x + 3] = Character.toLowerCase(corners.get(1).charAt(0));
        else
            visualStructure[y - 1][x + 3] = corners.get(1).charAt(0);
        visualStructure[y - 1][x + 4] = '│';
    }

    private void drawIdLine(Card card, Boolean frontUp, int x, int y) throws IllegalCommandException {
        visualStructure[y][x - 4] = '├';
        visualStructure[y][x - 3] = '─';
        visualStructure[y][x - 2] = '┤';
        if (card instanceof InitialCard)
            visualStructure[y][x - 1] = 'І';
        else
            visualStructure[y][x - 1] = card.getIdCard().charAt(0);
        visualStructure[y][x] = card.getIdCard().charAt(1);
        visualStructure[y][x + 1] = card.getIdCard().charAt(2);
        visualStructure[y][x + 2] = '├';
        visualStructure[y][x + 3] = '─';
        visualStructure[y][x + 4] = '┤';
    }

    private void drawBottomCornersLine(Card card, Boolean frontUp, List<String> corners, int x, int y)
            throws IllegalCommandException {
        visualStructure[y + 1][x - 4] = '│';
        if (corners.get(2).equals("INK") || corners.get(2).equals("SCROLL") || corners.get(2).equals("FEATHER"))
            visualStructure[y + 1][x - 3] = Character.toLowerCase(corners.get(2).charAt(0));
        else
            visualStructure[y + 1][x - 3] = corners.get(2).charAt(0);
        visualStructure[y + 1][x - 2] = '│';

        if (card instanceof InitialCard && frontUp) {
            visualStructure[y + 1][x - 1] = ' ';
            visualStructure[y + 1][x] = card.getFrontCenterResources().get(0).charAt(0);
            visualStructure[y + 1][x + 1] = ' ';
        } else if (!frontUp && (card instanceof ResourceCard || card instanceof GoldCard)) {
            visualStructure[y + 1][x - 1] = ' ';
            visualStructure[y + 1][x] = card.getSymbol().charAt(0);
            visualStructure[y + 1][x + 1] = ' ';
        } else {
            visualStructure[y + 1][x - 1] = ' ';
            visualStructure[y + 1][x] = ' ';
            visualStructure[y + 1][x + 1] = ' ';
        }

        visualStructure[y + 1][x + 2] = '│';
        if (corners.get(3).equals("INK") || corners.get(3).equals("SCROLL") || corners.get(3).equals("FEATHER"))
            visualStructure[y + 1][x + 3] = Character.toLowerCase(corners.get(3).charAt(0));
        else
            visualStructure[y + 1][x + 3] = corners.get(3).charAt(0);
        visualStructure[y + 1][x + 4] = '│';
    }

    private void drawBottomLine(int x, int y) {
        visualStructure[y + 2][x - 4] = '╰';
        visualStructure[y + 2][x - 3] = '─';
        visualStructure[y + 2][x - 2] = '┴';
        visualStructure[y + 2][x - 1] = '─';
        visualStructure[y + 2][x] = '─';
        visualStructure[y + 2][x + 1] = '─';
        visualStructure[y + 2][x + 2] = '┴';
        visualStructure[y + 2][x + 3] = '─';
        visualStructure[y + 2][x + 4] = '╯';
    }

    // structure is a matrix of 249x85
    public void printVisual() throws IllegalCommandException {
        for (int i = minYVisual - 2; i <= maxYVisual + 2; i++) {
            for (int j = minXVisual - 6; j <= maxXVisual + 6; j++) {
                if (visualStructure[i][j] == 0)
                    System.out.print(" ");
                else
                    switch (visualStructure[i][j]) {
                        case 'A':
                            System.out.print(ANSI_BLUE + visualStructure[i][j] + ANSI_RESET);
                            break;
                        case 'V':
                            System.out.print(ANSI_GREEN + visualStructure[i][j] + ANSI_RESET);
                            break;
                        case 'I', 'І':
                            if (Character.isDigit(visualStructure[i][j + 1])) {
                                System.out.print(ANSI_BG_YELLOW + visualStructure[i][j]);
                                System.out.print(visualStructure[i][j + 1]);
                                System.out.print(visualStructure[i][j + 2] + ANSI_RESET);
                                j += 2;
                            } else if (visualStructure[i][j] == 'I')
                                System.out.print(ANSI_PURPLE + visualStructure[i][j] + ANSI_RESET);
                            else {
                                System.out.print(visualStructure[i][j]);
                            }
                            break;
                        case 'R', 'G':
                            // Calculates the coordinates given the index of the matrix -> inverting the
                            // mathematical formula except for the x axis in which you take index of the
                            // center of the ID -> hence j - 252
                            switch (coordinateToCard.get((((j - 252) / 6 + 40) * 100) + (((87 - i) / 2) + 40)).getKey()
                                    .getSymbol()) {
                                case "SHROOM":
                                    System.out.print(ANSI_BG_RED + visualStructure[i][j]);
                                    break;
                                case "ANIMAL":
                                    System.out.print(ANSI_BG_BLUE + visualStructure[i][j]);
                                    break;
                                case "VEGETABLE":
                                    System.out.print(ANSI_BG_GREEN + visualStructure[i][j]);
                                    break;
                                case "INSECT":
                                    System.out.print(ANSI_BG_PURPLE + visualStructure[i][j]);
                                    break;
                                default:
                                    break;
                            }
                            System.out.print(visualStructure[i][j + 1]);
                            System.out.print(visualStructure[i][j + 2] + ANSI_RESET);
                            j += 2;
                            break;
                        case 'S':
                            System.out.print(ANSI_RED + visualStructure[i][j] + ANSI_RESET);
                            break;
                        case 's':
                            System.out.print(ANSI_YELLOW + Character.toUpperCase(visualStructure[i][j]) + ANSI_RESET);
                            break;
                        case 'i':
                            System.out.print(ANSI_YELLOW + Character.toUpperCase(visualStructure[i][j]) + ANSI_RESET);
                            break;
                        case 'f':
                            System.out.print(ANSI_YELLOW + Character.toUpperCase(visualStructure[i][j]) + ANSI_RESET);
                            break;
                        case 'N':
                            System.out.print(ANSI_BG_BRIGHT_RED + 'X' + ANSI_RESET);
                            break;
                        case 'E':
                            System.out.print(ANSI_DARK_GRAY + '░' + ANSI_RESET);
                            break;
                        default:
                            System.out.print(visualStructure[i][j]);
                            break;
                    }
            }
            System.out.println();
        }
    }
}