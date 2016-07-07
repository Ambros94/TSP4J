package Utility;


class Edge implements Comparable<Edge> {
    private final int source;
    private final int destination;
    private final int length;

    Edge(int source, int destination, int length) {
        this.source = source;
        this.destination = destination;
        this.length = length;
    }

    int getSource() {
        return source;
    }

    int getDestination() {
        return destination;
    }

    public int getLength() {
        return length;
    }

    @Override
    public int compareTo(Edge o) {
        return length - o.getLength();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        return source == edge.source && destination == edge.destination;

    }

    @Override
    public int hashCode() {
        int result = source;
        result = 31 * result + destination;
        return result;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "source=" + source +
                ", destination=" + destination +
                ", length=" + length +
                '}';
    }
}
