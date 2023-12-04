import java.io.FileWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

/**
 * The Main class represents the main program for a board game with insects.
 * It reads input from a file, initializes the game board, insects, and food points,
 * and then simulates the movement of insects based on their behavior.
 * The results are printed to an output file.
 */
public class Main {
    private static Board gameBoard;
    private static int d;
    private static int n;
    private static int m;
    private static List<Insect> insects;

    public static int getD() {
        return d;
    }

    /**
     * The main entry point for the program.
     *
     * @param args The command line arguments (not used in this program).
     */
    public static void main(String[] args) {
        try {
            // Input Section
            insects = new ArrayList<>();
            d = IOHandler.inputInt();
            final int mind = 4;
            final int maxd = 1000;
            if (d < mind || d > maxd) {
                throw new InvalidBoardException();
            }
            gameBoard = new Board(d);
            n = IOHandler.inputInt();
            final int minn = 1;
            final int maxn = 16;
            if (n < minn || n > maxn) {
                throw new InvalidNumberOfInsectsException();
            }
            m = IOHandler.inputInt();
            final int minm = 1;
            final int maxm = 200;
            if (m < minm || m > maxm) {
                throw new InvalidNumberOfFoodPointsException();
            }

            for (int i = 0; i < n; i++) {
                InsectColor color = InsectColor.toColour(IOHandler.inputString());
                String insectType = IOHandler.inputString();
                EntityPosition coordinates = IOHandler.inputCoordinates();
                for (Insect insect : insects) {
                    if (insect.color == color && insect.getInsectType().equals(insectType)) {
                        throw new DuplicateInsectException();
                    }
                }
                Insect insect;
                if (Objects.equals(insectType, "Butterfly")) {
                    insect = new Butterfly(coordinates, color);
                } else if (Objects.equals(insectType, "Ant")) {
                    insect = new Ant(coordinates, color);
                } else if (Objects.equals(insectType, "Spider")) {
                    insect = new Spider(coordinates, color);
                } else if (Objects.equals(insectType, "Grasshopper")) {
                    insect = new Grasshopper(coordinates, color);
                } else {
                    throw new InvalidInsectTypeException();
                }
                insect.setInsectType(insectType);
                insects.add(insect);
                gameBoard.addEntity(insect);
            }
            for (int i = 0; i < m; i++) {
                int foodAmount = IOHandler.inputInt();
                EntityPosition cords = IOHandler.inputCoordinates();
                gameBoard.addEntity(new FoodPoint(cords, foodAmount));
            }
            // End Input Section

            int index = 1;
            for (Insect insect : insects) {
                EntityPosition startPosition = insect.entityPosition;
                Direction dir = insect.getBestDirection(gameBoard.getBoardData(), d);
                int eaten = insect.travelDirection(dir, gameBoard.getBoardData(), d);
                IOHandler.print(insect.color.toString() + " " + insect.getInsectType() + ' ' + dir + ' ' + eaten);
                gameBoard.erase(startPosition);
                if (index != insects.size()) {
                    IOHandler.print("\n");
                }
                index++;
            }

        } catch (Exception e) {
            IOHandler.print(e.getMessage());
        }
        IOHandler.close();
    }

    /**
     * Deletes a food entity from the game board at the specified position.
     *
     * @param position The position of the food entity to be deleted.
     */
    public static void deleteFood(EntityPosition position) {
        gameBoard.erase(position);
    }
}

/**
 * The IOHandler class handles input and output operations for the program.
 * It reads input from a file and writes output to another file.
 */
class IOHandler {
    private static final Scanner INPUT;

