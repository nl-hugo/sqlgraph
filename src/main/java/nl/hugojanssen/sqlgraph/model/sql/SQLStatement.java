package nl.hugojanssen.sqlgraph.model.sql;

import gudusoft.gsqlparser.TCustomSqlStatement;

import java.util.ArrayList;
import java.util.List;

import nl.hugojanssen.sqlgraph.visitors.SQLVisitor;

/**
 * The <code>SQLStatement</code> object encapsulates a <code>TCustomSqlStatement</code> object that was discovered by
 * the parser. It provides a means for the attached visitors to notify their listeners on tokens that are found while
 * parsing.
 * 
 * @author hjanssen
 * @see gudusoft.gsqlparser.TCustomSqlStatement
 */
public class SQLStatement
{
	private List<SQLVisitor> visitors = new ArrayList<SQLVisitor>();

	private final TCustomSqlStatement statement;

	private final SQLScript parent;

	/**
	 * Construct a new <code>SQLStatement</code> object with the specified name and statement.
	 * 
	 * @param aScript the script where the statement resides
	 * @param aStatement the <code>TCustomSqlStatement</code> object
	 * @see gudusoft.gsqlparser.TCustomSqlStatement
	 */
	public SQLStatement( SQLScript aScript, TCustomSqlStatement aStatement )
	{
		this.parent = aScript;
		this.statement = aStatement;
	}

	/**
	 * Returns the list of visitors.
	 * 
	 * @return the list of visitors
	 */
	public List<SQLVisitor> getVisitors()
	{
		return this.visitors;
	}

	/**
	 * Sets the list of visitors to the specified visitor list.
	 * 
	 * @param aVisitorsList a list of visitors for this statement
	 */
	public void setVisitors( List<SQLVisitor> aVisitorsList )
	{
		this.visitors = aVisitorsList;
	}

	/**
	 * Lets the <code>TCustomSqlStatement</code> accept all visitors and signals the visitor listeners that the parse
	 * location changed to the current statement.
	 * 
	 * @see gudusoft.gsqlparser.TCustomSqlStatement.accept()
	 */
	public void visit()
	{
		for ( int i = 0; i < this.visitors.size(); i++ )
		{
			SQLVisitor visitor = this.visitors.get( i );
			visitor.getListener().setLocation( this.parent, this.statement.getLineNo() );
			this.statement.accept( visitor );
		}
	}
}