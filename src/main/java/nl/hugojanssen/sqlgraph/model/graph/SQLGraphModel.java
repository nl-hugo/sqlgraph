package nl.hugojanssen.sqlgraph.model.graph;

import java.util.List;

import org.apache.log4j.Logger;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

public class SQLGraphModel
{
	/** The logger */
	private final static Logger LOG = Logger.getLogger( SQLGraphModel.class );

	// could be synchronized
	public static SQLGraphModel getInstance()
	{
		if ( instance == null )
		{
			instance = new SQLGraphModel();
		}
		return instance;
	}

	private Workspace workspace;

	private GraphModel graphModel;

	private DirectedGraph graph;

	private static SQLGraphModel instance;

	// Constructor must be protected or private to prevent creating new object
	protected SQLGraphModel()
	{
		this.setUp();
	}

	/**
	 * Adds the specified target and source nodes to the graph and creates an edge between the target and each of the
	 * source nodes.
	 * 
	 * @param aEdgeLabel the edge label
	 * @param target the target node
	 * @param sources the list of source nodes
	 */
	public void addGraphElements( String aEdgeLabel, ParseResultNode target, List<ParseResultNode> sources )
	{
		LOG.debug( "Adding graph element " + target + " -- " + sources + " with label " + aEdgeLabel );

		// validate arguments
		if ( aEdgeLabel == null || target == null || sources == null )
		{
			throw new IllegalArgumentException( "Source list must not be null" );
		}

		int targetId = this.addNode( target );
		for ( ParseResultNode source : sources )
		{
			int sourceId = this.addNode( source );
			if ( this.addEdge( aEdgeLabel, sourceId, targetId, 1f, true ) )
			{
				LOG.debug( "Added edge" );
			}
			else
			{
				LOG.debug( "Could not add edge" );
			}
		}
	}

	public void clear()
	{
		this.graph.clear();
		//		this.graphModel.clear();
		//		this.workspace.

		//		this.setUp();
	}

	private boolean addEdge( Edge aEdge )
	{
		//TODO: check if exists!
		return this.graph.addEdge( aEdge );
	}

	private boolean addEdge( String aName, int aSource, int aTarget, float aWeight, boolean aDirected )
	{
		// name is used as label only!
		boolean result = false;
		if ( aName == null || aName.isEmpty() )
		{
			throw new IllegalArgumentException( "Edge name should not be empty" );
		}

		Node source = this.graph.getNode( aSource );
		Node target = this.graph.getNode( aTarget );
		if ( source != null && target != null )
		{
			// name is not unique!
			// no support for multi graphs!
			Edge edge = this.factory().newEdge( /*aName, */source, target, aWeight, aDirected );
			//Edge edge = this.newEdge( source, target, aWeight, aDirected );
			edge.getEdgeData().setLabel( aName );
			//			result = this.addEdge( edge );

			result = this.addEdge( edge );
		}
		else
		{
			LOG.warn( "Unable to add edge. Target and source must not be empty [" + source + " (id=" + aSource + ") to " + target + " (id="
				+ aTarget + ")]" );
		}
		return result;
	}

	private int addNode( ParseResultNode aNode )
	{
		Node node = SQLGraphModel.getInstance().newNode( aNode.getSQLId() );
		node.getNodeData().setLabel( aNode.getLabel() );
		node.getNodeData().setSize( aNode.getSize() );

		node.getNodeData().setColor( aNode.getColor().getRed() / 255f, aNode.getColor().getGreen() / 255f,
										aNode.getColor().getBlue() / 255f );
		node.getNodeData().setAlpha( aNode.getAlpha() );
		return this.addNode( node );
	}

	/**
	 * aNode.getId() // returns the internal id (int) aNode.getNodeData().getId() // returns the 'soft' id, or the
	 * internal id if no soft is was set
	 * 
	 * @param aNode the Node to add
	 * @return the unique id of the Node in the model if it did not exist in the model before, the unique id of an
	 *         existing Node in the model with the same name, or -1 if the Node could not be added to the model.
	 */
	private int addNode( Node aNode )
	{
		LOG.debug( "Adding node '" + aNode + "' to model" );
		int nodeId = -1;

		String name = aNode.getNodeData().getId();
		if ( !this.exists( aNode ) )
		{
			// add new node
			boolean result = this.graph.addNode( aNode );
			if ( result )
			{
				nodeId = aNode.getId();
				LOG.debug( "Added node '" + name + "' (id=" + nodeId + ")" );
			}
		}
		else
		{
			// return the id of the existing node
			nodeId = this.getNodeIdForName( name );
		}
		return nodeId;
	}

	public boolean exists( Node aNode )
	{
		// we cannot use the contains method from graph, since it only checks the internal id
		//return this.getNodeByName( this.getNodeName( aNode ) ) != null;
		boolean found = false;
		Node[] nodes = this.graph.getNodes().toArray();
		for ( int i = 0; i < nodes.length && !found; i++ )
		{
			found = this.equals( nodes[i], aNode );
		}
		return found;
	}

