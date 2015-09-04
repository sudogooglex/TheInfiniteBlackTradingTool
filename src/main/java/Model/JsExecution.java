package Model;

public class JsExecution {

    public String js;
    public String name;

    /**
     *
     * @param j js
     * @param d name
     */
    public JsExecution(String j, String d) {
        js = j;
        name = d;
    }

    public JsExecution() {
        js = "";
        name = "";
    }
    
    @Override
    public String toString() {
        return name + "\t" + js;
    }
    
    public void print() {
        System.out.println(this);
    }

    /**
     * @return the js
     */
    public String getJs() {
        return js;
    }

    /**
     * @param js the js to set
     */
    public void setJs(String js) {
        this.js = js;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
