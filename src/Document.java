import java.util.ArrayList;

public class Document implements Listable{
    static int docCount = 0; //tracks number of documents across all documents in order to prevent an ID collision
    int docID; //identifies document for linking purposes
    String name;
    ArrayList<Field> fields;
    //Category type; currently deprecated, for use if knowledge of category is needed

    public Document(String name, Category category) {
        docID = docCount;
        docCount++;
        instantiate(name, category);
    }

    //For use when loading a previous project, prevents link breakage
    public Document(String name, Category category, int docID) {
        this.docID = docID;
        if (docID >= docCount) {
            docCount = docID + 1;
        }
        instantiate(name, category);
    }

    private void instantiate (String name, Category category) {
        this.name = name;
        fields = new ArrayList<>();
        category.generateFields(fields, this);
        //type = category;
    }

    //Returns a field, used for linking two-way relationships
    public Field getFieldByID(int ID) {
        for(Field current: fields) {
            if (ID== current.getID()) {
                return current;
            }
        }
        return null;
    }

    //Returns field based on array index, used for grabbing fields for the editor
    public Field getFieldByIndex (int i) {
        return fields.get(i);
    }

    public void addField(FieldTemplate newField) {
        fields.add(newField.makeField(this));
    }

    public String getName() {
        return name;
    }

    public void delete(ArrayList<Category> oneWayPairs) {
        for (Category current: oneWayPairs) {
            for (Document potentialPair: current.getDocuments()) {
                for (int i = 0; i < current.getFields().size(); i++) {
                    potentialPair.getFieldByIndex(i).documentDeletion(this);
                }
            }
        }
        for (Field current: fields) {
            current.documentDeletion(this);
        }
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        String output = name +"\n";
        for (Field current: fields) {
            output += current.toString() + "\n";
        }
        return output;
    }
}
