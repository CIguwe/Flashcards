package flashcards;

import java.io.*;
import java.util.*;

class FlashCard {
    private final String term;
    private final String definition;

    FlashCard(String term, String definition) {
        this.term = term;
        this.definition = definition;
    }

    public String getTerm() {
        return term;
    }

    public String getDefinition() {
        return definition;
    }
}

class FlashCardSet{
    private final Map<FlashCard, Integer> flashCardMap;
    protected final Scanner scanner;
    private final List<String> logList;

    FlashCardSet() {
        this.flashCardMap = new HashMap<>();
        this.scanner = new Scanner(System.in);
        this.logList = new ArrayList<>();
    }

    public void add() {
        System.out.println("The card:");
        writeToLog("The card:");

        String term = scanner.nextLine();
        writeToLog(term);
        if (!containsTerm(term)) {
            System.out.println("The definition of the card:");
            writeToLog("The definition of the card:");
            String definition = scanner.nextLine();
            writeToLog(definition);
            if(!containsDefinition(definition)) {
                FlashCard flashCard = new FlashCard(term, definition);
                flashCardMap.put(flashCard, 0);
                System.out.println("The pair (\"" + term + "\":" + "\"" + definition + "\") has been added.");
                writeToLog("The pair (\"" + term + "\":" + "\"" + definition + "\") has been added.");
            } else {
                System.out.println("The definition \"" + definition + "\" already exists.");
                writeToLog("The definition \"" + definition + "\" already exists.");
            }
        } else {
            System.out.println("The card \"" + term + "\" already exists.");
            writeToLog("The card \"" + term + "\" already exists.");
        }
    }

