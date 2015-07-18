package nl.hugojanssen.sqlgraph.model.graph;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import nl.hugojanssen.sqlgraph.model.graph.ModelBuilder;
import nl.hugojanssen.sqlgraph.model.graph.ParseResultNode;
import nl.hugojanssen.sqlgraph.model.sql.EClauseType;
import nl.hugojanssen.sqlgraph.model.sql.SQLScript;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class CopyOfModelBuilderTest
{
	private ModelBuilder model;

	@BeforeTest
	public void setup()
	{
		this.model = ModelBuilder.getInstance();
	}

	@Test( enabled = false, description = "Tests getEdgeName" )
	public void testAddGraphElements() throws IllegalArgumentException, IOException
	{
		File file = new File( this.getClass().getResource( "/scripts/valid/tables/JoinTests.sql" ).getFile() );
		SQLScript script = new SQLScript( file, null );
		ParseResultNode node1 = new ParseResultNode( this, null, "table1", EClauseType.TARGET, 1, 1 );
		ParseResultNode node2 = new ParseResultNode( this, null, "table2", EClauseType.SOURCE, 1, 1 );
		ArrayList<ParseResultNode> sources = new ArrayList<ParseResultNode>();
		sources.add( node2 );

		// simple graph with 2 nodes and 1 edge
		this.model.addGraphElements( script, node1, sources );
		assertThat( this.model.getNodeCount() ).isEqualTo( 2 );
		assertThat( this.model.getEdgeCount() ).isEqualTo( 1 );

		File file2 = new File( this.getClass().getResource( "/scripts/valid/tables/InsertTests.sql" ).getFile() );
		SQLScript script2 = new SQLScript( file2, null );
		ParseResultNode node3 = new ParseResultNode( this, null, "table2", EClauseType.SOURCE, 1, 1 );
		ParseResultNode node4 = new ParseResultNode( this, null, "table3", EClauseType.SOURCE, 1, 1 );
		//		sources.clear();
		sources.add( node3 );
		sources.add( node4 );

		// 3 nodes and 2 edges, 1 duplicate node
		this.model.addGraphElements( script2, node1, sources );
		assertThat( this.model.getNodeCount() ).isEqualTo( 3 );
		assertThat( this.model.getEdgeCount() ).isEqualTo( 2 );
	}
}
