package net.biomodels.kisao;

/**
* @author Anna Zhukova
*         Date: 13-May-2011
*         Time: 13:08:40
*/
public enum SynonymType {
    EXACT("EXACT"),
    RELATED("RELATED"),
    NARROW("NARROW"),
    BROAD("BROAD");

    private final String name;

    SynonymType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public static SynonymType getDefault() {
        return RELATED;
    }

    public static SynonymType byName(String name) {
        if (name == null) return getDefault();
        for (SynonymType type : SynonymType.values()) {
            if (type.getName().equals(name)) return type;
        }
        return getDefault();
    }
}
