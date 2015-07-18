package nl.hugojanssen.sqlgraph.model.graph;

import static org.fest.assertions.Assertions.assertThat;

import nl.hugojanssen.sqlgraph.model.graph.ModelBuilder;

import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Node;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ModelBuilderTest
{
	private ModelBuilder model;

	private Edge edge1, edge2, edge3;

	private Node node1, node2, node3, node4;

	@BeforeTest
	public void setup()
	{
		this.model = ModelBuilder.getInstance();
	}

	@Test( groups = "add-edge", dependsOnGroups = "new-edge", description = "Tests addEdge" )
	public void testAddEdge()
	{
		assertThat( this.model.addEdge( null ) ).isEqualTo( -1 );
		assertThat( this.model.addEdge( this.edge1 ) ).isEqualTo( 1 );

		// trying to add it again yields no id; this DIFFERS from the way that duplicate nodes are added!
		assertThat( this.model.addEdge( this.edge1 ) ).isEqualTo( -1 );

		// name exists, but with different source and target
		assertThat( this.model.addEdge( this.edge3 ) ).isEqualTo( 3 );

		// 1 edge was added
		assertThat( this.model.getEdgeCount() ).isEqualTo( 2 );
	}

	@Test( groups = "add-node", dependsOnGroups = "new-node", description = "Tests addNode" )
	public void testAddNode()
	{
		assertThat( this.model.addNode( null ) ).isEqualTo( -1 );
		assertThat( this.model.addNode( this.node1 ) ).isEqualTo( 1 );
		assertThat( this.model.addNode( this.node2 ) ).isEqualTo( 2 );

		// node with this name already exists, returns id of node2
		assertThat( this.model.addNode( this.node3 ) ).isEqualTo( 2 );

		// only 2 nodes were added
		assertThat( this.model.getNodeCount() ).isEqualTo( 2 );
	}

	@Test( groups = "construct-model", description = "Tests constructor" )
	public void testConstructor()
	{
		assertThat( this.model ).isNotNull();
	}

	@Test( dependsOnGroups = "add-edge", description = "Tests exists (Edge) " )
	public void testExistsEdge()
	{
		assertThat( this.model.exists( (Edge) null ) ).isFalse();
		assertThat( this.model.exists( this.edge1 ) ).isTrue();
		assertThat( this.model.exists( this.edge2 ) ).isFalse();
	}

	@Test( dependsOnGroups = "add-node", description = "Tests exists (Node)" )
	public void testExistsNode()
	{
		assertThat( this.model.exists( (Node) null ) ).isFalse();
		assertThat( this.model.exists( this.node1 ) ).isTrue();
		assertThat( this.model.exists( this.node2 ) ).isTrue();

		// because a node with the the same name exists (node2)
		assertThat( this.model.exists( this.node3 ) ).isTrue();
	}

	@Test( dependsOnGroups = "add-edge", description = "Tests equals (Edge)" )
	public void testEqualsEdge()
	{
		assertThat( this.model.equals( (Edge) null, (Edge) null ) ).isTrue();
		assertThat( this.model.equals( this.edge1, (Edge) null ) ).isFalse();
		assertThat( this.model.equals( this.edge1, this.edge1 ) ).isTrue();
		assertThat( this.model.equals( this.edge1, this.edge2 ) ).isFalse();

		// node1 and node3 have the same name, but different source/target
		assertThat( this.model.equals( this.edge1, this.edge3 ) ).isFalse();
	}

	@Test( dependsOnGroups = "add-node", description = "Tests equals (Node)" )
	public void testEqualsNode()
	{
		assertThat( this.model.equals( (Node) null, (Node) null ) ).isTrue();
		assertThat( this.model.equals( this.node1, (Node) null ) ).isFalse();
		assertThat( this.model.equals( this.node1, this.node1 ) ).isTrue();
		assertThat( this.model.equals( this.node1, this.node2 ) ).isFalse();

		// node2 and node3 have the same name
		assertThat( this.model.equals( this.node2, this.node3 ) ).isTrue();
	}

	//	@Test( dependsOnGroups = "add-edge", description = "Tests " )
	//	public void testGetEdgeIdForName()
	//	{
	//		assertThat( this.model.getEdgeIdForName( null ) ).isEqualTo( -1 );
	//		assertThat( this.model.getEdgeIdForName( "" ) ).isEqualTo( -1 );
	//		assertThat( this.model.getEdgeIdForName( "doesnotexist" ) ).isEqualTo( -1 );
	//		assertThat( this.model.getEdgeIdForName( "edge1" ) ).isEqualTo( 1 );
	//	}

	@Test( dependsOnGroups = "add-edge", description = "Tests getEdgeName" )
	public void testGetEdgeName()
	{
		assertThat( this.model.getEdgeName( null ) ).isNull();
		assertThat( this.model.getEdgeName( this.edge1 ) ).isEqualTo( "edge1" );
		assertThat( this.model.getEdgeName( this.edge2 ) ).isEqualTo( "2" );
	}

	//	@Test( dependsOnGroups = "add-edge", description = "Tests " )
	//	public void testGetEdgeByName()
	//	{
	//		assertThat( this.model.getEdgeByName( null ) ).isNull();
	//		assertThat( this.model.getEdgeByName( "" ) ).isNull();
	//		assertThat( this.model.getEdgeByName( "doesnotexist" ) ).isNull();
	//		assertThat( this.model.getEdgeByName( "edge1" ) ).isEqualTo( this.edge1 );
	//		assertThat( this.model.getEdgeByName( "EdgE1" ) ).isEqualTo( this.edge1 );
	//	}

	@Test( dependsOnGroups = "add-node", description = "Tests getNodeByName" )
	public void testGetNodeByName()
	{
		assertThat( this.model.getNodeByName( null ) ).isNull();
		assertThat( this.model.getNodeByName( "" ) ).isNull();
		assertThat( this.model.getNodeByName( "doesnotexist" ) ).isNull();
		assertThat( this.model.getNodeByName( "1" ) ).isEqualTo( this.node1 );
		assertThat( this.model.getNodeByName( "test" ) ).isEqualTo( this.node2 );
		assertThat( this.model.getNodeByName( "TesT" ) ).isEqualTo( this.node2 );
		assertThat( this.model.getNodeByName( "test" ) ).isNotEqualTo( this.node3 );
	}

	@Test( dependsOnGroups = "add-node", description = "Tests getNodeIdForName" )
	public void testGetNodeIdForName()
	{
		assertThat( this.model.getNodeIdForName( null ) ).isEqualTo( -1 );
		assertThat( this.model.getNodeIdForName( "" ) ).isEqualTo( -1 );
		assertThat( this.model.getNodeIdForName( "doesnotexist" ) ).isEqualTo( -1 );

		// name not set, thus returns the model internal id
		assertThat( this.model.getNodeIdForName( "1" ) ).isEqualTo( 1 );

		// return by name
		assertThat( this.model.getNodeIdForName( "test" ) ).isEqualTo( 2 );
	}

	@Test( dependsOnGroups = "add-node", description = "Tests getNodeName" )
	public void testGetNodeName()
	{
		assertThat( this.model.getNodeName( null ) ).isNull();
		assertThat( this.model.getNodeName( this.node1 ) ).isEqualTo( "1" );
		assertThat( this.model.getNodeName( this.node2 ) ).isEqualTo( "test" );
		assertThat( this.model.getNodeName( this.node3 ) ).isEqualTo( "test" );
		assertThat( this.model.getNodeName( this.node4 ) ).isEqualTo( "notinmodel" );
	}

	@Test( groups = "new-edge", dependsOnGroups = "add-node", description = "Tests newEdge" )
	public void testNewEdge()
	{
		assertThat( this.model.newEdge( null, null ) ).isNull();
		assertThat( this.model.newEdge( this.node1, null ) ).isNull();

		// node4 was never added to the model
		assertThat( this.model.newEdge( this.node4, this.node1 ) ).isNull();

		this.edge1 = this.model.newEdge( "edge1", this.node1, this.node2, 5f, true );
		assertThat( this.edge1.getId() ).isEqualTo( 1 );
		assertThat( this.edge1.getEdgeData().getId() ).isEqualTo( "edge1" );

		this.edge2 = this.model.newEdge( this.node2, this.node1, 10f, true );
		assertThat( this.edge2.getId() ).isEqualTo( 2 );
		assertThat( this.edge2.getEdgeData().getId() ).isEqualTo( "2" );

		this.edge3 = this.model.newEdge( "edge1", this.node2, this.node1, 10f, true );
		assertThat( this.edge3.getId() ).isEqualTo( 3 );
		assertThat( this.edge3.getEdgeData().getId() ).isEqualTo( "edge1" );

		// no edges were added to the model
		assertThat( this.model.getEdgeCount() ).isEqualTo( 0 );
	}

	@Test( groups = "new-node", dependsOnGroups = "construct-model", description = "Tests newNode" )
	public void testNewNode()
	{
		this.node1 = this.model.newNode();
		assertThat( this.node1.getId() ).isEqualTo( 1 );
		assertThat( this.node1.getNodeData().getId() ).isEqualTo( "1" );

		this.node2 = this.model.newNode( "test" );
		assertThat( this.node2.getId() ).isEqualTo( 2 );
		assertThat( this.node2.getNodeData().getId() ).isEqualTo( "test" );

		this.node3 = this.model.newNode( "test" );
		assertThat( this.node3.getId() ).isEqualTo( 3 );
		assertThat( this.node3.getNodeData().getId() ).isEqualTo( "test" );

		this.node4 = this.model.newNode( "notinmodel" );
		assertThat( this.node4.getId() ).isEqualTo( 4 );
		assertThat( this.node4.getNodeData().getId() ).isEqualTo( "notinmodel" );

		// no nodes were added to the model
		assertThat( this.model.getNodeCount() ).isEqualTo( 0 );
	}

	@Test( dependsOnGroups = "new-node", description = "Tests " )
	public void testNodes()
	{
		assertThat( this.node1 ).isNotEqualTo( this.node2 );
		//		assertThat( this.node2 ).isEqualTo( this.node3 );
	}
}