	public boolean equals( Node aNode1, Node aNode2 )
	{
		// an node is unique for its name
		if ( aNode1 == aNode2 )
		{
			return true;
		}
		if ( aNode2 == null || aNode1 == null )
		{
			return false;
		}
		if ( !aNode1.getNodeData().getId().equalsIgnoreCase( aNode2.getNodeData().getId() ) )
		//		if ( !this.getNodeName( aNode1 ).equalsIgnoreCase( this.getNodeName( aNode2 ) ) )
		{
			return false;
		}
		return true;
	}

	/**
	 * Returns the id of the node with the specified name. In this graph, a node can be uniquely identified by its name.
	 * 
	 * @param aName the name of the node to look for
	 * @return the id of the node with the specified name, or -1 when no such node exists.
	 */
	public int getNodeIdForName( String aName )
	{
		int result = -1;
		Node node = this.getNodeByName( aName );
		if ( node != null )
		{
			result = node.getId();
		}
		return result;
	}

	/**
	 * Returns the node with the specified name. In this graph, a node can be uniquely identified by its name.
	 * 
	 * @param aName the name of the node to look for
	 * @return the node with the specified name, or null when no such node exists
	 */
	public Node getNodeByName( String aName )
	{
		Node result = null;
		Node[] iter = this.graph.getNodes().toArray();
		for ( int i = 0; i < iter.length && result == null; i++ )
		{
			Node node = iter[i];
			String name = node.getNodeData().getId();
			if ( name != null && name.equalsIgnoreCase( aName ) )
			{
				result = node;
			}
		}
		return result;
	}

	/**
	 * Returns the number of nodes in the graph.
	 * 
	 * @return the number of nodes in the graph
	 */
	public int getNodeCount()
	{
		return this.graph.getNodeCount();
	}

	/**
	 * Returns the number of edges in the graph.
	 * 
	 * @return the number of edges in the graph
	 */
	public int getEdgeCount()
	{
		return this.graph.getEdgeCount();
	}

	private Node newNode( String aId )
	{
		return this.factory().newNode( aId );
	}

	//	public void toFile( File aFile )
	//	{
	//		LOG.info( "Export graph" );
	//		LOG.info( this.graph.getNodeCount() );
	//		LOG.info( this.graph.getEdgeCount() );
	//
	//		// TODO: switch labels on by default
	//		// TODO: opacity?
	//		// TODO: show labels by default
	//		// TODO: set gexf version 1.2
	//		// TODO: set default edge/graph type DIRECTED
	//		// TODO: YifanHu multilevel(?) layout
	//
	//		PreviewModel model = Lookup.getDefault().lookup( PreviewController.class ).getModel();
	//		model.getProperties().putValue( PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE );
	//		model.getProperties().putValue( PreviewProperty.DIRECTED, Boolean.TRUE );
	//		model.getProperties().putValue( PreviewProperty.NODE_LABEL_SHOW_BOX, Boolean.TRUE );
	//
	//		model.getProperties().putValue( PreviewProperty.EDGE_COLOR, new EdgeColor( Color.BLUE ) );
	//
	//		//		VizController.getInstance().getTextManager().getModel().setShowNodeLabels( true );
	//
	//		//Export full graph
	//		ExportController ec = Lookup.getDefault().lookup( ExportController.class );
	//		GraphExporter exporter = (GraphExporter) ec.getExporter( "gexf" ); //Get GraphML exporter
	//		exporter.setExportVisible( true );
	//
	//		try
	//		{
	//			ec.exportFile( aFile, exporter );
	//		}
	//		catch ( Exception ex )
	//		{
	//			ex.printStackTrace();
	//			return;
	//		}
	//	}

	/**
	 * Returns an array with the nodes in the graph.
	 * 
	 * @return an array with the nodes in the graph
	 */
	public Node[] getNodes()
	{
		return this.graph.getNodes().toArray();
	}

	/**
	 * Returns an array with the edges in the graph.
	 * 
	 * @return an array with the edges in the graph
	 */
	public Edge[] getEdges()
	{
		return this.graph.getEdges().toArray();
	}

	private GraphFactory factory()
	{
		return this.graphModel.factory();
	}

	private void setUp()
	{
		//Init a project - and therefore a workspace
		ProjectController pc = Lookup.getDefault().lookup( ProjectController.class );
		pc.newProject();
		this.workspace = pc.getCurrentWorkspace();

		//Get a graph model - it exists because we have a workspace
		this.graphModel = Lookup.getDefault().lookup( GraphController.class ).getModel();

		// Initialize the graph
		this.graph = this.graphModel.getDirectedGraph();

		System.out.println( this.workspace );

	}

	/**
	 * @return
	 */
	public Workspace getCurrentWorkspace()
	{
		return this.workspace;
	}
}