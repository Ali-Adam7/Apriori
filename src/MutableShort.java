//optimization – Most efficient way to increment a Map value in Java – Code Utility:
// https://codeutility.org/optimization-most-efficient-way-to-increment-a-map-value-in-java-stack-overflow/

class MutableShort {
    short value = 0;

    public void increment() {
        ++value;
    }

    public int get() {
        return value;
    }
}