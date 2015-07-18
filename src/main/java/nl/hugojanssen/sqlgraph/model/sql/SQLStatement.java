package nl.hugojanssen.sqlgraph.model.sql;

import gudusoft.gsqlparser.TCustomSqlStatement;

import java.util.ArrayList;
import java.util.List;

import nl.hugojanssen.sqlgraph.visitors.SQLVisitor;

/**
 * @author hjanssen
 */
public class SQLStatement
{
	private List<SQLVisitor> visitors = new ArrayList<SQLVisitor>();

	private final TCustomSqlStatement statement;

	private final SQLScript parent;

	/**
	 * @param aScript
	 * @param aStatement
	 */
	public SQLStatement( SQLScript aScript, TCustomSqlStatement aStatement )
	{
		this.parent = aScript;
		this.statement = aStatement;
	}

	/**
	 * @param aVisitorsList
	 */
	public void setVisitors( List<SQLVisitor> aVisitorsList )
	{
		this.visitors = aVisitorsList;
	}

	/**
	 * 
	 */
	public void visit()
	{
		System.out.println( "visiting1" );
		for ( int i = 0; i < this.visitors.size(); i++ )
		{
			System.out.println( "visiting" );
			SQLVisitor visitor = this.visitors.get( i );
			visitor.getListener().setLocation( this.parent, this.statement.getLineNo() );
			this.statement.accept( visitor );
		}
	}

	/**
	 * @return
	 */
	public List<SQLVisitor> getVisitors()
	{
		return this.visitors;
	}
}