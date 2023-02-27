import java.util.ArrayList;

/*
A category acts as a template for documents of its type. It holds the fields that are in use, and manages the fields of
documents of its type. It also holds documents of its type for access and reference.
 */
public class Category implements Listable {
    ArrayList<FieldTemplate> fields;
    ArrayList<Document> documents;
    String name;
    ArrayList<Category> oneWayPointers = new ArrayList<>();

    public Category (String name){
        this.name = name;
        fields = new ArrayList<>();
        documents = new ArrayList<>();
    }

    public void addField(FieldTemplate newField) {
        fields.add(newField);
        for (Document current: documents) {
            current.addField(newField);
        }
    }

    public void newDocument(String name) {
        documents.add(new Document(name, this));
    }

    public void generateFields(ArrayList<Field> fields, Document owner) {
        for (FieldTemplate fieldBuilder:this.fields) {
            fields.add(fieldBuilder.makeField(owner));
        }
    }

    public ArrayList<Document> getDocuments() {
        return documents;
    }

    @Override
    public String getName() {
        return name;
    }

    public ArrayList<FieldTemplate> getFields() {
        return fields;
    }

    public void addOneWayPointer(Category pointer) {
        oneWayPointers.add(pointer);
    }

    public ArrayList<Category> getOneWayPointers() {
        return oneWayPointers;
    }
}
