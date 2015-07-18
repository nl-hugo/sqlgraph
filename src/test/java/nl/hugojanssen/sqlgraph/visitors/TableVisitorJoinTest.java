package nl.hugojanssen.sqlgraph.visitors;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;

import nl.hugojanssen.sqlgraph.model.sql.EClauseType;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TableVisitorJoinTest
{
	TableVisitorTestHelper helper = new TableVisitorTestHelper();

	@BeforeTest
	public void setup()
	{
		File file = new File( this.getClass().getResource( "/scripts/valid/tables/JoinTests.sql" ).getFile() );
		this.helper.getParseResults( file );
	}

	@Test( description = "Tests insert statements with join in where condition" )
	public void testJoinInWhere()
	{
		this.helper.testParseResult( 13, "table1", 21, EClauseType.TARGET );
		this.helper.testParseResult( 14, "table2", 22, EClauseType.SOURCE );
	}

	@Test( description = "Tests insert statements left join" )
	public void testLeftJoin()
	{
		this.helper.testParseResult( 3, "table1", 8, EClauseType.TARGET );
		this.helper.testParseResult( 4, "table2", 9, EClauseType.SOURCE );
		this.helper.testParseResult( 5, "table3", 9, EClauseType.SOURCE );
	}

	@Test( description = "Tests insert statements with join in nested subquery" )
	public void testNestedSubquery()
	{
		this.helper.testParseResult( 9, "table1", 16, EClauseType.TARGET );
		this.helper.testParseResult( 10, "table2", 17, EClauseType.SOURCE );
		this.helper.testParseResult( 11, "table3", 17, EClauseType.SOURCE );
		this.helper.testParseResult( 12, "table4", 17, EClauseType.SOURCE );
	}

	@Test( description = "Tests size of the resultset" )
	public void testResultSet()
	{
		assertThat( this.helper.getResultSetSize() ).isEqualTo( 15 );
	}

	@Test( description = "Tests insert statements with join on subquery" )
	public void testSubquery()
	{
		this.helper.testParseResult( 6, "table1", 12, EClauseType.TARGET );
		this.helper.testParseResult( 7, "table2", 13, EClauseType.SOURCE );
		this.helper.testParseResult( 8, "table3", 13, EClauseType.SOURCE );
	}

	@Test( description = "Tests insert statements with union all" )
	public void testUnionAll()
	{
		this.helper.testParseResult( 0, "table1", 2, EClauseType.TARGET );
		this.helper.testParseResult( 1, "table2", 3, EClauseType.SOURCE );
		this.helper.testParseResult( 2, "table3", 5, EClauseType.SOURCE );
	}
}
