package org.daxprotocol.core.dictionary.daxenum;

public class DaxEnumValue {

    String value;
    String desc;

    public DaxEnumValue(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