    private boolean containsDefinition(String definition) {
        for (FlashCard flashCard : flashCardMap.keySet()) {
            if (flashCard.getDefinition().equals(definition)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsTerm(String term) {
        for (FlashCard flashCard : flashCardMap.keySet()) {
            if (flashCard.getTerm().equals(term)) {
                return true;
            }
        }
        return false;
    }

    public void remove() {
        System.out.println("The card:");
        writeToLog("The card:");
        String term = scanner.nextLine();
        writeToLog(term);

        if (containsTerm(term)) {
            for (FlashCard flashCard : flashCardMap.keySet()) {
                if (flashCard.getTerm().equals(term)) {
                    flashCardMap.remove(flashCard);
                    System.out.println("The card has been removed");
                    writeToLog("The card has been removed");
                    return;
                }
            }
        } else {
            System.out.println("Can't remove \"" + term + "\": there is no such card.");
            writeToLog("Can't remove \"" + term + "\": there is no such card.");
        }
    }

    public void  importFile(String fileName) {
        try(Scanner fileScanner = new Scanner(new File(fileName))) {
            int count = 0;
            while (fileScanner.hasNextLine()) {
                String string = fileScanner.nextLine();
                writeToLog(string);
                String[] card = string.split(":");
                String keyTerm = card[0];
                String keyDefinition = card[1];
                Integer value = Integer.parseInt(card[2]);
                for (FlashCard flashCard : flashCardMap.keySet()) {
                    if (keyTerm.equals(flashCard.getTerm())) {
                        flashCardMap.remove(flashCard);
                        break;
                    }
                }
                FlashCard flashCard = new FlashCard(keyTerm, keyDefinition);
                flashCardMap.put(flashCard, value);
                count++;
            }
            System.out.println(count + " cards have been loaded.");
            writeToLog(count + " cards have been loaded.");
        } catch (FileNotFoundException e) {
            System.out.println( "File not found.");
            writeToLog( "File not found.");
        }
    }

    public void importFile() {
        System.out.println("File name:");
        writeToLog("File name:");
        String fileName = scanner.nextLine();
        writeToLog(fileName);
        importFile(fileName);
    }

    public void exportFile(String fileName) {
        try(PrintWriter printWriter = new PrintWriter(new File(fileName))) {
            flashCardMap.forEach((key, value) -> printWriter.println(key.getTerm() + ":" + key.getDefinition() + ":" + value));
            System.out.println(flashCardMap.size() + " cards have been saved");
            writeToLog(flashCardMap.size() + " cards have been saved");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            writeToLog(e.getMessage());
        }
    }

    public void exportFile() {
        System.out.println("File name:");
        writeToLog("File name:");
        String fileName = scanner.nextLine();
        writeToLog(fileName);
        exportFile(fileName);
    }

    public FlashCard flashCardWithThisTerm(String term) {
        for (FlashCard flashCard : flashCardMap.keySet()) {
            if (flashCard.getTerm().equals(term)) {
                return flashCard;
            }
        }
        return new FlashCard("", "");
    }

    public FlashCard flashCardWithThisDefinition(String definition) {
        for (FlashCard flashCard : flashCardMap.keySet()) {
            if (flashCard.getDefinition().equals(definition)) {
                return flashCard;
            }
        }
        return new FlashCard("", "");
    }
    public void ask() {
        System.out.println("How many times to ask?");
        writeToLog("How many times to ask?");
        int count = Integer.parseInt(scanner.nextLine());
        writeToLog(String.valueOf(count));
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            int itemNumber = random.nextInt(flashCardMap.size());
            int index = 0;

            for (FlashCard flashCard : flashCardMap.keySet()) {
                if (itemNumber == index) {
                    System.out.println("Print the definition of \"" + flashCard.getTerm() + "\":");
                    writeToLog("Print the definition of \"" + flashCard.getTerm() + "\":");
                    String definition = scanner.nextLine();
                    writeToLog(definition);
                    if (flashCard.getDefinition().equals(definition)) {
                        System.out.println("Correct answer.");
                        writeToLog("Correct answer.");
                    } else if (containsDefinition(definition)) {
                        System.out.println("Wrong answer. The correct one is \"" + flashCardWithThisTerm(flashCard.getTerm()).getDefinition() + "\", you've just written the " +
                                "definition of \"" + flashCardWithThisDefinition(definition).getTerm() + "\".");
                        writeToLog("Wrong answer. The correct one is \"" + flashCardWithThisTerm(flashCard.getTerm()).getDefinition() + "\", you've just written the " +
                                "definition of \"" + flashCardWithThisDefinition(definition).getTerm() + "\".");
                        flashCardMap.replace(flashCard, flashCardMap.get(flashCard) + 1);
                    } else {
                        System.out.println("Wrong answer. The correct one is \"" + flashCardWithThisTerm(flashCard.getTerm()).getDefinition() + "\".");
                        writeToLog("Wrong answer. The correct one is \"" + flashCardWithThisTerm(flashCard.getTerm()).getDefinition() + "\".");
                        flashCardMap.replace(flashCard, flashCardMap.get(flashCard) + 1);
                    }
                    break;
                } else {
                    index++;
                }
            }
        }
    }

    public void writeToLog(String string) {
        logList.add(string);
    }
    public void log() {
        System.out.println("File name");
        writeToLog("File name");
        String fileName = scanner.nextLine();
        writeToLog(fileName);

        try(PrintWriter printWriter = new PrintWriter(new File(fileName))) {
            logList.forEach(printWriter::println);
            System.out.println("The log has been saved");
            writeToLog("The log has been saved");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            writeToLog(e.getMessage());
        }
    }

    public void hardestCard() {
        List<String> result = new ArrayList<>();
        int maxCount = 1;
        for (FlashCard flashCard : flashCardMap.keySet()) {
            int currMax = flashCardMap.get(flashCard);
            if (currMax > maxCount) {
                maxCount = currMax;
                result.clear();
                result.add(flashCard.getTerm());
            } else if (currMax == maxCount) {
                result.add(flashCard.getTerm());
            }
        }
        switch (result.size()) {
            case 0:
                System.out.println("There are no cards with errors.");
                writeToLog("There are no cards with errors.");
                break;
            case 1:
                System.out.println("The hardest card is \"" + result.get(0) + "\". You have " + maxCount + " errors answering it.");
                writeToLog("The hardest card is \"" + result.get(0) + "\". You have " + maxCount + " errors answering it.");
                break;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < result.size(); i++) {
                    if (i < result.size() - 1) {
                        stringBuilder.append("\"").append(result.get(i)).append("\", ");
                    } else {
                        stringBuilder.append("\"").append(result.get(i)).append("\". ");
                    }
                }
                System.out.println("The hardest cards are " + stringBuilder.toString() + "You have " + maxCount + " errors answering them.");
                writeToLog("The hardest cards are " + stringBuilder.toString() + "You have " + maxCount + " errors answering them.");
                break;
        }

    }

    public void reset() {
        flashCardMap.replaceAll((f, v) -> 0);
        System.out.println("Card statistics has been reset.");
        writeToLog("Card statistics has been reset.");
    }
}
public class Main {
    public static void main(String[] args) {
        FlashCardSet flashCardSet = new FlashCardSet();
        String exportFileName = "";
        for (int i = 0, argsLength = args.length; i + 1 < argsLength; i++) {
            String arg = args[i];
            if (arg.equals("-import")) {
                flashCardSet.importFile(args[i + 1]);
            } else if (arg.equals("-export")) {
                exportFileName = args[i + 1];
            }
        }

        String choice;
        do {
            System.out.println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats): ");
            flashCardSet.writeToLog("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats): ");
            choice = flashCardSet.scanner.nextLine();
            flashCardSet.writeToLog(choice);

            switch (choice) {
                case "add":
                    flashCardSet.add();
                    break;
                case "remove":
                    flashCardSet.remove();
                    break;
                case "import":
                    flashCardSet.importFile();
                    break;
                case "export":
                    flashCardSet.exportFile();
                    break;
                case "ask":
                    flashCardSet.ask();
                    break;
                case "exit":
                    System.out.println("Bye bye!");
                    if (!exportFileName.equals("")) {
                        flashCardSet.exportFile(exportFileName);
                    }
                    return;
                case "log":
                    flashCardSet.log();
                    break;
                case "hardest card":
                    flashCardSet.hardestCard();
                    break;
                case "reset stats":
                    flashCardSet.reset();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + choice);
            }
        } while (true);

    }
}
