import java.io.PrintStream;
import java.util.*;

/**
 * QuestionsGame is similar to 20 Questions
 * It will ask the user questions to try and guess what the user's object is
 * For every time the computer loses, it gets smarter.
 * @Author Chris Carrillo on February 23, 2017
 */

public class QuestionsGame {
    private QuestionNode overallRoot; // the overallRoot of the QuestionNode
    private Scanner input; // the Scanner used to get input from the user


    /**
     * Initializes a new QuestionsGame with a single QuestionNode
     * With a String object if the file does not exist
     * @param object is the String of the file that does not exist
     */
    public QuestionsGame(String object) {
        this.input = new Scanner(System.in);
        overallRoot = new QuestionNode(object);
    }

    /**
     * Initializes a new QuestionsGame by reading from the Scanner of a file of questions
     * @param input is a Scanner of a file full of questions
     */
    public QuestionsGame(Scanner input) {
        this.input = new Scanner(System.in);
        overallRoot = constructorHelper(input, overallRoot);
    }

    /**
     * Helper method that builds the overallRoot tree
     * @param input is the Scanner of the questions/answers
     * @param root is the overallRoot (QuestionNode)
     * @return the QuestionNode that will be stored into overallRoot
     */
    private QuestionNode constructorHelper(Scanner input, QuestionNode root) {
        if (input.hasNextLine()) {
            String type = input.nextLine();
            String data = input.nextLine();
            root = new QuestionNode(data);

            if (type.contains("A:")) return root; //I am a leaf
            root.left = constructorHelper(input, root.left);
            root.right = constructorHelper(input, root.right);

            return root;
        }
        return null;
    }

    /**
     * Stores the questions to an output file
     * @param output is the PrintStream used to save the questions
     * @throws IllegalArgumentException if the PrintStream is null
     */
    public void saveQuestions(PrintStream output) throws IllegalArgumentException {
        if (output == null) throw new IllegalArgumentException("The PrintStream cannot be null");
        saveQuestions(output, overallRoot);
    }

    /**
     * Private helper method that prints the questions/answers in a
     * Pre-order traversal
     * @param output is the PrintStream that outputs to the file
     * @param root is the QuestionNode to be outputted
     */
    private void saveQuestions(PrintStream output, QuestionNode root) {
        if (root != null) {
            if (root.data.contains("?")) {
                output.println("Q:");
                output.println(root.data);
            } else {
                output.println("A:");
                output.println(root.data);
            }
            saveQuestions(output, root.left);
            saveQuestions(output, root.right);
        }
    }

    // Calls its helper method to set the overallRoot
    public void play() {
        overallRoot = play(overallRoot);
    }

    /**
     * Private helper method that uses the question tree to play a game with the user.
     * It will continue to play until it reaches an answer. For every time the computer loses,
     * it will get smarter by asking the user to provide it more information.
     * @param root is the QuestionNode root to be considered
     * @return a QuestionNode to set the overallRoot from the main play() method
     */
    private QuestionNode play(QuestionNode root) {
        if (root != null) {
            if (root.left != null && root.right != null) {
                System.out.print(root.data + " (y/n)? ");
                if (input.nextLine().trim().toLowerCase().startsWith("y")) { // if the user's input start's with y
                    play(root.left);
                } else { // otherwise, it must be no
                    play(root.right);
                }
            } else { // this must mean we have reached a possible answer, so guess it
                guessAnswer(root);
            }
        }
        return root;
    }

    /**
     * Private helper method of play(). guessAnswer() will guess the answer with the user. If it wins,
     * then it will display that it has won. If the computer has lost, then it will ask the user for more information
     * to make the program smarter.
     * @param root is the QuestionNode that is considered
     */
    private void guessAnswer(QuestionNode root) {
        System.out.println("I guess that your object is " + root.data + "!");
        System.out.print("Am I right? (y/n)? ");
        if (input.nextLine().trim().toLowerCase().startsWith("y")) { // the program won
            System.out.println("Awesome! I win!");
        } else { // the program lost, so make it smarter
            System.out.println("Boo! I Lose.  Please help me get better!");
            System.out.print("What is your object? ");
            String ans = input.nextLine().trim().toLowerCase();
            System.out.println("Please give me a yes/no question that distinguishes between " + ans + " and " + root.data + ".");
            System.out.print("Q: ");
            String ques = input.nextLine();
            System.out.print("Is the answer \"yes\" for " + ans + "? (y/n)? ");
            if (input.nextLine().trim().toLowerCase().startsWith("y")) { // adds more data to the tree
                root.left = new QuestionNode(ans);
                root.right = new QuestionNode(root.data);
                root.data = ques;
            } else { // adds more data to the tree
                root.left = new QuestionNode(root.data);
                root.right = new QuestionNode(ans);
                root.data = ques;
            }
        }
    }

    /**
     * QuestionNode represents a single node of the question tree.
     * @Author Chris Carrillo on February 23, 2017
     */
    private static class QuestionNode {
        public String data; // String of data
        public QuestionNode left; // The left side of the node
        public QuestionNode right; // The right side of the node

        // Constructor calls the other constructor to set the values
        public QuestionNode(String data) {
            this(data, null, null);
        }

        // Constructor that sets teh values data, left, and right with the value passed in
        public QuestionNode(String data, QuestionNode left, QuestionNode right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }
    }
}
