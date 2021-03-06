package student;

import game.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Escape the cavern, based on Dijkstra's algorithm, calculating the route having most gold.
 *
 * @author Fernando Silva
 */
public class EscapeFinder {

    final private EscapeState state;

    /**
     * Route calculated in the constructor.
     */
    final private List<Node> route;

    /**
     * Constructor.
     *
     * @param state escape state.
     */
    EscapeFinder(EscapeState state) {
        this.state = state;

        // The following was a test using the code provided in coursework-temple/src/searchexample/Paths.java
        // resulting in the same shortest route as my getShortestRoute()
        // route = dijkstra(state.getCurrentNode(), state.getExit());

        // route = getShortestRoute(state.getCurrentNode(), state.getExit());
        route = getMaximumGoldRoute(state.getCurrentNode(), state.getExit());
    }

    /**
     * Calculates the shortest route between start and end nodes.
     * <p>
     * This code is based on Cavern.minPathLengthToTarget() which is used to calculate the time limit for the escape
     * stage of the game.
     *
     * @param start initial node in the route.
     * @param end   final node in the route.
     * @return list of nodes with the shortest route between start and end
     */
    private List<Node> getShortestRoute(Node start, Node end) {
        Map<Node, NodeAndWeightTuple> routeWeights = new HashMap<>();
        routeWeights.put(start, new NodeAndWeightTuple(null, 0));

        PriorityQueue<NodeAndWeightTuple> frontier = new PriorityQueue<>(1, Comparator.comparingInt(n -> n.weight));
        frontier.add(new NodeAndWeightTuple(start, 0));

        while (!frontier.isEmpty()) {
            Node node = frontier.poll().node;
            if (node.equals(end)) {
                break;
            }

            int nWeight = routeWeights.get(node).weight;

            for (Edge edge : node.getExits()) {
                Node edgeNode = edge.getOther(node);
                NodeAndWeightTuple existingTuple = routeWeights.get(edgeNode);

                int weightThroughN = nWeight + edge.length();

                if (existingTuple == null) {
                    routeWeights.put(edgeNode, new NodeAndWeightTuple(node, weightThroughN));
                    frontier.add(new NodeAndWeightTuple(edgeNode, weightThroughN));
                } else if (weightThroughN < existingTuple.weight) {
                    routeWeights.put(edgeNode, new NodeAndWeightTuple(node, weightThroughN));

                    // Change the weight of an existent node, by removing it first
                    for (NodeAndWeightTuple nodeAndWeight : frontier) {
                        if (edgeNode.equals(nodeAndWeight.node)) {
                            frontier.remove(nodeAndWeight);
                            break;
                        }
                    }
                    frontier.add(new NodeAndWeightTuple(edgeNode, weightThroughN));
                }
            }
        }


        ArrayList<Node> route = new ArrayList<>();
        Node node = end;
        while (node != null) {
            route.add(node);
            node = routeWeights.get(node).node;
        }
        Collections.reverse(route);

        return route;
    }

    /**
     * Calculates the route having most gold between start and end nodes.
     *
     * @param start initial node in the route.
     * @param end   final node in the route.
     * @return list of nodes with the route having most gold between start and end
     */
    private List<Node> getMaximumGoldRoute(Node start, Node end) {
        // Initialize the discovery of the route with most gold
        Node currentNode = start;

        // That can the traversed within the time limit
        int remainingTime = state.getTimeRemaining();

        // Update the route with the starting node
        List<Node> goldRoute = new ArrayList<>();
        goldRoute.add(currentNode);

        // When discovering the nodes, exclude the ones already visited
        Set<Node> visited = new HashSet<>();
        visited.add(currentNode);

        // Calculate the amount of gold and its distribution, giving priority to nodes with most gold
        IntSummaryStatistics totalGoldStats = state.getVertices().stream()
                .filter(n -> n.getTile().getGold() > 0)
                .mapToInt(value -> value.getTile().getGold())
                .summaryStatistics();

        System.out.println("Gold available   : " + totalGoldStats.getSum());

        // When the time limit is reached, stop and escape now
        while (remainingTime > 0) {
            // Calculate the routes for all the nodes with gold that we are interested in reaching
            final Map<Node, List<Node>> routesToGoldNodes = getInterestingGoldRoutes(
                    currentNode, totalGoldStats.getAverage(), visited);

            // Get the node (and route) that we are closer too (exceptionally give priority to reach the bonus)
            final Optional<Map.Entry<Node, List<Node>>> closestGoldNode = routesToGoldNodes.entrySet().stream()
                    .min(Comparator.comparingInt(
                            // Cavern.TASTY_VALUE == 5000 (no idea if I'm allowed to use the constant)
                            e -> e.getKey().getTile().getGold() == 5000 ? -1 : routeTraverseTime(e.getValue())));

            // No more nodes with gold
            if (!closestGoldNode.isPresent()) {
                goldRoute.addAll(getShortestRoute(currentNode, end));
                remainingTime = 0;
            } else {
                // Calculate the time required to traverse the selected route
                int timeToNode = routeTraverseTime(closestGoldNode.get().getValue());

                // Calculate the time taking in account the time required to exit
                List<Node> exitRoute = getShortestRoute(closestGoldNode.get().getKey(), end);
                int totalTime = timeToNode + routeTraverseTime(exitRoute);

                // No more time available
                if (totalTime > remainingTime) {
                    goldRoute.addAll(getShortestRoute(currentNode, end));
                    remainingTime = 0;
                } else {
                    currentNode = closestGoldNode.get().getKey();
                    goldRoute.addAll(closestGoldNode.get().getValue());
                    visited.addAll(closestGoldNode.get().getValue());
                    remainingTime -= timeToNode;
                }

            }
        }

        return goldRoute;
    }

    /**
     * Calculates the shortest route from the current node to each node (with enough gold to be interesting) in the map.
     *
     * @param currentNode  the node we are located
     * @param moreGoldThan the minimum gold we are interested in
     * @param visited      don't calculate for nodes that have been visited
     * @return a map with the routes from the current node to every gold node
     */
    private Map<Node, List<Node>> getInterestingGoldRoutes(Node currentNode, double moreGoldThan, Set<Node> visited) {
        return state.getVertices().stream()
                .filter(n -> !visited.contains(n))
                .filter(n -> n.getTile().getGold() >= moreGoldThan)
                .collect(Collectors.toUnmodifiableMap(n -> n, n -> getShortestRoute(currentNode, n)));
    }

    /**
     * Calculates the total time required to traverse the route.
     *
     * @param route to traverse.
     * @return time required.
     */
    private int routeTraverseTime(List<Node> route) {
        return IntStream.range(0, route.size() - 1)
                .map(i -> route.get(i).getEdge(route.get(i + 1)).length())
                .sum();
    }

    /**
     * Checks if the tile has gold and pick it up.
     *
     * @param tile to pick up gold from.
     */
    private void pickUpGold(Tile tile) {
        if (tile.getGold() > 0) {
            state.pickUpGold();
        }
    }

    /**
     * Traverse the route while picking up gold.
     */
    public void find() {
        pickUpGold(state.getCurrentNode().getTile());

        for (Node node : route) {
            if (!state.getCurrentNode().equals(node)) {
                state.moveTo(node);
                pickUpGold(state.getCurrentNode().getTile());
            }
        }
    }

    /**
     * Tuple structure of Node and Weight to be used internally for shortest route calculation.
     */
    private class NodeAndWeightTuple {
        private Node node;
        private int weight;

        NodeAndWeightTuple(Node node, int weight) {
            this.node = node;
            this.weight = weight;
        }
    }

}
