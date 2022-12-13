public class Pair implements Comparable<Pair> {
    int x;
    int y;

    public Pair(int x, int y) {
        this.x = Math.min(x, y);
        this.y = Math.max(x, y);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Pair)) {
            return false;
        }

        Pair c = (Pair) o;
        if (c.x == this.x && c.y == this.y) {
            return true;
        }
        return false;
    }

    public String toString() {
        return ("(" + this.x + "," + this.y + ")");
    }

    public int hashCode() {
        // szudzik pairing function:
        // http://szudzik.com/ElegantPairing.pdf
        // return ((x * x) + this.y);

        return (((y) * (y - 1)) / 2) + ((x));

    }

    public int compareTo(Pair p2) {
        if (this.x == p2.x && this.y == p2.y) {
            return 0;
        }
        if (this.x > p2.x) {
            return 1;
        }

        if (this.x < p2.x) {
            return -1;
        }

        if (this.x == p2.x) {
            if (this.y > p2.y) {
                return 1;
            }
        }
        return -1;

    }

}
