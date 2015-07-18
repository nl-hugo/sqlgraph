package nl.hugojanssen.sqlgraph.visitors;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;

import nl.hugojanssen.sqlgraph.model.sql.EClauseType;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TableVisitorInsertTest
{
	TableVisitorTestHelper helper = new TableVisitorTestHelper();

	@BeforeTest
	public void setup()
	{
		File file = new File( this.getClass().getResource( "/scripts/valid/tables/InsertTests.sql" ).getFile() );
		this.helper.getParseResults( file );
	}

	@Test( description = "Tests insert statements with values, single row, with named columns" )
	public void testInsert1()
	{
		this.helper.testParseResult( 0, "table1", 2, EClauseType.TARGET );
	}

	@Test( description = "Tests insert statements with values, multiple rows, no named columns" )
	public void testInsert2()
	{
		this.helper.testParseResult( 1, "table1", 6, EClauseType.TARGET );
	}

	@Test( description = "Tests insert statements with subquery, named columns, with function" )
	public void testInsert3()
	{
		this.helper.testParseResult( 2, "table1", 10, EClauseType.TARGET );
		this.helper.testParseResult( 3, "table2", 11, EClauseType.SOURCE );
	}

	@Test( description = "Tests insert statements with subquery, no named columns, no alias" )
	public void testInsert4()
	{
		this.helper.testParseResult( 4, "table1", 14, EClauseType.TARGET );
		this.helper.testParseResult( 5, "table2", 15, EClauseType.SOURCE );
	}

	@Test( description = "Tests insert statements with subquery, no named columns, with alias" )
	public void testInsert5()
	{
		this.helper.testParseResult( 6, "table1", 18, EClauseType.TARGET );
		this.helper.testParseResult( 7, "table2", 19, EClauseType.SOURCE );
	}

	@Test( description = "Tests size of the resultset" )
	public void testResultSet()
	{
		assertThat( this.helper.getResultSetSize() ).isEqualTo( 8 );
	}
}
