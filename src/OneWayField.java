import java.util.ArrayList;
import java.util.Scanner;

public class OneWayField extends Field {

    //Points to a category, when adding a new link, it will list possible documents to link to in said category
    protected Category pointTo;

    protected ArrayList<DocumentPair> links;

    public OneWayField(String name, Category pointTo, int fieldID) {
        super(name, fieldID);
        this.pointTo = pointTo;
        links = new ArrayList<>();
    }

    @Override
    public void edit(Scanner input) {
        int result = StringUtilities.numericalSelect("Would you like to (1) add a new link, (2) remove an" +
                " existing link, or (3) edit an existing link?", 1, 3, input);
        DocumentPair temp;
        switch (result) {
            case 1 -> add(input);
            case 2 -> {
                temp = selectPair(input);
                if (temp != null) {
                    links.remove(temp);
                }
            }
            case 3 -> {
                temp = selectPair(input);
                if (temp != null) {
                    result = StringUtilities.numericalSelect("Would you like to (1) delete this note, (2)" +
                                    " add to this note, or (3) replace this note?", 1, 3,
                                    input);
                    switch (result) {
                        case 1 -> temp.setNote("");
                        case 2 -> temp.setNote(
                                    StringUtilities.getUserInput("Enter the new text for the note", input));
                        case 3 -> temp.setNote(temp.getNote() +
                                    StringUtilities.getUserInput(
                                            "Enter the additional text for the note", input));
                    }
                }
            }
        }
        regenerateContent();
    }

    protected DocumentPair selectPair(Scanner input) {
        for (int i = 0; i < links.size(); i++) {
            System.out.println("(" + i + 1 + ") " + links.get(i).getDocument().getName());
        }
        int result = StringUtilities.numericalSelect("Please select the field you wish to delete from the above" +
                "list by number, or 0 to cancel", 0, links.size(), input);
        if (result == 0) {
            System.out.println("Operation cancelled.");
            return null;
        }
        else {
            return links.get(result - 1);
        }
    }

    protected void add(Scanner input) {
        ArrayList<Document> list = pointTo.getDocuments();
        for (int i = 0; i < list.size(); i++){
            System.out.println("(" + i + 1 + ") " + list.get(i).getName());
        }
        int result = StringUtilities.numericalSelect("Please select a document from the above list by number, or 0 to cancel",
                0, list.size(), input);
        if (result == 0) {
            System.out.println("Operation cancelled");
        }
        else {
            links.add(new DocumentPair(list.get(result - 1),
                    StringUtilities.getUserInput("Enter any note for this link", input)));
        }
        regenerateContent();
    }

    /*Completely replaces content in cases such as deletion by re-adding all recorded links and details, reduces string
      manipulations */
    protected void regenerateContent() {
        content = "";
        for(DocumentPair current: links) {
            content += current.toString() + ",\n";
        }
        content = content.substring(0,content.length()-4);
    }

    @Override
    public void removeLink(Document reference) {
        for (int i = 0; i < links.size(); i++) {
            if (links.get(i).getDocument() == reference) {
                links.remove(i);
                return;
            }
        }
    }

    //Used to keep pointer and note together and provide an easy interface to work with
    protected static class DocumentPair {
        Document pointer;
        String note = "";

        public DocumentPair(Document pointer, String note) {
            this.pointer = pointer;
            this.note = note;
        }

        public DocumentPair(Document pointer) {
            this.pointer = pointer;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {this.note = note;}

        public Document getDocument () {
            return pointer;
        }

        @Override
        public String toString() {
            return pointer.getName() + ": " + note;
        }
    }

}