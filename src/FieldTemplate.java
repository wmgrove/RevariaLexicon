import java.util.List;
import java.util.Scanner;

public class FieldTemplate implements Listable {
    static int fieldCount = 0; //Tracks number of field templates across all categories to prevent ID collision

    //Used for linking purposes with two-way-relationships, but kept at a high level to reduce needs for casting
    int fieldID;
    FieldType type; //enum to track field subtype
    Category pointTo = null; //Tracks category for the endpoint of a one-way or two-way relationship
    Category owner = null;
    int mirror = -1; //Tracks fieldID of endpoint for a two-way relationship
    String name;

    public FieldTemplate(Scanner input, Category owner, List<Category> categories) {
        fieldID = fieldCount;
        fieldCount++;
        System.out.print("Please enter field name ");
        name = input.next();
        int relation = StringUtilities.numericalSelect("Please Select Field type: (1) No relation" +
                " (2) One-way relation (3) Two-way relation", 1, 3, input);
        switch (relation) {
            case 1 -> type = FieldType.NORELATION;
            case 2 -> oneWayGenerator(input, categories, owner);
            case 3 -> twoWayGenerator(input, owner, categories);
        }
    }

    public FieldTemplate(Category pointer, Scanner input, int mirror, Category owner) {
        fieldID = fieldCount;
        fieldCount++;
        StringUtilities.getUserInput("Please enter the name for this field", input);
        this.pointTo = pointer;
        this.mirror = mirror;
        type = FieldType.TWOWAYRELATION;
        owner.addField(this);
    }

    private void oneWayGenerator(Scanner input, List<Category> categories, Category owner) {
        type = FieldType.ONEWAYRELATION;
        pointTo = categorySelector(input, categories);
        pointTo.addOneWayPointer(owner);
    }

    private Category categorySelector(Scanner input, List<Category> categories) {
        StringUtilities.optionList(categories);
        int result = StringUtilities.numericalSelect("Select a category from the list above to point to",
                1, categories.size(), input);
        return categories.get(result - 1);
    }

    private void twoWayGenerator(Scanner input, Category owner, List<Category> categories) {
        type = FieldType.TWOWAYRELATION;
        pointTo = categorySelector(input, categories);
        mirror = fieldSelector(input);
        this.owner = owner;
        if (mirror == -1) {
            FieldTemplate temp = new FieldTemplate(owner, input, fieldID, pointTo);
            mirror = temp.getFieldID();
        }
    }

    public int getFieldID() {
        return fieldID;
    }

    private int fieldSelector(Scanner input) {
        StringUtilities.optionList(pointTo.getFields());
        return StringUtilities.numericalSelect("Please select the field you wish to point to from the list" +
                " above by number, or 0 to make a new field", 0, pointTo.getFields().size(), input) - 1;
    }

    //Selects constructor based on field subtype
    public Field makeField(Document owner) {
        switch (type) {
            case NORELATION -> {
                return new Field(name, fieldID);
            }
            case ONEWAYRELATION -> {
                return new OneWayField(name, pointTo, fieldID);
            }
            case TWOWAYRELATION -> {
                return new TwoWayField(name, pointTo, mirror, owner, fieldID);
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
}
