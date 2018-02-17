package student;

import game.*;

import java.util.*;

public class Explorer {

    /**
     * Explore the cavern, trying to find the orb in as few steps as possible.
     * Once you find the orb, you must return from the function in order to pick
     * it up. If you continue to move after finding the orb rather
     * than returning, it will not count.
     * If you return from this function while not standing on top of the orb,
     * it will count as a failure.
     * <p>
     * There is no limit to how many steps you can take, but you will receive
     * a score bonus multiplier for finding the orb in fewer steps.
     * <p>
     * At every step, you only know your current tile's ID and the ID of all
     * open neighbor tiles, as well as the distance to the orb at each of these tiles
     * (ignoring walls and obstacles).
     * <p>
     * To get information about the current state, use functions
     * getCurrentLocation(),
     * getNeighbours(), and
     * getDistanceToTarget()
     * in ExplorationState.
     * You know you are standing on the orb when getDistanceToTarget() is 0.
     * <p>
     * Use function moveTo(long id) in ExplorationState to move to a neighboring
     * tile by its ID. Doing this will change state to reflect your new position.
     * <p>
     * A suggested first implementation that will always find the orb, but likely won't
     * receive a large bonus multiplier, is a depth-first search.
     *
     * @param state the information available at the current state
     */
    public void explore(ExplorationState state) {
        // Keep a list of visited nodes, to avoid visiting the same node twice
        List<NodeStatus> visited = new ArrayList<>();

        // Keep the current path in the stack, to be able to go back when we find a dead end
        Stack<NodeStatus> currentPath = new Stack<>();

        while (state.getDistanceToTarget() > 0) {
            // Generate a list with the next possible moves, excluding the nodes that were visited.
            List<NodeStatus> toVisit = new ArrayList<>();
            for (NodeStatus node : state.getNeighbours()) {
                if (!visited.contains(node)) {
                    toVisit.add(node);
                }
            }

            NodeStatus closerNode = null;
            if (!toVisit.isEmpty()) {
                // Sort the nodes we can visit by how close they are to the target
                toVisit.sort(NodeStatus::compareTo);

                // Pick the next possible move
                for (NodeStatus node : toVisit) {
                    closerNode = node;
                    visited.add(node);
                    currentPath.add(node);
                    break;
                }
            } else {
                // If there are no more possible moves, we must go back on our previous path
                currentPath.pop();
                closerNode = currentPath.peek();
            }

            state.moveTo(closerNode.getId());
        }
    }

    /**
     * Escape from the cavern before the ceiling collapses, trying to collect as much
     * gold as possible along the way. Your solution must ALWAYS escape before time runs
     * out, and this should be prioritized above collecting gold.
     * <p>
     * You now have access to the entire underlying graph, which can be accessed through EscapeState.
     * getCurrentNode() and getExit() will return you Node objects of interest, and getVertices()
     * will return a collection of all nodes on the graph.
     * <p>
     * Note that time is measured entirely in the number of steps taken, and for each step
     * the time remaining is decremented by the weight of the edge taken. You can use
     * getTimeRemaining() to get the time still remaining, pickUpGold() to pick up any gold
     * on your current tile (this will fail if no such gold exists), and moveTo() to move
     * to a destination node adjacent to your current node.
     * <p>
     * You must return from this function while standing at the exit. Failing to do so before time
     * runs out or returning from the wrong location will be considered a failed run.
     * <p>
     * You will always have enough time to escape using the shortest path from the starting
     * position to the exit, although this will not collect much gold.
     *
     * @param state the information available at the current state
     */
    public void escape(EscapeState state) {
        Stack<Node> route = getShortestRoute(state.getCurrentNode(), state.getExit());

        // Estimate the required time to go through the shortest route
        // This will be used to decide if we should pick up more gold around the neighbours or not
        int requiredTime = 0;
        for (int i = 1; i < route.size(); i++) {
            requiredTime += route.get(i - 1).getEdge(route.get(i)).length;
        }

        while (!route.empty()) {
            // Pick gold before moving further
            if (state.getCurrentNode().getTile().getGold() > 0) {
                state.pickUpGold();
            }

            // identify the next node to move on
            Node nextNode = route.pop();

            // If we have time, make a small detour and collect gold around the current node
            if (state.getTimeRemaining() > requiredTime) {
                // Select from the neighbour nodes, the one that has more gold
                Optional<Node> selectNodeWithGold = state.getCurrentNode().getNeighbours().stream()
                        .filter(n -> n.getTile().getGold() > 0)
                        .max(Comparator.comparingInt(n -> n.getTile().getGold()));

                if (selectNodeWithGold.isPresent()) {
                    Node goBackNode = state.getCurrentNode();
                    Node nodeWithMostGold = selectNodeWithGold.get();
                    state.moveTo(nodeWithMostGold);
                    state.pickUpGold();

                    // Don't go back if the node is part of our path to escape
                    if (!nodeWithMostGold.equals(nextNode)) {
                        state.moveTo(goBackNode);
                    }
                }
            }

            // Move forward to the next node in the path
            if (!state.getCurrentNode().equals(nextNode)) {
                state.moveTo(nextNode);
            }
        }
    }

    /**
     * Tuple structure of Node and Weight to be used internally for shortest route calculation.
     */
    private class NodeAndWeightTuple {
        private Node node;
        private int weight;

        private NodeAndWeightTuple(Node node, int weight) {
            this.node = node;
            this.weight = weight;
        }
    }

    /**
     * Calculates the shortest path between start and end nodes.
     * <p>
     * This code is based on Cavern.minPathLengthToTarget() which is used to calculate the time limit for the escape
     * stage of the game.
     *
     * @param start Initial node in the path
     * @param end   Final node in the path
     * @return stack of nodes with the shortest path between start and end
     */
    private Stack<Node> getShortestRoute(Node start, Node end) {
        Map<Node, NodeAndWeightTuple> pathWeights = new HashMap<>();
        pathWeights.put(start, new NodeAndWeightTuple(null, 0));

        PriorityQueue<NodeAndWeightTuple> frontier = new PriorityQueue<>(1, Comparator.comparingInt(n -> n.weight));
        frontier.add(new NodeAndWeightTuple(start, 0));

        while (!frontier.isEmpty()) {
            Node node = frontier.poll().node;
            if (node.equals(end)) {
                break;
            }

            int nWeight = pathWeights.get(node).weight;

            for (Edge edge : node.getExits()) {
                Node edgeNode = edge.getOther(node);
                NodeAndWeightTuple existingTuple = pathWeights.get(edgeNode);

                int weightThroughN = nWeight + edge.length();

                if (existingTuple == null) {
                    pathWeights.put(edgeNode, new NodeAndWeightTuple(node, weightThroughN));
                    frontier.add(new NodeAndWeightTuple(edgeNode, weightThroughN));
                } else if (weightThroughN < existingTuple.weight) {
                    pathWeights.put(edgeNode, new NodeAndWeightTuple(node, weightThroughN));

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

        // Generate the stack of nodes representing the route (begins at the exit node)
        Stack<Node> path = new Stack<>();
        Node node = end;
        while (node != null) {
            path.push(node);
            node = pathWeights.get(node).node;
        }

        return path;
    }

}
