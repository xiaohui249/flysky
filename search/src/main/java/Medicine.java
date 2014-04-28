/**
 * Created by bbgds on 14-3-6.
 */
public class Medicine {
    private Integer id;
    private String name;
    private String function;
    private Integer num;
    private String highName;

    public Medicine() {
        super();
    }

    public Medicine(Integer id, String name, String function) {
        super();
        this.id = id;
        this.name = name;
        this.function = function;
    }

    public Medicine(Integer id, String name, String function, Integer num) {
        super();
        this.id = id;
        this.name = name;
        this.function = function;
        this.num = num;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getHighName() {
        return highName;
    }

    public void setHighName(String highName) {
        this.highName = highName;
    }
}
