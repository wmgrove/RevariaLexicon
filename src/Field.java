import java.util.Scanner;

public class Field implements Listable {

    protected String name;

    protected String content = "";

    protected final int fieldID;

    public Field(String name, int fieldID) {
        this.name = name;
        this.fieldID = fieldID;
    }

    public int getID() {
        return fieldID;
    }

    public void edit(Scanner input) {
        int result = StringUtilities.numericalSelect("Would you like to (1) add to this field, (2) delete its" +
                " content, or (3) replace its content?", 1, 3, input);
        switch (result) {
            case 3 -> content += "\n" + StringUtilities.getUserInput("Please input your new content", input);
            case 2 -> {
                System.out.print("Content deleted");
                content = "";
            }
        }
    }

    public void removeLink(Document reference) {}

    public void documentDeletion (Document reference) { removeLink(reference);}

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + ": " + content;
    }
}
