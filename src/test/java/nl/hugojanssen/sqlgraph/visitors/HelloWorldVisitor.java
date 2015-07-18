package nl.hugojanssen.sqlgraph.visitors;

import nl.hugojanssen.sqlgraph.model.ParseResult;
import nl.hugojanssen.sqlgraph.visitors.SQLVisitor;
import nl.hugojanssen.sqlgraph.visitors.VisitorListener;
import gudusoft.gsqlparser.stmt.TInsertSqlStatement;

/**
 * @author hjanssen
 */
public class HelloWorldVisitor extends SQLVisitor
{
	/**
	 * @param aInterface
	 */
	public HelloWorldVisitor( VisitorListener aInterface )
	{
		super( aInterface );
	}

	@Override
	public void preVisit( TInsertSqlStatement statement )
	{
		this.addParseResult();
	}

	/**
	 * 
	 */
	private void addParseResult()
	{
		ParseResult result = new ParseResult( this, "hello", "world", null, -1, -1 );
		super.getListener().update( result );
	}

}
