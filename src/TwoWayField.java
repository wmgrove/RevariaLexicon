import java.util.ArrayList;
import java.util.Scanner;

public class TwoWayField extends OneWayField{

    //Points to ID of field such that that field can add the backlink needed
    int mirror;
    Document owner;

    public TwoWayField(String name, Category pointTo, int mirror, Document owner, int fieldID) {
        super(name, pointTo, fieldID);
        this.mirror = mirror;
        this.owner = owner;
    }

    @Override
    public void add(Scanner input) {
        ArrayList<Document> list = pointTo.getDocuments();
        for (int i = 0; i < list.size(); i++){
            System.out.println("(" + i + 1 + ") " + list.get(i).getName());
        }
        int result = StringUtilities.numericalSelect("Please select a document from the above list by number," +
                        " (-1) to cancel, or (0) to create a new document",-1, list.size(), input);
        switch (result) {
            case -1: System.out.println("Operation cancelled"); break;

            case 0: {
                pointTo.newDocument(StringUtilities.getUserInput("Please enter the document name", input));
                result = pointTo.getDocuments().size();
            }

            default: {
                links.add(new DocumentPair(list.get(result - 1),
                        StringUtilities.getUserInput("Enter any note for this link", input)));
                mirrorAdd(list.get(result - 1));
            }
        }
        regenerateContent();
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
                    mirrorRemove(temp.getDocument());
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

    //Tells mate to add a link to this
    private void mirrorAdd(Document reference) {
        Field mate = reference.getFieldByID(mirror);
        ((TwoWayField) mate).addLink(owner);
    }

    //Tells mate to remove a link to this
    private void mirrorRemove(Document reference) {
        Field mate = reference.getFieldByID(mirror);
        mate.removeLink(owner);
    }

    //Removes backlink without pulling up edit dialogue
    @Override
    public void removeLink(Document reference) {
        for (int i = 0; i < links.size(); i++) {
            if (links.get(i).getDocument() == reference) {
                links.remove(i);
                return;
            }
        }
    }

    //Adds backlink without pulling up edit dialogue
    public void addLink(Document reference) {
        for (DocumentPair current: links) {
            if (current.getDocument() == reference) {
                return;
            }
        }
        links.add(new DocumentPair(reference));
    }

    @Override
    public void documentDeletion(Document reference) {
        for (DocumentPair current: links) {
            mirrorRemove(current.getDocument());
        }
    }
}