    static {
        try {
            INPUT = new Scanner(new File("input.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static FileWriter fileWriter;

    static {
        try {
            fileWriter = new FileWriter("output.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static PrintWriter output = new PrintWriter(fileWriter);

    /**
     * Prints a string to the output file.
     *
     * @param string The string to be printed.
     */
    public static void print(String string) {
        output.print(string);
    }

    /**
     * Reads an integer from the input file.
     *
     * @return The integer read from the input file.
     */
    public static int inputInt() {
        return INPUT.nextInt();
    }

    /**
     * Reads coordinates (x, y) from the input file and returns an EntityPosition.
     *
     * @return The EntityPosition representing the coordinates.
     * @throws InvalidEntityPositionException If the coordinates are invalid.
     */
    public static EntityPosition inputCoordinates() throws InvalidEntityPositionException {
        int x = INPUT.nextInt();
        int y = INPUT.nextInt();
        if (x < 1 || x > Main.getD() || y < 1 || y > Main.getD()) {
            throw new InvalidEntityPositionException();
        }
        return new EntityPosition(x, y);
    }

    /**
     * Reads a string from the input file.
     *
     * @return The string read from the input file.
     */
    public static String inputString() {
        return INPUT.next();
    }
    public static void close() {
        output.close();
    }
}

/**
 * The EntityPosition class represents the position of an entity on the game board.
 */
class EntityPosition {
    private int x;
    private int y;

    EntityPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x-coordinate of the position.
     *
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the position.
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Adds another EntityPosition to this position and returns a new EntityPosition.
     *
     * @param other The other EntityPosition to be added.
     * @return The new EntityPosition after addition.
     */
    public EntityPosition sum(EntityPosition other) {
        int newX = x + other.getX();
        int newY = y + other.getY();
        return new EntityPosition(newX, newY);
    }

    @Override
    public String toString() {
        return Integer.toString(x) + ':' + y;
    }
}

/**
 * The FoodPoint class represents a food entity on the game board.
 */
class FoodPoint extends BoardEntity {
    protected int value;

    /**
     * Constructs a FoodPoint with the specified position and value.
     *
     * @param position The position of the food entity.
     * @param value    The value of the food entity.
     */
    public FoodPoint(EntityPosition position, int value) {
        this.setEntityPosition(position);
        this.value = value;
    }
}

/**
 * The BoardEntity class is an abstract class representing an entity on the game board.
 */
abstract class BoardEntity {
    protected EntityPosition entityPosition;

    /**
     * Sets the position of the entity on the game board.
     *
     * @param entityPosition The new position of the entity.
     */
    public void setEntityPosition(EntityPosition entityPosition) {
        this.entityPosition = entityPosition;
    }
}

/**
 * The InsectColor enum represents the possible colors of insects.
 */
enum    InsectColor {
    RED,
    GREEN,
    BLUE,
    YELLOW;

    /**
     * Converts a string to the corresponding InsectColor.
     *
     * @param s The string representation of the color.
     * @return The InsectColor.
     * @throws InvalidInsectColorException If the string is not a valid color.
     */
    public static InsectColor toColour(String s) throws InvalidInsectColorException {
        switch (s) {
            case "Red" -> {
                return RED;
            }
            case "Green" -> {
                return GREEN;
            }
            case "Blue" -> {
                return BLUE;
            }
            case "Yellow" -> {
                return YELLOW;
            }
            default -> {
                throw new InvalidInsectColorException();
            }
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case RED -> {
                return "Red";
            }
            case YELLOW -> {
                return "Yellow";
            }
            case GREEN -> {
                return "Green";
            }
            case BLUE -> {
                return "Blue";
            }
            default -> {
                return null;
            }
        }
    }
}

/**
 * The Direction enum represents the possible directions an insect can move.
 */
enum Direction {
    N("North"),
    E("East"),
    S("South"),
    W("West"),
    NE("North-East"),
    SE("South-East"),
    SW("South-West"),
    NW("North-West");

    private final String textRepresentation;

    Direction(String text) {
        textRepresentation = text;
    }

    /**
     * Gets the offset corresponding to the direction.
     *
     * @return The offset as an EntityPosition.
     */
    public EntityPosition getOffset() {
        switch (this.textRepresentation) {
            case "North" -> {
                return new EntityPosition(-1, 0);
            }
            case "East" -> {
                return new EntityPosition(0, 1);
            }
            case "South" -> {
                return new EntityPosition(1, 0);
            }
            case "West" -> {
                return new EntityPosition(0, -1);
            }
            case "North-East" -> {
                return new EntityPosition(-1, 1);
            }
            case "South-East" -> {
                return new EntityPosition(1, 1);
            }
            case "South-West" -> {
                return new EntityPosition(1, -1);
            }
            case "North-West" -> {
                return new EntityPosition(-1, -1);
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Calculates the next position based on the current position and the direction.
     *
     * @param curr The current position.
     * @return The next position after moving in the direction.
     */
    public EntityPosition nextPos(EntityPosition curr) {
        return curr.sum(this.getOffset());
    }

    /**
     * Gets a list of orthogonal directions (N, E, S, W).
     *
     * @return The list of orthogonal directions.
     */
    public static List<Direction> getOrthogonalDirections() {
        return Arrays.asList(
                N,
                E,
                S,
                W
        );
    }

    /**
     * Gets a list of diagonal directions (NE, SE, SW, NW).
     *
     * @return The list of diagonal directions.
     */
    public static List<Direction> getDiagonalDirections() {
        return Arrays.asList(
                NE,
                SE,
                SW,
                NW
        );
    }

    /**
     * Gets a list of all directions (N, E, S, W, NE, SE, SW, NW).
     *
     * @return The list of all directions.
     */
    public static List<Direction> getAllDirections() {
        return Arrays.asList(
                N,
                E,
                S,
                W,
                NE,
                SE,
                SW,
                NW
        );
    }

    @Override
    public String toString() {
        return textRepresentation;
    }
}

/**
 * An interface representing movement in orthogonal directions on a board.
 */
interface OrthogonalMoving {

    /**
     * Computes the sum of visible values in the orthogonal direction from the current position.
     *
     * @param dir            The direction of movement.
     * @param entityPosition The current position of the entity.
     * @param boardData      The board data containing entities.
     * @param boardSize      The size of the board.
     * @return The sum of visible values in the specified orthogonal direction.
     */
    public default int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                                          Map<String, BoardEntity> boardData, int boardSize) {
        Board temp = new Board(boardData, boardSize);
        int counter = 0;
        while (temp.onBoard(dir.nextPos(entityPosition))) {
            EntityPosition nextPosition = dir.nextPos(entityPosition);
            BoardEntity entity = temp.getEntity(nextPosition);
            if (entity instanceof FoodPoint) {
                counter += ((FoodPoint) entity).value;
            }
            entityPosition = dir.nextPos(entityPosition);
        }
        return counter;
    }

    /**
     * Moves the entity orthogonally in the specified direction, interacting with entities on the way.
     *
     * @param dir            The direction of movement.
     * @param entityPosition The current position of the entity.
     * @param insectColor    The color of the insect.
     * @param boardData      The board data containing entities.
     * @param boardSize      The size of the board.
     * @return The sum of values obtained during movement (e.g., consumed food).
     */
    public default int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor insectColor,
                                          Map<String, BoardEntity> boardData, int boardSize) {
        Board temp = new Board(boardData, boardSize);
        int eaten = 0;
        while (temp.onBoard(dir.nextPos(entityPosition))) {
            EntityPosition nextPosition = dir.nextPos(entityPosition);
            BoardEntity entity = temp.getEntity(nextPosition);
            if (entity instanceof FoodPoint) {
                eaten += ((FoodPoint) entity).value;
                Main.deleteFood(entity.entityPosition);
            } else if (entity instanceof Insect) {
                InsectColor otherColor = ((Insect) entity).color;
                if (insectColor != otherColor) {
                    break;
                }
            }
            entityPosition = dir.nextPos(entityPosition);
        }
        return eaten;
    };
}

/**
 * An interface representing movement in diagonal directions on a board.
 */
interface DiagonalMoving {

    /**
     * Computes the sum of visible values in the diagonal direction from the current position.
     *
     * @param dir            The direction of movement.
     * @param entityPosition The current position of the entity.
     * @param boardData      The board data containing entities.
     * @param boardSize      The size of the board.
     * @return The sum of visible values in the specified diagonal direction.
     */
    public default int getDiagonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                                        Map<String, BoardEntity> boardData, int boardSize) {
        Board temp = new Board(boardData, boardSize);
        int counter = 0;
        while (temp.onBoard(dir.nextPos(entityPosition))) {
            EntityPosition nextPosition = dir.nextPos(entityPosition);
            BoardEntity entity = temp.getEntity(nextPosition);
            if (entity instanceof FoodPoint) {
                counter += ((FoodPoint) entity).value;
            }
            entityPosition = dir.nextPos(entityPosition);
        }
        return counter;
    };

    /**
     * Moves the entity diagonally in the specified direction, interacting with entities on the way.
     *
     * @param dir            The direction of movement.
     * @param entityPosition The current position of the entity.
     * @param insectColor    The color of the insect.
     * @param boardData      The board data containing entities.
     * @param boardSize      The size of the board.
     * @return The sum of values obtained during movement (e.g., consumed food).
     */
    public default int travelDiagonally(Direction dir, EntityPosition entityPosition, InsectColor insectColor,
                                        Map<String, BoardEntity> boardData, int boardSize) {
        Board temp = new Board(boardData, boardSize);
        int eaten = 0;
        while (temp.onBoard(dir.nextPos(entityPosition))) {
            EntityPosition nextPosition = dir.nextPos(entityPosition);
            BoardEntity entity = temp.getEntity(nextPosition);
            if (entity instanceof FoodPoint) {
                eaten += ((FoodPoint) entity).value;
                Main.deleteFood(entity.entityPosition);
            } else if (entity instanceof Insect) {
                InsectColor otherColor = ((Insect) entity).color;
                if (insectColor != otherColor) {
                    break;
                }
            }
            entityPosition = dir.nextPos(entityPosition);
        }
        return eaten;
    };
}
/**
 * An abstract class representing an insect on the board.
 */
abstract class Insect extends BoardEntity {

    private String insectType;

    public String getInsectType() {
        return insectType;
    }

    public void setInsectType(String insectType) {
        this.insectType = insectType;
    }

    protected InsectColor color;

    /**
     * Constructs an insect with the given position and color.
     *
     * @param position The initial position of the insect.
     * @param color    The color of the insect.
     */
    public Insect(EntityPosition position, InsectColor color) {
        this.color = color;
        this.setEntityPosition(position);
    }

    /**
     * Abstract method to get the best direction for the insect to move based on board conditions.
     *
     * @param boardData The board data containing entities.
     * @param boardSize The size of the board.
     * @return The best direction for the insect to move.
     */
    public abstract Direction getBestDirection(Map<String, BoardEntity> boardData, int boardSize);

    /**
     * Abstract method to perform movement in a specific direction and interact with entities on the way.
     *
     * @param dir       The direction of movement.
     * @param boardData The board data containing entities.
     * @param boardSize The size of the board.
     * @return The sum of values obtained during movement (e.g., consumed food).
     */
    public abstract int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int boardSize);
}

/**
 * A class representing a butterfly on the board, capable of orthogonal movement.
 */
class Butterfly extends Insect implements OrthogonalMoving {

    /**
     * Constructs a butterfly with the given position and color.
     *
     * @param entityPosition The initial position of the butterfly.
     * @param color          The color of the butterfly.
     */
    public Butterfly(EntityPosition entityPosition, InsectColor color) {
        super(entityPosition, color);
    }

    @Override
    public Direction getBestDirection(Map<String, BoardEntity> boardData, int boardSize) {
        Direction bestDir = Direction.getOrthogonalDirections().get(0);
        int bestValue = this.getOrthogonalDirectionVisibleValue(bestDir, this.entityPosition, boardData, boardSize);
        for (Direction dir : Direction.getOrthogonalDirections()) {
            int value = this.getOrthogonalDirectionVisibleValue(dir, this.entityPosition, boardData, boardSize);
            if (value > bestValue) {
                bestValue = value;
                bestDir = dir;
            }
        }
        return bestDir;
    }

    @Override
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int boardSize) {
        return this.travelOrthogonally(dir, this.entityPosition, this.color, boardData, boardSize);
    }
}
/**
 * A class representing an ant on the board, capable of both orthogonal and diagonal movement.
 */
class Ant extends Insect implements OrthogonalMoving, DiagonalMoving {

    /**
     * Constructs an ant with the given position and color.
     *
     * @param entityPosition The initial position of the ant.
     * @param color          The color of the ant.
     */
    public Ant(EntityPosition entityPosition, InsectColor color) {
        super(entityPosition, color);
    }

    @Override
    public Direction getBestDirection(Map<String, BoardEntity> boardData, int boardSize) {
        Direction bestDir = Direction.getAllDirections().get(0);
        int bestValue = this.getDiagonalDirectionVisibleValue(bestDir, this.entityPosition, boardData, boardSize);
        for (Direction dir : Direction.getAllDirections()) {
            int value = this.getDiagonalDirectionVisibleValue(dir, this.entityPosition, boardData, boardSize);
            if (value > bestValue) {
                bestValue = value;
                bestDir = dir;
            }
        }
        return bestDir;
    }

    @Override
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int boardSize) {
        return this.travelOrthogonally(dir, this.entityPosition, this.color, boardData, boardSize);
    }
}

/**
 * A class representing a spider on the board, capable of diagonal movement.
 */
class Spider extends Insect implements DiagonalMoving {

    /**
     * Constructs a spider with the given position and color.
     *
     * @param entityPosition The initial position of the spider.
     * @param color          The color of the spider.
     */
    public Spider(EntityPosition entityPosition, InsectColor color) {
        super(entityPosition, color);
    }

    @Override
    public Direction getBestDirection(Map<String, BoardEntity> boardData, int boardSize) {
        Direction bestDir = Direction.getDiagonalDirections().get(0);
        int bestValue = this.getDiagonalDirectionVisibleValue(bestDir, this.entityPosition, boardData, boardSize);
        for (Direction dir : Direction.getDiagonalDirections()) {
            int value = this.getDiagonalDirectionVisibleValue(dir, this.entityPosition, boardData, boardSize);
            if (value > bestValue) {
                bestValue = value;
                bestDir = dir;
            }
        }
        return bestDir;
    }

    @Override
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int boardSize) {
        return this.travelDiagonally(dir, this.entityPosition, this.color, boardData, boardSize);
    }
}

/**
 * A class representing a grasshopper on the board, capable of orthogonal movement and jumping.
 */
class Grasshopper extends Insect {

    /**
     * Constructs a grasshopper with the given position and color.
     *
     * @param entityPosition The initial position of the grasshopper.
     * @param color          The color of the grasshopper.
     */
    public Grasshopper(EntityPosition entityPosition, InsectColor color) {
        super(entityPosition, color);
    }

    @Override
    public Direction getBestDirection(Map<String, BoardEntity> boardData, int boardSize) {
        Direction bestDir = Direction.getOrthogonalDirections().get(0);
        int bestValue = this.getDirectionVisibleValue(bestDir, this.entityPosition, boardData, boardSize);
        for (Direction dir : Direction.getOrthogonalDirections()) {
            int value = this.getDirectionVisibleValue(dir, this.entityPosition, boardData, boardSize);
            if (value > bestValue) {
                bestValue = value;
                bestDir = dir;
            }
        }
        return bestDir;
    }

    /**
     * Gets the visible value in a specific direction for the grasshopper, considering its jumping ability.
     *
     * @param dir            The direction to check.
     * @param entityPosition The current position of the grasshopper.
     * @param boardData      The board data containing entities.
     * @param boardSize      The size of the board.
     * @return The visible value in the specified direction.
     */
    public int getDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                        Map<String, BoardEntity> boardData, int boardSize) {
        Board temp = new Board(boardData, boardSize);
        int counter = 0;
        // Double motion
        while (temp.onBoard(dir.nextPos(dir.nextPos(entityPosition)))) {
            EntityPosition nextPosition = dir.nextPos(dir.nextPos(entityPosition));
            BoardEntity entity = temp.getEntity(nextPosition);
            if (entity instanceof FoodPoint) {
                counter += ((FoodPoint) entity).value;
            }
            entityPosition = nextPosition;
        }
        return counter;
    }

    @Override
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int boardSize) {
        Board temp = new Board(boardData, boardSize);
        int eaten = 0;
        while (temp.onBoard(dir.nextPos(dir.nextPos(entityPosition)))) {
            EntityPosition nextPosition = dir.nextPos(dir.nextPos(entityPosition));
            BoardEntity entity = temp.getEntity(nextPosition);
            if (entity instanceof FoodPoint) {
                eaten += ((FoodPoint) entity).value;
                Main.deleteFood(entity.entityPosition);
            } else if (entity instanceof Insect) {
                InsectColor otherColor = ((Insect) entity).color;
                if (this.color != otherColor) {
                    break;
                }
            }
            entityPosition = nextPosition;
        }
        return eaten;
    }
}

/**
 * A class representing the game board.
 */
class Board {

