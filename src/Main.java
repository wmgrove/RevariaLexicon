import java.util.*;

public class Main {
    public static void main(String[] args) {
        ArrayList<Category> categories = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        boolean willContinue;
        do {
            mainMenu(categories, input);
            willContinue = StringUtilities.numericalSelect(
                    "Input (1) to continue the program, or (2) to exit", 1, 2, input) == 1;
        } while(willContinue);
    }

    private static void mainMenu(ArrayList<Category> categories, Scanner input) {
        boolean selected = false;
        int menuSelection;
        do {
            menuSelection = StringUtilities.numericalSelect("Would you like to (0) view a list of" +
                    " categories, (1) view/edit/create a document/field, or (2) create a category?",
                    0, 2, input);
            if (menuSelection == 0) {
                StringUtilities.optionList(categories);
            }
            else if ((menuSelection == 1 || menuSelection == 3) && categories.size() == 0) {
                System.out.println("Invalid selection, no categories exist for a document to be in.");
            }
            else {
                selected = true;
            }
        } while (!selected);
        switch (menuSelection) {
            case 1 -> categoryMenu(categories, input);
            case 2 -> categories.add(new Category(StringUtilities.getUserInput("Please enter a category name",
                    input)));
        }
    }

    private static void categoryMenu(ArrayList<Category> categories, Scanner input) {
        int categorySelection = StringUtilities.optionListSelector(categories,
                "Please select a category from the above list by number", input);
        Category currentCategory = categories.get(categorySelection - 1);
        int menuSelection = StringUtilities.numericalSelect("Would you like to (1) view a document, (2) edit a"
                + " document, (3) create a document, (4) delete a document, (5) add a field to this category, or (6)" +
                " remove a field from this category?", 1, 6, input);
        switch (menuSelection) {
            case 1 -> System.out.print(documentSelector(currentCategory.getDocuments(), input).toString());

            case 2 -> documentEditor(currentCategory.getDocuments(), input);

            case 3 -> currentCategory.newDocument(StringUtilities.getUserInput("Please enter the document name",
                        input));

            case 4 -> { Document removing = documentSelector(currentCategory.getDocuments(), input);
                        removing.delete(currentCategory.getOneWayPointers());
                        currentCategory.getDocuments().remove(removing);
            }

            case 5 -> currentCategory.addField(new FieldTemplate(input, currentCategory, categories));

            case 6 -> System.out.println("Functionality not yet supported");
        }
    }

    private static void documentEditor(ArrayList<Document> documents, Scanner input) {
        int menuSelection = StringUtilities.optionListSelector(documents,
                "Select which document to edit", input);
        Document currentDocument = documents.get(menuSelection - 1);
        menuSelection = StringUtilities.optionListSelector(currentDocument.getFields(),
                "Select which field to edit", input);
        currentDocument.getFieldByIndex(menuSelection - 1).edit(input);
    }

    private static Document documentSelector(ArrayList<Document> list, Scanner input) {
        return list.get(StringUtilities.optionListSelector(list,
                "Select a document from the above list by number", input) - 1);
    }

}
