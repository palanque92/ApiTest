package Campus;

public class Fields {

    private String name;
    private String code;
    private String id;

    private String Type;
    private  String SchoolId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getSchoolId() {
        return SchoolId;
    }

    public void setSchoolId(String schoolId) {
        SchoolId = schoolId;
    }


    @Override
    public String toString() {
        return "Fields{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", id='" + id + '\'' +
                ", Type='" + Type + '\'' +
                ", SchoolId='" + SchoolId + '\'' +
                '}';
    }
}