    private Map<String, BoardEntity> boardData;
    private int size;

    /**
     * Gets the board data.
     *
     * @return The board data.
     */
    public Map<String, BoardEntity> getBoardData() {
        return boardData;
    }

    /**
     * Constructs an empty board with the given size.
     *
     * @param boardSize The size of the board.
     */
    public Board(int boardSize) {
        size = boardSize;
        boardData = new HashMap<String, BoardEntity>();
    }

    /**
     * Constructs a board with the given data and size.
     *
     * @param boardData The initial board data.
     * @param boardSize The size of the board.
     */
    public Board(Map<String, BoardEntity> boardData, int boardSize) {
        this.boardData = boardData;
        this.size = boardSize;
    }

    /**
     * Checks if a position is within the bounds of the board.
     *
     * @param position The position to check.
     * @return True if the position is on the board, false otherwise.
     */
    public boolean onBoard(EntityPosition position) {
        return position.getX() <= size && position.getY() <= size && position.getX() >= 1 && position.getY() >= 1;
    }

    /**
     * Erases an entity from the board at the specified position.
     *
     * @param position The position of the entity to erase.
     */
    public void erase(EntityPosition position) {
        boardData.replace(position.toString(), null);
    }

    /**
     * Adds an entity to the board, checking for overlapping entities.
     *
     * @param entity The entity to add.
     * @throws TwoEntitiesOnSamePositionException if there are two entities in the same position.
     */
    public void addEntity(BoardEntity entity) throws TwoEntitiesOnSamePositionException {
        BoardEntity curr = this.getEntity(entity.entityPosition);
        if (curr == null) {
            boardData.put(entity.entityPosition.toString(), entity);
            return;
        }
        throw new TwoEntitiesOnSamePositionException();
    }

