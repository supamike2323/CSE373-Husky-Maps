package seamfinding;

import graphs.Edge;
import graphs.Graph;
import graphs.shortestpaths.ShortestPathSolver;
import seamfinding.energy.EnergyFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Generative adjacency list graph single-source {@link ShortestPathSolver} implementation of the {@link SeamFinder}
 * interface.
 *
 * @see Graph
 * @see ShortestPathSolver
 * @see SeamFinder
 */
public class GenerativeSeamFinder implements SeamFinder {
    /**
     * The constructor for the {@link ShortestPathSolver} implementation.
     */
    private final ShortestPathSolver.Constructor<Node> sps;

    /**
     * Constructs an instance with the given {@link ShortestPathSolver} implementation.
     *
     * @param sps the {@link ShortestPathSolver} implementation.
     */
    public GenerativeSeamFinder(ShortestPathSolver.Constructor<Node> sps) {
        this.sps = sps;
    }

    @Override
    public List<Integer> findHorizontal(Picture picture, EnergyFunction f) {
        PixelGraph graph = new PixelGraph(picture, f);
        List<Node> seam = sps.run(graph, graph.source).solution(graph.sink);
        seam = seam.subList(1, seam.size() - 1);
        List<Integer> result = new ArrayList<>(seam.size());
        for (Node node : seam) {
            PixelGraph.Pixel pixel = (PixelGraph.Pixel) node;
            result.add(pixel.y);
        }
        return result;
    }

    /**
     * Generative adjacency list graph of {@link Pixel} vertices and {@link EnergyFunction}-weighted edges.
     */
    private static class PixelGraph implements Graph<Node> {
        private final Picture picture;
        private final EnergyFunction f;

        // Source node
        private final Node source = new Node() {
            @Override
            public List<Edge<Node>> neighbors(Picture picture, EnergyFunction f) {
                List<Edge<Node>> neighbors = new ArrayList<>();
                for (int y = 0; y < picture.height(); y++) {
                    Pixel to = new Pixel(0, y);
                    neighbors.add(new Edge<>(this, to, f.apply(picture, 0, y)));
                }
                return neighbors;
            }
        };

        // Sink node
        private final Node sink = new Node() {
            @Override
            public List<Edge<Node>> neighbors(Picture picture, EnergyFunction f) {
                return List.of();
            }
        };

        public PixelGraph(Picture picture, EnergyFunction f) {
            this.picture = picture;
            this.f = f;
        }

        @Override
        public List<Edge<Node>> neighbors(Node node) {
            return node.neighbors(picture, f);
        }

        /**
         * A pixel in the {@link PixelGraph} representation of the {@link Picture}.
         */
        public class Pixel implements Node {
            private final int x;
            private final int y;

            /**
             * Constructs a pixel representing the (<i>x</i>, <i>y</i>) indices in the picture.
             *
             * @param x horizontal index into the picture.
             * @param y vertical index into the picture.
             */
            public Pixel(int x, int y) {
                this.x = x;
                this.y = y;
            }

            @Override
            public List<Edge<Node>> neighbors(Picture picture, EnergyFunction f) {
                List<Edge<Node>> neighbors = new ArrayList<>();
                for (int dx = 1, dy = -1; dy <= 1; dy++) {
                    int nx = x + dx;
                    int ny = y + dy;
                    if (nx < picture.width() && ny >= 0 && ny < picture.height()) {
                        Pixel neighbor = new Pixel(nx, ny);
                        double weight = f.apply(picture, nx, ny);
                        neighbors.add(new Edge<>(this, neighbor, weight));
                    }
                }
                if (x == picture.width() - 1) {
                    neighbors.add(new Edge<>(this, sink, 0));
                }
                return neighbors;
            }

            @Override
            public String toString() {
                return "(" + x + ", " + y + ")";
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                } else if (!(o instanceof Pixel)) {
                    return false;
                }
                Pixel other = (Pixel) o;
                return this.x == other.x && this.y == other.y;
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y);
            }
        }
    }
}
