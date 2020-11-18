package hk.edu.polyu.comp.comp2021.cvfs.model;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Objects;

public class Criterion implements Cloneable {
    /**
     * The name of the criterion, containing exactly 2 English letters.
     */
    private String name;

    /**
     * The name of the attribute. One of ["name","type","size"].
     */
    private String attr;

    /**
     * The name of the option.
     * name -> contains
     * type -> equals
     * size -> a comparison mark
     */
    private String op;

    /**
     * name,type -> A string in double quote
     * size -> An integer
     */
    private String val;

    /**
     * To decide whether the result of check should be negated, false by default.
     */
    private boolean negation = false;

    /**
     * Used to mark the special criterion IsDocument
     */
    private boolean isDocumentMark = false;

    /** A ScriptEngine to evaluate the result*/
    private static ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");

    /**
     * The special criterion isDocument
     */
    static Criterion isDocument = new Criterion("IsDocument");

    static {
        isDocument.isDocumentMark = true;
    }

    /**
     * Get the special criterion isDocument
     *
     * @return the reference to isDocument
     */
    public static Criterion getIsDocument() {
        return isDocument;
    }

    /**
     * Create a criterion.
     *
     * @param name The name of the criterion.
     * @param attr The name of the attribute.
     * @param op   The name of the operation.
     * @param val  The value of the operation.
     */
    public Criterion(String name, String attr, String op, String val) {
        this.name = name;
        this.attr = attr;
        this.op = op;
        this.val = val;
    }

    /**
     * Constructor with only name initialized.
     */
    public Criterion(String name) {
        this.name = name;
    }

    /**
     * A clone constructor
     */
    private Criterion(Criterion x) {
        name = x.getName();
        attr = x.getAttr();
        op = x.getOp();
        val = x.getVal();
        negation = x.isNeg();
    }

    @Override
    public Object clone() {
        return new Criterion(this);
    }

    /**
     * @return The name of the criterion
     */
    public String getName() {
        return name;
    }

    /**
     * @return The attribute of the criterion.
     */
    public String getAttr() {
        return attr;
    }

    /**
     * @return The operation of the criterion.
     */
    public String getOp() {
        return op;
    }

    /**
     * @return The value of the operation.
     */
    public String getVal() {
        return val;
    }

    /**
     * Set this Criterion to its Negative
     */
    public void setNeg() {
        negation = !negation;
    }

    /**
     * set the this.name to param
     * without valid-name-checking
     *
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return True if the check result is negated.
     */
    public boolean isNeg() {
        return negation;
    }

    /**
     * Check whether all parameters are valid. Rubrics over the declarations of fields.
     *
     * @param name name
     * @param attr attribute
     * @param op   operation
     * @param val  value
     * @return True if all parameters are valid.
     */
    public static boolean isValidCri(String name, String attr, String op, String val){
        return (isValidCriName(name) && isValidCriContent(attr, op, val));
    }

    /**
     * Check if the unit x fits the criterion.
     * Print a warning and return false if x is null.
     *
     * @param x The unit to be checked.
     * @return True if the condition holds.
     */
    public boolean check(Unit x) {
        if (x == null) {
            System.out.println("Error: Invalid Argument. Details: Checking a null Unit with " + this);
            return false;
        }

        if (isDocumentMark)
            return x instanceof Document;

        String expression = toJsString();

        engine.put("size", x.getSize());
        engine.put("name", x.getName());

        if (expression.contains("type")) {
            if (x instanceof Document)
                engine.put("type", ((Document) x).getType().toString());
            else {
                System.out.println("Error: Invalid Argument. Details: Type error: " + x);
                return false;
            }
        }

        try {
            return (boolean) engine.eval(expression);
        } catch (ScriptException e) {
            System.out.println("Error: Invalid Argument. Details: invalid script: " + expression);
            return false;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Criterion criterion = (Criterion) o;
        return isNeg() == criterion.isNeg() &&
                Objects.equals(getName(), criterion.getName()) &&
                Objects.equals(getAttr(), criterion.getAttr()) &&
                Objects.equals(getOp(), criterion.getOp()) &&
                Objects.equals(getVal(), criterion.getVal());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, attr, op, val, negation);
    }

    @Override
    public String toString() {
        if (isDocumentMark) return "Criterion { IsDocument }";

        return "Criterion '" + getName() + "', { " + criToString() + " }";
    }

    /**
     * Get a negative copy of this
     *
     * @param negName the name of the negative criterion
     * @return a negative criterion of this
     */
    public Criterion getNegCri(String negName) {
        Criterion that = new Criterion(this);
        that.setNeg();
        that.setName(negName);
        return that;
    }

    /**
     * Check whether CriName is valid.
     *
     * @param name the name of the criterion
     * @return boolean, whether it contains exactly 2 English letters.
     */
    public static boolean isValidCriName(String name) {
        if (name == null) return false;

        return (name.matches("[a-zA-Z]{2}"));
    }

    // ===================================== private and protected methods for implementation

    /**
     * Convert this to a js string
     *
     * @return js String
     */
    private String toJsString() {
        return criToJsString(this);
    }

    /**
     * toString for a single Criterion object
     *
     * @return toString
     */
    protected String criToString() {
        String base;
        base = getAttr() + ' ' + getOp() + ' ' + getVal();

        if (isNeg())
            base = "!(" + base + ")";

        return base;
    }

    /**
     * toJsString for a single Criterion object
     *
     * @param cur current Criterion object;
     * @return toString
     */
    private static String criToJsString(Criterion cur) {
        String base;
        switch (cur.getAttr()) {
            case "name":
                // js: name.contains(matcher), matcher = [ "sth" ]
                base = cur.getAttr() + ".contains(" + cur.getVal() + ")";
                break;
            case "type":
                // js: type == matcher, matcher = [ "sth" ]
                base = cur.getAttr() + "==" + cur.getVal();
                break;
            case "size":
                // js: size >= 30
                base = cur.getAttr() + cur.getOp() + cur.getVal();
                break;
            default:
                System.out.println("Error: Invalid Argument. Details: getAttr() failure: " + cur);
                return "";
        }

        if (cur.isNeg())
            base = "!(" + base + ")";

        return base;
    }

    /**
     * Check whether CriContent (attr, op, val) is valid.
     *
     * @param attr 'name'      | 'type'    | 'size'
     * @param op   'contains'  | 'equals'  | 6 logOps
     * @param val  '"text"'    | '"text"'  | integer
     * @return boolean
     */
    private static boolean isValidCriContent(String attr, String op, String val) {
        if (attr == null || op == null || val == null) {
            System.out.println("Error: Invalid Argument. Details: Null Cri-Content detected.");
            return false;
        }

        switch (attr) {
            case "name":
                return op.equals("contains")
                        && val.matches("^\\\"\\w+\\\"$");

            case "type":
                if (!val.matches("\\\"(txt|html|css|java)\\\""))
                    System.out.println("Warning: Entered file type not supported. Details: entered " + val + ".");
                return op.equals("equals")
                        && val.matches("^\\\"\\w+\\\"$");

            case "size":
                boolean flagOp, flagVal;
                flagOp = op.matches("([><]=?)|([!=]=)");

                try {
                    Integer.parseInt(val);
                    flagVal = true;
                } catch (NumberFormatException e) {
                    flagVal = false;
                }

                return flagOp && flagVal;
        }
        return false;
    }

}
