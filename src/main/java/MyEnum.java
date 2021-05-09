public enum MyEnum {
    Left(1),
    Top(1 << 1),
    Right(1 << 2),
    Bottom(1 << 3);
    private int value;

    MyEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
