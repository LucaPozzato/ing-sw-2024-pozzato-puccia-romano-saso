package it.polimi.ingsw.codexnaturalis.model.game.components.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Objects;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Resource;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Printer;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;

public class Structure {
    // TODO: delete regionmatches -> code hard to read
    // TODO: add message to IllegalCommandExceptions
    private List<Card> timeStamp;
    private Map<Card, Triplet<Integer, Boolean, Boolean>> cardToCoordinate; // <Card , <Coordinates, Side, Visited> >
    private Map<Integer, Triplet<Card, Boolean, Boolean>> coordinateToCard; // <Coordinates , <Card, Side, Visited> >
    private Map<String, Integer> visibleSymbols;
    private char[][] visualStructure = new char[174][506];
    private String[][] skeletonStructure = new String[80][80];

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
        Integer destinationCoord = calcCoordinate(father, position);
        // Checks if the card can be placed and places it
        isPlaceable(father, card, destinationCoord, position, frontUp);
        if (card instanceof InitialCard) {
            cardToCoordinate.put(card, new Triplet<>(4040, frontUp, false));
            coordinateToCard.put(4040, new Triplet<>(card, frontUp, false));
        } else {
            cardToCoordinate.put(card, new Triplet<>(destinationCoord, frontUp, false));
            coordinateToCard.put(destinationCoord, new Triplet<>(card, frontUp, false));

        }
        timeStamp.add(card);
        addCardToVisual(card, frontUp);
        addCardToSkeleton(card);
        calcVisibleSymbols();
    }

    private void calcVisibleSymbols() {
        int minX = coordinateToCard.keySet().stream().mapToInt(Integer::intValue).map(i -> 253 + (i / 100 - 40) * 6)
                .min().getAsInt();
        int minY = coordinateToCard.keySet().stream().mapToInt(Integer::intValue).map(i -> 87 - (i % 100 - 40) * 2)
                .min().getAsInt();
        int maxX = coordinateToCard.keySet().stream().mapToInt(Integer::intValue).map(i -> 253 + (i / 100 - 40) * 6)
                .max().getAsInt();
        int maxY = coordinateToCard.keySet().stream().mapToInt(Integer::intValue).map(i -> 87 - (i % 100 - 40) * 2)
                .max().getAsInt();

        // Resets visible symbols
        for (String symbol : visibleSymbols.keySet()) {
            visibleSymbols.put(symbol, 0);
        }

        // Updates visible symbols
        for (int i = minY - 2; i <= maxY + 2; i++) {
            for (int j = minX - 4; j <= maxX + 4; j++) {
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

    public String getVisibleResources() {
        String visibleResources = "";
        for (Resource resource : Resource.values()) {
            if (!(resource.name().equals("EMPTY")) && !(resource.name().equals("NULL")))
                visibleResources += resource.name() + ": " + visibleSymbols.get(resource.name()) + "\n";
        }
        return visibleResources;
    }

    public String getVisibleObjects() {
        String visibleObjects = "";
        for (Objects resource : Objects.values()) {
            visibleObjects += resource.name() + ": " + visibleSymbols.get(resource.name()) + "\n";
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
    private Boolean isPlaceable(Card father, Card card, Integer coordinate, String position, Boolean frontUp)
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

        // Checks if top left card exists and does not have null in BR corner
        if (coordinateToCard.containsKey(coordinate - 99)) {
            if (coordinateToCard.get(coordinate - 99).getSide()) {
                if (coordinateToCard.get(coordinate - 99).getFirst().getFrontCorners().get(3).equals("NULL"))
                    throw new IllegalCommandException("Card cannot be placed on null corner");
            } else if (coordinateToCard.get(coordinate - 99).getFirst().getBackCorners().get(3).equals("NULL"))
                throw new IllegalCommandException("Card cannot be placed on null corner");
        }
        // Checks if top right card exists and does not have null in BL corner
        if (coordinateToCard.containsKey(coordinate + 101)) {
            if (coordinateToCard.get(coordinate + 101).getSide()) {
                if (coordinateToCard.get(coordinate + 101).getFirst().getFrontCorners().get(2).equals("NULL"))
                    throw new IllegalCommandException("Card cannot be placed on null corner");
            } else if (coordinateToCard.get(coordinate + 101).getFirst().getBackCorners().get(2).equals("NULL"))
                throw new IllegalCommandException("Card cannot be placed on null corner");
        }
        // Checks if bottom left card exists and does not have null in TR corner
        if (coordinateToCard.containsKey(coordinate - 101)) {
            if (coordinateToCard.get(coordinate - 101).getSide()) {
                if (coordinateToCard.get(coordinate - 101).getFirst().getFrontCorners().get(1).equals("NULL"))
                    throw new IllegalCommandException("Card cannot be placed on null corner");
            } else if (coordinateToCard.get(coordinate - 101).getFirst().getBackCorners().get(1).equals("NULL"))
                throw new IllegalCommandException("Card cannot be placed on null corner");
        }
        // Checks if bottom right card exists and does not have null in TL corner
        if (coordinateToCard.containsKey(coordinate + 99)) {
            if (coordinateToCard.get(coordinate + 99).getSide()) {
                if (coordinateToCard.get(coordinate + 99).getFirst().getFrontCorners().get(0).equals("NULL"))
                    throw new IllegalCommandException("Card cannot be placed on null corner");
            } else if (coordinateToCard.get(coordinate + 99).getFirst().getBackCorners().get(0).equals("NULL"))
                throw new IllegalCommandException("Card cannot be placed on null corner");
        }

        // Checks if the gold card has the requirements to be placed
        if (card instanceof GoldCard && frontUp) {
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

        Integer fatherCoordinate = cardToCoordinate.get(father).getFirst();

        // Important: these coordinates are correct in the Euclidean plane, but not when
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

    public int getPointsFromPatterns(List<Card> patternList, Card placed) throws IllegalCommandException {
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

    private boolean searchStairPattern(Card specificPattern, Card placed) throws IllegalCommandException {
        int placedCoordinate = cardToCoordinate.get(placed).getFirst();
        switch (specificPattern.getIdCard()) {
            case "OP1":
                if ((coordinateToCard.get(placedCoordinate - 99) != null
                        && coordinateToCard.get(placedCoordinate - 99).getFirst().getSymbol().equals("SHROOM")
                        && !coordinateToCard.get(placedCoordinate - 99).getVisited() &&
                        coordinateToCard.get(placedCoordinate - 198) != null
                        && coordinateToCard.get(placedCoordinate - 198).getFirst().getSymbol().equals("SHROOM")
                        && !coordinateToCard.get(placedCoordinate - 198).getVisited())) {
                    coordinateToCard.get(placedCoordinate).setVisited(true);
                    coordinateToCard.get(placedCoordinate - 99).setVisited(true);
                    coordinateToCard.get(placedCoordinate - 198).setVisited(true);
                    return true;
                } else if ((coordinateToCard.get(placedCoordinate - 99) != null
                        && coordinateToCard.get(placedCoordinate - 99).getFirst().getSymbol().equals("SHROOM")
                        && !coordinateToCard.get(placedCoordinate - 99).getVisited() &&
                        coordinateToCard.get(placedCoordinate + 99) != null
                        && coordinateToCard.get(placedCoordinate + 99).getFirst().getSymbol().equals("SHROOM")
                        && !coordinateToCard.get(placedCoordinate + 99).getVisited())) {
                    coordinateToCard.get(placedCoordinate).setVisited(true);
                    coordinateToCard.get(placedCoordinate - 99).setVisited(true);
                    coordinateToCard.get(placedCoordinate + 99).setVisited(true);
                    return true;
                } else if ((coordinateToCard.get(placedCoordinate + 99) != null
                        && coordinateToCard.get(placedCoordinate + 99).getFirst().getSymbol().equals("SHROOM")
                        && !coordinateToCard.get(placedCoordinate + 99).getVisited()) &&
                        coordinateToCard.get(placedCoordinate + 198) != null
                        && coordinateToCard.get(placedCoordinate + 198).getFirst().getSymbol().equals("SHROOM")
                        && !coordinateToCard.get(placedCoordinate + 198).getVisited()) {
                    coordinateToCard.get(placedCoordinate).setVisited(true);
                    coordinateToCard.get(placedCoordinate + 99).setVisited(true);
                    coordinateToCard.get(placedCoordinate + 198).setVisited(true);
                    return true;
                } else
                    return false;
            case "OP2":
                if ((coordinateToCard.get(placedCoordinate - 101) != null
                        && coordinateToCard.get(placedCoordinate - 101).getFirst().getSymbol().equals("VEGETABLE")
                        && !coordinateToCard.get(placedCoordinate - 101).getVisited()) &&
                        coordinateToCard.get(placedCoordinate - 202) != null
                        && coordinateToCard.get(placedCoordinate - 202).getFirst().getSymbol().equals("VEGETABLE")
                        && !coordinateToCard.get(placedCoordinate - 202).getVisited()) {
                    coordinateToCard.get(placedCoordinate).setVisited(true);
                    coordinateToCard.get(placedCoordinate - 101).setVisited(true);
                    coordinateToCard.get(placedCoordinate - 202).setVisited(true);
                    return true;
                } else if ((coordinateToCard.get(placedCoordinate - 101) != null
                        && coordinateToCard.get(placedCoordinate - 101).getFirst().getSymbol().equals("VEGETABLE")
                        && !coordinateToCard.get(placedCoordinate - 101).getVisited()) &&
                        coordinateToCard.get(placedCoordinate + 101) != null
                        && coordinateToCard.get(placedCoordinate + 101).getFirst().getSymbol().equals("VEGETABLE")
                        && !coordinateToCard.get(placedCoordinate + 101).getVisited()) {
                    coordinateToCard.get(placedCoordinate).setVisited(true);
                    coordinateToCard.get(placedCoordinate - 101).setVisited(true);
                    coordinateToCard.get(placedCoordinate + 101).setVisited(true);
                    return true;
                } else if ((coordinateToCard.get(placedCoordinate + 101) != null
                        && coordinateToCard.get(placedCoordinate + 101).getFirst().getSymbol().equals("VEGETABLE")
                        && !coordinateToCard.get(placedCoordinate + 101).getVisited()) &&
                        coordinateToCard.get(placedCoordinate + 201) != null
                        && coordinateToCard.get(placedCoordinate + 201).getFirst().getSymbol().equals("VEGETABLE")
                        && !coordinateToCard.get(placedCoordinate + 201).getVisited()) {
                    coordinateToCard.get(placedCoordinate).setVisited(true);
                    coordinateToCard.get(placedCoordinate + 101).setVisited(true);
                    coordinateToCard.get(placedCoordinate + 201).setVisited(true);
                    return true;
                } else
                    return false;

            case "OP3":
                if ((coordinateToCard.get(placedCoordinate - 99) != null
                        && coordinateToCard.get(placedCoordinate - 99).getFirst().getSymbol().equals("ANIMAL")
                        && !coordinateToCard.get(placedCoordinate - 99).getVisited()) &&
                        coordinateToCard.get(placedCoordinate - 198) != null
                        && coordinateToCard.get(placedCoordinate - 198).getFirst().getSymbol().equals("ANIMAL")
                        && !coordinateToCard.get(placedCoordinate - 198).getVisited()) {
                    coordinateToCard.get(placedCoordinate).setVisited(true);
                    coordinateToCard.get(placedCoordinate - 99).setVisited(true);
                    coordinateToCard.get(placedCoordinate - 198).setVisited(true);
                    return true;
                } else if ((coordinateToCard.get(placedCoordinate - 99) != null
                        && coordinateToCard.get(placedCoordinate - 99).getFirst().getSymbol().equals("ANIMAL")
                        && !coordinateToCard.get(placedCoordinate - 99).getVisited()) &&
                        coordinateToCard.get(placedCoordinate + 99) != null
                        && coordinateToCard.get(placedCoordinate + 99).getFirst().getSymbol().equals("ANIMAL")
                        && !coordinateToCard.get(placedCoordinate + 99).getVisited()) {
                    coordinateToCard.get(placedCoordinate).setVisited(true);
                    coordinateToCard.get(placedCoordinate - 99).setVisited(true);
                    coordinateToCard.get(placedCoordinate + 99).setVisited(true);
                    return true;
                } else if ((coordinateToCard.get(placedCoordinate + 99) != null
                        && coordinateToCard.get(placedCoordinate + 99).getFirst().getSymbol().equals("ANIMAL")
                        && !coordinateToCard.get(placedCoordinate + 99).getVisited()) &&
                        coordinateToCard.get(placedCoordinate + 198) != null
                        && coordinateToCard.get(placedCoordinate + 198).getFirst().getSymbol().equals("ANIMAL")
                        && !coordinateToCard.get(placedCoordinate + 198).getVisited()) {
                    coordinateToCard.get(placedCoordinate).setVisited(true);
                    coordinateToCard.get(placedCoordinate + 99).setVisited(true);
                    coordinateToCard.get(placedCoordinate + 198).setVisited(true);
                    return true;
                } else
                    return false;

            case "OP4":
                if ((coordinateToCard.get(placedCoordinate - 101) != null
                        && coordinateToCard.get(placedCoordinate - 101).getFirst().getSymbol().equals("INSECT")
                        && !coordinateToCard.get(placedCoordinate - 101).getVisited()) &&
                        coordinateToCard.get(placedCoordinate - 202) != null
                        && coordinateToCard.get(placedCoordinate - 202).getFirst().getSymbol().equals("INSECT")
                        && !coordinateToCard.get(placedCoordinate - 202).getVisited()) {
                    coordinateToCard.get(placedCoordinate).setVisited(true);
                    coordinateToCard.get(placedCoordinate - 101).setVisited(true);
                    coordinateToCard.get(placedCoordinate - 202).setVisited(true);
                    return true;
                } else if ((coordinateToCard.get(placedCoordinate - 101) != null
                        && coordinateToCard.get(placedCoordinate - 101).getFirst().getSymbol().equals("INSECT")
                        && !coordinateToCard.get(placedCoordinate - 101).getVisited()) &&
                        coordinateToCard.get(placedCoordinate + 101) != null
                        && coordinateToCard.get(placedCoordinate + 101).getFirst().getSymbol().equals("INSECT")
                        && !coordinateToCard.get(placedCoordinate + 101).getVisited()) {
                    coordinateToCard.get(placedCoordinate).setVisited(true);
                    coordinateToCard.get(placedCoordinate - 101).setVisited(true);
                    coordinateToCard.get(placedCoordinate + 101).setVisited(true);
                    return true;
                } else if ((coordinateToCard.get(placedCoordinate + 101) != null
                        && coordinateToCard.get(placedCoordinate + 101).getFirst().getSymbol().equals("INSECT")
                        && !coordinateToCard.get(placedCoordinate + 101).getVisited()) &&
                        coordinateToCard.get(placedCoordinate + 201) != null
                        && coordinateToCard.get(placedCoordinate + 201).getFirst().getSymbol().equals("INSECT")
                        && !coordinateToCard.get(placedCoordinate + 201).getVisited()) {
                    coordinateToCard.get(placedCoordinate).setVisited(true);
                    coordinateToCard.get(placedCoordinate + 101).setVisited(true);
                    coordinateToCard.get(placedCoordinate + 201).setVisited(true);
                    return true;
                } else
                    return false;

            default:
                throw new IllegalCommandException("Illegal pattern recognized");
        }
    }

    private Boolean searchChairPattern(Card specificPattern, Card placed) throws IllegalCommandException {
        int placedCoordinate = cardToCoordinate.get(placed).getFirst();
        // BUG: fox chair index, top card is 2 indexes away from the bottom card
        switch (specificPattern.getIdCard()) {
            case "OP5":
                if (placed.getSymbol().equals("VEGETABLES")) {
                    if (coordinateToCard.get(placedCoordinate - 101) != null
                            && coordinateToCard.get(placedCoordinate - 101).getFirst().getSymbol().equals("SHROOM")
                            && !coordinateToCard.get(placedCoordinate - 101).getVisited() &&
                            coordinateToCard.get(placedCoordinate - 201) != null
                            && coordinateToCard.get(placedCoordinate - 201).getFirst().getSymbol().equals("SHROOM")
                            && !coordinateToCard.get(placedCoordinate - 201).getVisited()) {
                        coordinateToCard.get(placedCoordinate).setVisited(true);
                        coordinateToCard.get(placedCoordinate - 101).setVisited(true);
                        coordinateToCard.get(placedCoordinate - 201).setVisited(true);
                        return true;
                    } else
                        return false;
                }
                if (placed.getSymbol().equals("SHROOM")) {
                    if (coordinateToCard.get(placedCoordinate + 100) != null
                            && coordinateToCard.get(placedCoordinate + 100).getFirst().getSymbol().equals("SHROOM")
                            && !coordinateToCard.get(placedCoordinate + 100).getVisited() &&
                            coordinateToCard.get(placedCoordinate + 201) != null
                            && coordinateToCard.get(placedCoordinate + 201).getFirst().getSymbol().equals("VEGETABLE")
                            && !coordinateToCard.get(placedCoordinate + 201).getVisited()) {
                        coordinateToCard.get(placedCoordinate).setVisited(true);
                        coordinateToCard.get(placedCoordinate + 100).setVisited(true);
                        coordinateToCard.get(placedCoordinate + 201).setVisited(true);
                        return true;
                    } else if (coordinateToCard.get(placedCoordinate - 100) != null
                            && coordinateToCard.get(placedCoordinate - 100).getFirst().getSymbol().equals("SHROOM")
                                    & !coordinateToCard.get(placedCoordinate - 100).getVisited()
                            &&
                            coordinateToCard.get(placedCoordinate + 101) != null
                            && coordinateToCard.get(placedCoordinate + 101).getFirst().getSymbol().equals("VEGETABLE")
                            && !coordinateToCard.get(placedCoordinate + 101).getVisited()) {
                        coordinateToCard.get(placedCoordinate).setVisited(true);
                        coordinateToCard.get(placedCoordinate - 100).setVisited(true);
                        coordinateToCard.get(placedCoordinate + 101).setVisited(true);
                        return true;
                    } else
                        return false;
                }

            case "OP6":
                if (placed.getSymbol().equals("INSECT")) {
                    if (coordinateToCard.get(placedCoordinate - 99) != null
                            && coordinateToCard.get(placedCoordinate - 99).getFirst().getSymbol().equals("VEGETABLE")
                            && !coordinateToCard.get(placedCoordinate - 99).getVisited() &&
                            coordinateToCard.get(placedCoordinate - 199) != null
                            && coordinateToCard.get(placedCoordinate - 199).getFirst().getSymbol().equals("VEGETABLE")
                            && !coordinateToCard.get(placedCoordinate - 199).getVisited()) {
                        coordinateToCard.get(placedCoordinate).setVisited(true);
                        coordinateToCard.get(placedCoordinate - 99).setVisited(true);
                        coordinateToCard.get(placedCoordinate - 199).setVisited(true);
                        return true;
                    } else
                        return false;
                }
                if (placed.getSymbol().equals("VEGETABLE")) {
                    if (coordinateToCard.get(placedCoordinate + 100) != null
                            && coordinateToCard.get(placedCoordinate + 100).getFirst().getSymbol().equals("VEGETABLE")
                            && !coordinateToCard.get(placedCoordinate + 100).getVisited() &&
                            coordinateToCard.get(placedCoordinate + 199) != null
                            && coordinateToCard.get(placedCoordinate + 199).getFirst().getSymbol().equals("INSECT")
                            && !coordinateToCard.get(placedCoordinate + 199).getVisited()) {
                        coordinateToCard.get(placedCoordinate).setVisited(true);
                        coordinateToCard.get(placedCoordinate + 100).setVisited(true);
                        coordinateToCard.get(placedCoordinate + 199).setVisited(true);
                        return true;
                    } else if (coordinateToCard.get(placedCoordinate - 100) != null
                            && coordinateToCard.get(placedCoordinate - 100).getFirst().getSymbol().equals("SHROOM")
                            && !coordinateToCard.get(placedCoordinate - 100).getVisited() &&
                            coordinateToCard.get(placedCoordinate + 99) != null
                            && coordinateToCard.get(placedCoordinate + 99).getFirst().getSymbol().equals("VEGETABLE")
                            && !coordinateToCard.get(placedCoordinate + 99).getVisited()) {
                        coordinateToCard.get(placedCoordinate).setVisited(true);
                        coordinateToCard.get(placedCoordinate - 100).setVisited(true);
                        coordinateToCard.get(placedCoordinate + 99).setVisited(true);
                        return true;
                    } else
                        return false;
                }
            case "OP7":
                if (placed.getSymbol().equals("SHROOM")) {
                    if (coordinateToCard.get(placedCoordinate + 99) != null
                            && coordinateToCard.get(placedCoordinate + 99).getFirst().getSymbol().equals("ANIMAL")
                            && !coordinateToCard.get(placedCoordinate + 99).getVisited() &&
                            coordinateToCard.get(placedCoordinate + 199) != null
                            && coordinateToCard.get(placedCoordinate + 199).getFirst().getSymbol().equals("ANIMAL")
                            && !coordinateToCard.get(placedCoordinate + 199).getVisited()) {
                        coordinateToCard.get(placedCoordinate).setVisited(true);
                        coordinateToCard.get(placedCoordinate + 99).setVisited(true);
                        coordinateToCard.get(placedCoordinate + 199).setVisited(true);
                        return true;
                    } else
                        return false;
                }
                if (placed.getSymbol().equals("ANIMAL")) {
                    if (coordinateToCard.get(placedCoordinate - 99) != null
                            && coordinateToCard.get(placedCoordinate - 99).getFirst().getSymbol().equals("SHROOM")
                            && !coordinateToCard.get(placedCoordinate - 99).getVisited() &&
                            coordinateToCard.get(placedCoordinate + 100) != null
                            && coordinateToCard.get(placedCoordinate + 100).getFirst().getSymbol().equals("ANIMAL")
                            && !coordinateToCard.get(placedCoordinate + 100).getVisited()) {
                        coordinateToCard.get(placedCoordinate).setVisited(true);
                        coordinateToCard.get(placedCoordinate - 99).setVisited(true);
                        coordinateToCard.get(placedCoordinate + 100).setVisited(true);
                        return true;
                    } else if (coordinateToCard.get(placedCoordinate - 100) != null
                            && coordinateToCard.get(placedCoordinate - 100).getFirst().getSymbol().equals("ANIMAL")
                            && !coordinateToCard.get(placedCoordinate - 100).getVisited() &&
                            coordinateToCard.get(placedCoordinate - 199) != null
                            && coordinateToCard.get(placedCoordinate - 199).getFirst().getSymbol().equals("SHROOM")
                            && !coordinateToCard.get(placedCoordinate - 199).getVisited()) {
                        coordinateToCard.get(placedCoordinate).setVisited(true);
                        coordinateToCard.get(placedCoordinate - 100).setVisited(true);
                        coordinateToCard.get(placedCoordinate - 199).setVisited(true);
                        return true;
                    } else
                        return false;
                }
            case "OP8":
                if (placed.getSymbol().equals("ANIMAL")) {
                    if (coordinateToCard.get(placedCoordinate + 101) != null
                            && coordinateToCard.get(placedCoordinate + 101).getFirst().getSymbol().equals("INSECT")
                            && !coordinateToCard.get(placedCoordinate + 101).getVisited() &&
                            coordinateToCard.get(placedCoordinate + 201) != null
                            && coordinateToCard.get(placedCoordinate + 201).getFirst().getSymbol().equals("INSECT")
                            && !coordinateToCard.get(placedCoordinate + 201).getVisited()) {
                        coordinateToCard.get(placedCoordinate).setVisited(true);
                        coordinateToCard.get(placedCoordinate + 101).setVisited(true);
                        coordinateToCard.get(placedCoordinate + 201).setVisited(true);
                        return true;
                    } else
                        return false;
                }
                if (placed.getSymbol().equals("INSECT")) {
                    if (coordinateToCard.get(placedCoordinate - 101) != null
                            && coordinateToCard.get(placedCoordinate - 101).getFirst().getSymbol().equals("ANIMAL")
                            && !coordinateToCard.get(placedCoordinate - 101).getVisited() &&
                            coordinateToCard.get(placedCoordinate + 100) != null
                            && coordinateToCard.get(placedCoordinate + 100).getFirst().getSymbol().equals("INSECT")
                            && !coordinateToCard.get(placedCoordinate + 100).getVisited()) {
                        coordinateToCard.get(placedCoordinate).setVisited(true);
                        coordinateToCard.get(placedCoordinate - 101).setVisited(true);
                        coordinateToCard.get(placedCoordinate + 100).setVisited(true);
                        return true;
                    } else if (coordinateToCard.get(placedCoordinate - 100) != null
                            && coordinateToCard.get(placedCoordinate - 100).getFirst().getSymbol().equals("INSECT")
                            && !coordinateToCard.get(placedCoordinate - 100).getVisited() &&
                            coordinateToCard.get(placedCoordinate - 201) != null
                            && coordinateToCard.get(placedCoordinate - 201).getFirst().getSymbol().equals("ANIMAL")
                            && !coordinateToCard.get(placedCoordinate - 201).getVisited()) {
                        coordinateToCard.get(placedCoordinate).setVisited(true);
                        coordinateToCard.get(placedCoordinate - 100).setVisited(true);
                        coordinateToCard.get(placedCoordinate - 201).setVisited(true);
                        return true;
                    } else
                        return false;
                }
            default:
                throw new IllegalCommandException("Illegal pattern recognized");
        }
    }

    public int getPointsFromCard(Card placed) throws IllegalCommandException {
        if (placed.getPoints() > 0) {
            if (placed.getIdCard().regionMatches(0, "R", 0, 1)) {
                return 1;
                // TODO: GoldCard also has points from objetcs -> INK, SCROLL, FEATHER
            } else if (placed.getIdCard().regionMatches(0, "G", 0, 1)) {
                if (placed.getPointsType().regionMatches(0, "NULL", 0, 3)) {
                    return placed.getPoints();
                } else if (!placed.getPointsType().equals("ANGLE")) {
                    return visibleSymbols.get(placed.getPointsType());
                } else if (placed.getPointsType().equals("ANGLE")) {
                    int coveredCorners = 0;
                    int placedCoord = cardToCoordinate.get(placed).getFirst();
                    if (coordinateToCard.get(placedCoord + 99) != null)
                        coveredCorners++;
                    if (coordinateToCard.get(placedCoord - 99) != null)
                        coveredCorners++;
                    if (coordinateToCard.get(placedCoord + 101) != null)
                        coveredCorners++;
                    if (coordinateToCard.get(placedCoord - 101) != null)
                        coveredCorners++;
                    // BUG: points should be the number of corners * 2
                    return coveredCorners;
                }
            } else
                throw new IllegalCommandException("Passed neither gold nor resource card");
            return 0;
        } else
            return 0;
    }

    private void addCardToSkeleton(Card card) {
        int x = cardToCoordinate.get(card).getFirst() / 100;
        // Inverting y coordinate to match the matrix structure
        int y = 40 - (cardToCoordinate.get(card).getFirst() % 100 - 40);
        skeletonStructure[y][x] = card.getIdCard();
    }

    public void printSkeleton() {
        int minX = coordinateToCard.keySet().stream().mapToInt(Integer::intValue).map(i -> i / 100).min().getAsInt();
        int minY = coordinateToCard.keySet().stream().mapToInt(Integer::intValue).map(i -> 40 - (i % 100 - 40)).min()
                .getAsInt();
        int maxX = coordinateToCard.keySet().stream().mapToInt(Integer::intValue).map(i -> i / 100).max().getAsInt();
        int maxY = coordinateToCard.keySet().stream().mapToInt(Integer::intValue).map(i -> 40 - (i % 100 - 40)).max()
                .getAsInt();

        System.out.println();
        for (int i = minY; i <= maxY; i++) {
            for (int j = minX; j <= maxX; j++) {
                if (skeletonStructure[i][j] == null)
                    System.out.print("   ");
                else
                    System.out.print(skeletonStructure[i][j]);
            }
            System.out.print("\n");
        }
        System.out.println();
    }

    private void addCardToVisual(Card card, Boolean frontUp) throws IllegalCommandException {
        int x = cardToCoordinate.get(card).getFirst() / 100;
        int y = cardToCoordinate.get(card).getFirst() % 100;

        // Center of the matrix + x offset (6 pixels per card)
        x = 253 + ((x - 40) * 6);
        // Inverting y coordinate to match the matrix structure, center of the matrix +
        // y offset (2 pixels per card)
        y = 87 - ((y - 40) * 2);

        visualStructure = card.drawVisual(visualStructure, x, y, frontUp);
    }

    // structure is a matrix of 249x85
    public String draw() throws IllegalCommandException {
        return new Printer().printStructure(visualStructure, coordinateToCard);
    }
}