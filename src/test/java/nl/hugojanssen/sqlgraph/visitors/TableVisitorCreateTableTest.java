package nl.hugojanssen.sqlgraph.visitors;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;

import nl.hugojanssen.sqlgraph.model.sql.EClauseType;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TableVisitorCreateTableTest
{
	TableVisitorTestHelper helper = new TableVisitorTestHelper();

	@BeforeTest
	public void setup()
	{
		File file = new File( this.getClass().getResource( "/scripts/valid/tables/CreateTableTests.sql" ).getFile() );
		this.helper.getParseResults( file );
	}

	@Test( description = "Creates films table" )
	public void testCreateFilms()
	{
		this.helper.testParseResult( 0, "films", 5, EClauseType.TARGET );
	}

	@Test( description = "Creates distributors table" )
	public void testCreateTableDistributors()
	{
		this.helper.testParseResult( 1, "distributors", 15, EClauseType.TARGET );
	}

	@Test( description = "Tests size of the resultset" )
	public void testResultSet()
	{
		assertThat( this.helper.getResultSetSize() ).isEqualTo( 2 );
	}
}
