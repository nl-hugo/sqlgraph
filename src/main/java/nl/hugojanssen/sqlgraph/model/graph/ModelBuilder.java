package nl.hugojanssen.sqlgraph.model.graph;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.List;

import nl.hugojanssen.sqlgraph.model.sql.SQLScript;

import org.apache.log4j.Logger;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.EdgeColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.gephi.ranking.api.Transformer;
import org.gephi.ranking.plugin.transformer.AbstractColorTransformer;
import org.openide.util.Lookup;

public class ModelBuilder
{
	/** The logger */
	private final static Logger LOG = Logger.getLogger( ModelBuilder.class );

	// could be synchronized
	public static ModelBuilder getInstance()
	{
		if ( instance == null )
		{
			instance = new ModelBuilder();
		}
		return instance;
	}

	private Workspace workspace;

	private GraphModel graphModel;

	private DirectedGraph graph;

	private static ModelBuilder instance;

	// Constructor must be protected or private to prevent creating new object
	protected ModelBuilder()
	{
		this.setUp();
	}

	//	public boolean addEdge( Node aSource, Node aTarget, String aLabel )
	//	{
	//		LOG.info( "Adding edge from " + aSource + " to " + aTarget + " with label: " + aLabel );
	//
	//		Edge edge = this.graphModel.factory().newEdge( aSource, aTarget, 1f, true );
	//		edge.getEdgeData().setLabel( aLabel );
	//		boolean result = this.graph.addEdge( edge );
	//
	//		String source = aSource.getNodeData().getId();
	//		String target = aTarget.getNodeData().getId();
	//		LOG.info( "Add edge from " + source + " to " + target + " -- " + result );
	//		return result;
	//	}

	//	public GraphModel getGraphModel()
	//	{
	//		return this.graphModel;
	//	}

	//	public DirectedGraph getGraph()
	//	{
	//		return this.graph;
	//	}

	//	public int addNode( String aSQLId, String aLabel, float aSize, Color aColor, float aAlpha )
	//	{
	//		int result = this.getIdForName( aSQLId );
	//
	//		// Create new node if it does not exists
	//		if ( result < 0 )
	//		{
	//			result = this.createNode( aSQLId, aLabel, aSize, aColor, aAlpha );
	//		}
	//		else
	//		{
	//			LOG.warn( "Already there " + aSQLId + " returning id: " + result );
	//		}
	//		return result;
	//	}

	//	public int createNode( String aSQLId, String aLabel, float aSize, Color aColor, float aAlpha )
	//	{
	//		Node node = this.graphModel.factory().newNode( aSQLId );
	//		node.getNodeData().setLabel( aLabel );
	//		node.getNodeData().setSize( aSize );
	//		node.getNodeData().setColor( aColor.getRed(), aColor.getGreen(), aColor.getBlue() );
	//		node.getNodeData().setAlpha( aAlpha );
	//
	//		this.addNode( node );
	//		boolean result = this.graph.addNode( node );
	//		LOG.debug( "Added node " + node.getId() + " -- " + node.getNodeData().getId() + " -- " + result );
	//
	//		return node.getId();
	//		//return node;
	//	}

	/**
	 * @param currFile
	 * @param target
	 * @param sources
	 */
	//	public void addGraphElements( File currFile, ParseResultNode target, List<ParseResultNode> sources )
	public void addGraphElements( SQLScript currFile, ParseResultNode target, List<ParseResultNode> sources )
	{
		LOG.debug( "Adding graph element " + target + " -- " + sources );

		Node targetNode = target.toNode();
		int targetId = this.addNode( targetNode );

		for ( ParseResultNode source : sources )
		{
			Node sourceNode = source.toNode();
			int sourceId = this.addNode( sourceNode );
			LOG.debug( "Adding graph element " + targetId + " -- " + sourceId );

			//this.createEdge( sourceId, targetId, currFile.getName() );

			Edge edge = this.newEdge( currFile.getName(), sourceNode, targetNode, 1f, true );
			this.graph.addEdge( edge );
		}

	}

	//	public void clear()
	//	{
	//		this.graph.clear();
	//	}

	//	public void addEdge( Edge aEdge )
	//	{
	//		//TODO: check if exists!
	//		this.graph.addEdge( aEdge );
	//	}

	//	public void addEdge( SQLScript aScript, Node aSource, Node aTarget )
	//	{
	//		//this.graph.addEdge( aEdge );
	//		// create edge first
	//		Edge edge = this.graphModel.factory().newEdge( aScript.getName(), aSource, aTarget, 1f, true );
	//		this.graph.addEdge( edge );
	//	}

