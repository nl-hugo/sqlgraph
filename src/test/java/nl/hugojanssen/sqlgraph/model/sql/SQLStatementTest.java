package nl.hugojanssen.sqlgraph.model.sql;

import static org.fest.assertions.Assertions.assertThat;
import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.stmt.TInsertSqlStatement;

import java.util.ArrayList;
import java.util.List;

import nl.hugojanssen.sqlgraph.model.ParseResult;
import nl.hugojanssen.sqlgraph.model.sql.SQLScript;
import nl.hugojanssen.sqlgraph.model.sql.SQLStatement;
import nl.hugojanssen.sqlgraph.visitors.HelloWorldVisitor;
import nl.hugojanssen.sqlgraph.visitors.SQLVisitor;
import nl.hugojanssen.sqlgraph.visitors.VisitorListener;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SQLStatementTest implements VisitorListener
{
	private SQLStatement statement;

	private boolean visited = false;

	@BeforeTest
	public void setup()
	{
		// parse a simple INSERT statement
		TGSqlParser parser = new TGSqlParser( EDbVendor.dbvpostgresql );
		parser.sqltext = "insert into films values('MIB',1999);";
		assertThat( parser.parse() ).isEqualTo( 0 );

		TInsertSqlStatement insert = (TInsertSqlStatement) parser.sqlstatements.get( 0 );
		this.statement = new SQLStatement( null, insert );
	}

	@Test( groups = "construct-statement", description = "Tests constructor" )
	public void testConstructor()
	{
		assertThat( this.statement ).isNotNull();
		assertThat( this.statement.getVisitors().size() ).isEqualTo( 0 );
	}

	@Test( groups = "set-visitors", dependsOnGroups = "construct-statement", description = "Tests setVisitors" )
	public void testSetVisitors()
	{
		HelloWorldVisitor visitor = new HelloWorldVisitor( this );
		List<SQLVisitor> list = new ArrayList<SQLVisitor>();
		list.add( visitor );
		this.statement.setVisitors( list );
		assertThat( this.statement.getVisitors().size() ).isEqualTo( 1 );
	}

	@Test( dependsOnGroups = "set-visitors", description = "Tests visit" )
	public void testVisit() throws InterruptedException
	{
		this.statement.visit();
		assertThat( this.visited ).isTrue();
	}

	@Override
	public void update( ParseResult aResult )
	{
		// omitted
	}

	@Override
	public void setLocation( SQLScript aFile, long aIndex )
	{
		this.visited = true;
	}
}