    /**
     * Gets the entity at the specified position on the board.
     *
     * @param position The position to query.
     * @return The entity at the specified position, or null if no entity is present.
     */
    public BoardEntity getEntity(EntityPosition position) {
        return boardData.get(position.toString());
    }

    /**
     * Gets the direction for an insect on the board.
     *
     * @param insect The insect for which to determine the direction.
     * @return The direction for the insect.
     */
    public Direction getDirection(Insect insect) {
        return null; // Not implemented
    }

    /**
     * Gets the sum of directions for an insect on the board.
     *
     * @param insect The insect for which to determine the direction sum.
     * @return The sum of directions for the insect.
     */
    public int getDirectionSum(Insect insect) {
        return 0; // Not implemented
    }
}
/**
 * An exception indicating that the board size is invalid.
 */
class InvalidBoardException extends Exception {
    InvalidBoardException() {
        super("Invalid board size");
    }

    @Override
    public String getMessage() {
        return "Invalid board size";
    }
}

/**
 * An exception indicating that the number of insects is invalid.
 */
class InvalidNumberOfInsectsException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid number of insects";
    }
}

/**
 * An exception indicating that the number of food points is invalid.
 */
class InvalidNumberOfFoodPointsException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid number of food points";
    }
}

/**
 * An exception indicating that the insect color is invalid.
 */
class InvalidInsectColorException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid insect color";
    }
}

/**
 * An exception indicating that the insect type is invalid.
 */
class InvalidInsectTypeException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid insect type";
    }
}

/**
 * An exception indicating that the entity position is invalid.
 */
class InvalidEntityPositionException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid entity position";
    }
}

/**
 * An exception indicating that there are duplicate insects.
 */
class DuplicateInsectException extends Exception {
    @Override
    public String getMessage() {
        return "Duplicate insects";
    }
}

/**
 * An exception indicating that there are two entities in the same position on the board.
 */
class TwoEntitiesOnSamePositionException extends Exception {
    @Override
    public String getMessage() {
        return "Two entities in the same position";
    }
}