	public int addEdge( Edge aEdge )
	{
		int edgeId = -1;

		if ( aEdge != null )
		{
			String name = this.getEdgeName( aEdge );
			if ( !this.exists( aEdge ) )
			{
				boolean result = this.graph.addEdge( aEdge );
				if ( result )
				{
					edgeId = aEdge.getId();
					LOG.debug( "Added edge '" + name + "' (id=" + edgeId + ")" );
				}
				else
				{
					LOG.debug( "Failed to add edge " + name );
				}
			}
			else
			{
				// TODO: does NOT return the edgeId of the existing edge!
				//edgeId = this.getEdgeIdForName( name );
				LOG.debug( "Edge '" + name + "' already exists (id=" + edgeId + ")" );
			}
		}
		else
		{
			LOG.debug( "Edge 'null' cannot be added" );
		}
		return edgeId;
	}

	/**
	 * aNode.getId() // returns the internal id (int) aNode.getNodeData().getId() // returns the 'soft' id, or the
	 * internal id if no soft is was set
	 * 
	 * @param aNode the Node to add
	 * @return the unique id of the Node in the model if it did not exist in the model before, the unique id of an
	 *         existing Node in the model with the same name, or -1 if the Node could not be added to the model.
	 */
	public int addNode( Node aNode )
	{
		LOG.debug( "Adding node '" + aNode + "' to model" );
		int nodeId = -1;

		if ( aNode != null )
		{
			String name = this.getNodeName( aNode );
			if ( !this.exists( aNode ) )
			{
				boolean result = this.graph.addNode( aNode );
				if ( result )
				{
					nodeId = aNode.getId();
					LOG.debug( "Added node '" + name + "' (id=" + nodeId + ")" );
				}
				else
				{
					LOG.debug( "Failed to add node " + name );
				}
			}
			else
			{
				nodeId = this.getNodeIdForName( name );
				LOG.debug( "Node '" + name + "' already exists (id=" + nodeId + ")" );
			}
		}
		else
		{
			LOG.debug( "Node 'null' cannot be added" );
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

	public boolean exists( Edge aEdge )
	{
		boolean found = false;
		Edge[] edges = this.graph.getEdges().toArray();
		for ( int i = 0; i < edges.length && !found; i++ )
		{
			found = this.equals( edges[i], aEdge );
		}
		return found;
	}

	public boolean equals( Edge aEdge1, Edge aEdge2 )
	{
		// an edge is unique for its name+source+target
		if ( aEdge1 == aEdge2 )
		{
			return true;
		}
		if ( aEdge2 == null || aEdge1 == null )
		{
			return false;
		}
		if ( !this.getEdgeName( aEdge1 ).equalsIgnoreCase( this.getEdgeName( aEdge2 ) ) )
		{
			return false;
		}
		if ( !this.equals( aEdge1.getSource(), aEdge2.getSource() ) && !this.equals( aEdge1.getTarget(), aEdge2.getTarget() ) )
		{
			return false;
		}
		// directed or not?!
		return true;
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
		if ( !this.getNodeName( aNode1 ).equalsIgnoreCase( this.getNodeName( aNode2 ) ) )
		{
			return false;
		}
		return true;
	}

	//	public List<Integer> getEdgeIdForName( String aName )
	//	{
	//		List<Integer> result = new ArrayList<Integer>();
	//		Edge[] iter = (Edge[]) this.getEdgeByName( aName ).toArray();
	//		for ( Edge edge : iter )
	//		{
	//			result.add( edge.getId() );
	//		}
	//		return result;
	//	}

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

	public Node getNodeByName( String aName )
	{
		Node result = null;
		Node[] iter = this.graph.getNodes().toArray();
		for ( int i = 0; i < iter.length && result == null; i++ )
		{
			Node node = iter[i];
			String name = this.getNodeName( node );
			if ( name != null && name.equalsIgnoreCase( aName ) )
			{
				result = node;
			}
		}
		return result;
	}

	//	public List<Edge> getEdgesByName( String aName )
	//	{
	//		List<Edge> result = new ArrayList<Edge>();
	//		Edge[] iter = this.graph.getEdges().toArray();
	//		for ( Edge edge : iter )
	//		{
	//			String name = this.getEdgeName( edge );
	//			if ( name != null && name.equalsIgnoreCase( aName ) )
	//			{
	//				result.add( edge );
	//			}
	//		}
	//		return result;
	//	}

	public int getNodeCount()
	{
		return this.graph.getNodeCount();
	}

	public int getEdgeCount()
	{
		return this.graph.getEdgeCount();
	}

	public String getNodeName( Node aNode )
	{
		String result = null;
		if ( aNode != null )
		{
			result = aNode.getNodeData().getId();
		}
		return result;
	}

	public String getEdgeName( Edge aEdge )
	{
		String result = null;
		if ( aEdge != null )
		{
			result = aEdge.getEdgeData().getId();
		}
		return result;
	}

	public Node newNode()
	{
		return this.newNode( null );
	}

	public Edge newEdge( String aName, Node aSource, Node aTarget, float aWeight, boolean aDirected )
	{
		LOG.debug( "New edge from " + aSource + " to " + aTarget );
		Edge result = null;
		if ( this.exists( aSource ) && this.exists( aTarget ) )
		{
			result = this.factory().newEdge( aName, aSource, aTarget, aWeight, aDirected );
			LOG.debug( "New edge " + result );
		}
		return result;
	}

	public Edge newEdge( Node aSource, Node aTarget, float aWeight, boolean aDirected )
	{
		return this.newEdge( null, aSource, aTarget, aWeight, aDirected );
	}

	public Edge newEdge( Node aSource, Node aTarget )
	{
		return this.newEdge( aSource, aTarget, 1f, true );
	}

	public Node newNode( String aId )
	{
		return this.factory().newNode( aId );
	}

	public void toFile()
	{
		LOG.info( "Export graph" );
		LOG.info( this.graph.getNodeCount() );
		LOG.info( this.graph.getEdgeCount() );

		//TODO: switch labels on by default
		//TODO: opacity?
		//TODO: color * 255

		PreviewModel model = Lookup.getDefault().lookup( PreviewController.class ).getModel();
		model.getProperties().putValue( PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE );
		model.getProperties().putValue( PreviewProperty.DIRECTED, Boolean.TRUE );
		model.getProperties().putValue( PreviewProperty.NODE_LABEL_SHOW_BOX, Boolean.TRUE );

		model.getProperties().putValue( PreviewProperty.EDGE_COLOR, new EdgeColor( Color.BLUE ) );

		RankingController rankingController = Lookup.getDefault().lookup( RankingController.class );
		Ranking degreeRanking = rankingController.getModel().getRanking( Ranking.NODE_ELEMENT, Ranking.DEGREE_RANKING );
		AbstractColorTransformer colorTransformer =
			(AbstractColorTransformer) rankingController.getModel().getTransformer( Ranking.NODE_ELEMENT, Transformer.RENDERABLE_COLOR );

		colorTransformer.setColors( new Color[] { new Color( 0xF7F4F9 ), new Color( 0x67001F ) } );
		//		rankingController.transform( colorTransformer );
		rankingController.transform( degreeRanking, colorTransformer );

		//		VizController.getInstance().getTextManager().getModel().setShowNodeLabels(true);

		//Export full graph
		ExportController ec = Lookup.getDefault().lookup( ExportController.class );
		//Exporter exporterGraphML = ec.getExporter( "graphml" ); //Get GraphML exporter
		//GraphExporter exporter = (GraphExporter) ec.getExporter( "gexf" ); //Get GraphML exporter
		//exporter.setExportVisible( true );
		//		exporterGraphML.setWorkspace( this.workspace );
		try
		{
			//			ec.exportFile( new File( "io_graphml.graphml" ), exporterGraphML );
			//			ec.exportFile( new File( "io_graphml.gexf" ), exporter );
			ec.exportFile( new File( "io_graphml.gexf" ) );
		}
		catch ( IOException ex )
		{
			ex.printStackTrace();
			return;
		}
	}

	/**
	 * @param sourceId
	 * @param targetId
	 * @param name
	 * @return
	 */
	//	private boolean createEdge( int sourceId, int targetId, String aLabel )
	//	{
	//		// TODO Auto-generated method stub
	//		// deze moet rekening houden dat de nodes al bestaan
	//		Node target = this.graphModel.getGraph().getNode( targetId );
	//		Node source = this.graphModel.getGraph().getNode( sourceId );
	//
	//		LOG.info( "Create edge from " + targetId + " to " + sourceId );
	//		LOG.info( "Create edge from " + target + " to " + source );
	//
	//		return this.addEdge( source, target, aLabel );
	//	}

	private GraphFactory factory()
	{
		return this.graphModel.factory();
	}

	private void printStatus()
	{
		LOG.info( "*** The graph now has " + this.graph.getNodeCount() + " nodes and " + this.graph.getEdgeCount() + " edges" );
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
	}
}